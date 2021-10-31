package com.weweibuy.pay.wx.config;

import com.weweibuy.pay.wx.client.dto.resp.DownloadCertificateRespDTO;
import com.weweibuy.pay.wx.client.dto.resp.WxResponseHeader;
import com.weweibuy.pay.wx.config.properties.WxAppProperties;
import com.weweibuy.pay.wx.constant.WxApiConstant;
import com.weweibuy.pay.wx.manager.PlatformCertificateManager;
import com.weweibuy.pay.wx.support.CertificatesHelper;
import com.weweibuy.pay.wx.utils.SignAndVerifySignUtils;
import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.support.AlarmService;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import com.weweibuy.framework.common.feign.support.FeignFilter;
import com.weweibuy.framework.common.feign.support.FeignFilterChain;
import com.weweibuy.framework.common.log.support.LogTraceContext;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;

/**
 * 签名验签过滤器
 *
 * @author durenhao
 * @date 2021/10/30 17:27
 **/
@Slf4j
@Order(0)
@RequiredArgsConstructor
@Component
public class VerifySignFeignFilter implements FeignFilter {

    private final PlatformCertificateManager platformCertificateManager;

    private final WxAppProperties wxAppProperties;

    private final AlarmService alarmService;

    private final Validator validator;

    public static final String ALARM_BIZ_TYPE = "微信支付";

    @Override
    public Response filter(Request request, Request.Options options, FeignFilterChain chain) throws IOException {
        // 增加签名请求头
        request = addSingHeader(request);
        Response response = chain.doFilter(request, options);
        return verifySign(response, request);
    }


    private Request addSingHeader(Request request) {
        String authorization = null;
        try {
            authorization = SignAndVerifySignUtils.authorization(request, wxAppProperties);
        } catch (SignatureException e) {
            alarmService.sendAlarm(ALARM_BIZ_TYPE,
                    String.format("请求微信: %s %s 生成签名失败: %s, trace: %s",
                            request.httpMethod(),
                            request.url(),
                            e.getMessage(),
                            LogTraceContext.getTraceCodeOrEmpty()));
            throw Exceptions.business("请求微信签名失败", e);
        } catch (InvalidKeyException e) {
            alarmService.sendAlarm(ALARM_BIZ_TYPE,
                    String.format("请求微信: 商户签名证书无效: %s, trace: %s",
                            e.getMessage(),
                            LogTraceContext.getTraceCodeOrEmpty()));
            throw Exceptions.business("请求微信签名证书无效", e);
        }
        request.requestTemplate().header(WxApiConstant.AUTHORIZATION_HEADER, Collections.singleton(authorization));
        return request.requestTemplate().request();
    }

    /**
     * 验签
     *
     * @param response
     * @param request
     * @return
     * @throws IOException
     */
    private Response verifySign(Response response, Request request) throws IOException {
        // 不验证签名
        if (response == null || isDownLoadBillUrl(request)) {
            return response;
        }
        int status = response.status();
        if (status == 401) {
            alarmService.sendAlarmFormatMsg(ALARM_BIZ_TYPE, "请求微信: %s %s, 签名错误, trace: %s",
                    request.httpMethod(), request.url(), LogTraceContext.getTraceCodeOrEmpty());
            return response;
        }

        if (status < 200 || status >= 300) {
            return response;
        }

        // 响应头
        WxResponseHeader wxResponseHeader = WxResponseHeader.fromWxResponse(response);
        verifyResponseHeader(wxResponseHeader);

        // 下载证书接口
        if (isCertificateUrl(request)) {
            return verifySignWhenDownLoadCertificate(response, request, wxResponseHeader);
        }
        return verifySignAndReBufResponse(response, request, wxResponseHeader, currentUsedCert(wxResponseHeader));
    }


    /**
     * 下载证书时验签
     *
     * @param response
     * @param request
     * @param wxResponseHeader
     * @return
     */
    private Response verifySignWhenDownLoadCertificate(Response response, Request request, WxResponseHeader wxResponseHeader) throws IOException {

        String bodyStr = "";
        Response.Body body = response.body();
        if (body != null) {
            byte[] bodyData = Util.toByteArray(body.asInputStream());
            bodyStr = new String(bodyData);
            if (!body.isRepeatable()) {
                response = response.toBuilder().body(bodyData).build();
            }
        }

        DownloadCertificateRespDTO downloadCertificateRespDTO = JackJsonUtils.readSnakeCaseValue(bodyStr, DownloadCertificateRespDTO.class);

        Map<String, X509Certificate> stringX509CertificateMap = CertificatesHelper.platformCertificate(downloadCertificateRespDTO, wxAppProperties);
        X509Certificate x509Certificate = stringX509CertificateMap.get(wxResponseHeader.getSerial());
        if (x509Certificate == null) {
            alarmService.sendAlarm(ALARM_BIZ_TYPE,
                    String.format("下载平台证书时, 无法找到平台签名证书, trace: %s",
                            LogTraceContext.getTraceCodeOrEmpty()));
            throw Exceptions.business("下载平台证书时, 无法找到平台签名证书");
        }

        verifySignAndAlarm(request, wxResponseHeader, bodyStr, x509Certificate);
        return response;

    }


    private Response verifySignAndReBufResponse(Response response, Request request, WxResponseHeader wxResponseHeader, X509Certificate certificate) throws IOException {

        int status = response.status();
        String bodyStr = "";
        Response.Body body = response.body();
        if (body != null && !(status == 204 || status == 205)) {
            byte[] bodyData = Util.toByteArray(body.asInputStream());
            bodyStr = new String(bodyData);
            if (!body.isRepeatable()) {
                response = response.toBuilder().body(bodyData).build();
            }
        }
        verifySignAndAlarm(request, wxResponseHeader, bodyStr, certificate);
        return response;
    }

    private void verifySignAndAlarm(Request request, WxResponseHeader wxResponseHeader,
                                    String body, X509Certificate certificate) {
        try {
            boolean verifySign = SignAndVerifySignUtils.verifySign(wxResponseHeader, body, certificate);
            // 验签失败
            if (!verifySign) {
                alarmService.sendAlarm(ALARM_BIZ_TYPE,
                        String.format("请求微信: %s %s , 响应验签, 验签失败, trace: %s",
                                request.httpMethod(),
                                request.url(),
                                LogTraceContext.getTraceCodeOrEmpty()));
                throw Exceptions.business("微信响应验签,验签失败");
            }
        } catch (SignatureException e) {
            alarmService.sendAlarm(ALARM_BIZ_TYPE,
                    String.format("请求微信: %s %s 响应验签, 生成签名失败: %s, trace: %s",
                            request.httpMethod(),
                            request.url(),
                            e.getMessage(),
                            LogTraceContext.getTraceCodeOrEmpty()));
            throw Exceptions.business("微信响应验签, 生成签名失败", e);
        } catch (InvalidKeyException e) {
            alarmService.sendAlarm(ALARM_BIZ_TYPE,
                    String.format("请求微信: %s %s , 响应验签, 微信平台证书无效: %s, trace: %s",
                            request.httpMethod(),
                            request.url(),
                            e.getMessage(),
                            LogTraceContext.getTraceCodeOrEmpty()));
            throw Exceptions.business("微信响应验签, 微信平台证书无效", e);
        }
    }


    /**
     * 当前使用的证书
     *
     * @param wxResponseHeader
     * @return
     */
    private X509Certificate currentUsedCert(WxResponseHeader wxResponseHeader) {
        X509Certificate x509Certificate = platformCertificateManager.queryPlatformCertificate(wxResponseHeader.getSerial());
        if (x509Certificate == null) {
            // 重新下载证书
            platformCertificateManager.reloadPlatformCertificate();
        }
        x509Certificate = platformCertificateManager.queryPlatformCertificate(wxResponseHeader.getSerial());
        if (x509Certificate == null) {
            alarmService.sendAlarm(ALARM_BIZ_TYPE,
                    String.format("无法加载获取平台证书: %s, trace: %s",
                            wxResponseHeader.getSerial(),
                            LogTraceContext.getTraceCode().orElse("")));
            throw Exceptions.business("无法加载获取平台证书: " + wxResponseHeader.getSerial());
        }

        return x509Certificate;
    }


    /**
     * 校验请求头
     *
     * @param wxResponseHeader
     */
    private void verifyResponseHeader(WxResponseHeader wxResponseHeader) {
        validator.validate(wxResponseHeader).stream()
                .findFirst()
                .map(c -> c.getMessage())
                .ifPresent(m -> {
                    String msg = m + ", 请求id: " + wxResponseHeader.getRequestId();
                    alarmService.sendAlarm(ALARM_BIZ_TYPE, msg + ", trace: "
                            + LogTraceContext.getTraceCode().orElse(""));
                    throw Exceptions.business(msg);
                });
        Long timestamp = wxResponseHeader.getTimestamp();

        Instant instant = Instant.ofEpochSecond(timestamp);
        // 拒绝5分钟之外的应答
        if (Duration.between(instant, Instant.now()).abs().toMinutes() >= 5) {
            String msg = "微信响应时间戳异常, 请求id: " + wxResponseHeader.getRequestId();
            alarmService.sendAlarm(ALARM_BIZ_TYPE, msg + ", trace: "
                    + LogTraceContext.getTraceCode().orElse(""));
            throw Exceptions.business(msg);
        }

    }

    private boolean isCertificateUrl(Request request) {
        String[] split = request.requestTemplate().url().split("\\?");
        return WxApiConstant.DOWNLOAD_CERTIFICATE_URL.equals(split[0]);
    }

    private boolean isDownLoadBillUrl(Request request) {
        String[] split = request.requestTemplate().url().split("\\?");
        return WxApiConstant.DOWNLOAD_BILL_FILE_URL.equals(split[0]);
    }

}
