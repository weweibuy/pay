package com.weweibuy.pay.wx.config;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.support.AlarmService;
import com.weweibuy.framework.common.feign.support.FeignFilter;
import com.weweibuy.framework.common.feign.support.FeignFilterChain;
import com.weweibuy.pay.wx.client.dto.resp.DownloadCertificateRespDTO;
import com.weweibuy.pay.wx.client.dto.resp.WxResponseHeader;
import com.weweibuy.pay.wx.config.properties.WxAppProperties;
import com.weweibuy.pay.wx.manager.PlatformCertificateManager;
import com.weweibuy.pay.wx.model.constant.AlarmConstant;
import com.weweibuy.pay.wx.model.constant.WxApiConstant;
import com.weweibuy.pay.wx.model.event.BizAlarmEvent;
import com.weweibuy.pay.wx.support.CertificatesHelper;
import com.weweibuy.pay.wx.support.WxSignHelper;
import com.weweibuy.pay.wx.utils.SignAndVerifySignUtils;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
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

    private final ApplicationContext applicationContext;

    private final Validator validator;

    private final WxSignHelper wxSignHelper;

    /**
     * 是否为 mock 环境
     */
    private boolean isMock = false;

    @Override
    public Response filter(Request request, Request.Options options, FeignFilterChain chain) throws IOException {
        if (!isReqWx(request)) {
            return chain.doFilter(request, options);
        }
        // 增加签名请求头
        request = addSingHeader(request);
        Response response = chain.doFilter(request, options);
        return verifySign(response, request);
    }


    private boolean isReqWx(Request request) {
        String url = request.url();
        return url.startsWith(WxApiConstant.WX_MCH_PAY_HOST);
    }

    private Request addSingHeader(Request request) {
        String authorization = null;
        try {
            authorization = SignAndVerifySignUtils.authorization(request, wxAppProperties);
        } catch (SignatureException e) {
            applicationContext.publishEvent(BizAlarmEvent.builder()
                    .alarmLevel(AlarmService.AlarmLevel.WARN)
                    .bizType(AlarmConstant.WX_PAY_ALARM_BIZ_TYPE)
                    .msg(String.format("请求微信: %s %s 生成签名失败: %s",
                            request.httpMethod(),
                            request.url(),
                            e.getMessage()))
                    .build());
            throw Exceptions.business("请求微信签名失败", e);
        } catch (InvalidKeyException e) {
            applicationContext.publishEvent(BizAlarmEvent.builder()
                    .alarmLevel(AlarmService.AlarmLevel.CRITICAL)
                    .bizType(AlarmConstant.WX_PAY_ALARM_BIZ_TYPE)
                    .msg(String.format("请求微信: 商户签名证书无效: %s",
                            e.getMessage()))
                    .build());
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
            applicationContext.publishEvent(BizAlarmEvent.builder()
                    .alarmLevel(AlarmService.AlarmLevel.CRITICAL)
                    .bizType(AlarmConstant.WX_PAY_ALARM_BIZ_TYPE)
                    .msg(String.format("请求微信: %s %s, 签名错误",
                            request.httpMethod(),
                            request.url()))
                    .build());
            return response;
        }

        if (status < 200 || status >= 300) {
            return response;
        }

        // 响应头
        WxResponseHeader wxResponseHeader = WxResponseHeader.fromWxResponse(response);
        wxSignHelper.verifyResponseHeader(wxResponseHeader);

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

        DownloadCertificateRespDTO downloadCertificateRespDTO = WxFeignSnakeCaseEncoderAndDecoder.getWxObjectMapper().readValue(bodyStr, DownloadCertificateRespDTO.class);

        Map<String, X509Certificate> stringX509CertificateMap = CertificatesHelper.platformCertificate(downloadCertificateRespDTO, wxAppProperties);
        X509Certificate x509Certificate = stringX509CertificateMap.get(wxResponseHeader.getSerial());
        if (x509Certificate == null) {
            applicationContext.publishEvent(BizAlarmEvent.builder()
                    .alarmLevel(AlarmService.AlarmLevel.CRITICAL)
                    .bizType(AlarmConstant.WX_PAY_ALARM_BIZ_TYPE)
                    .msg("下载平台证书时, 无法找到平台签名证书")
                    .build());
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
                applicationContext.publishEvent(BizAlarmEvent.builder()
                        .alarmLevel(AlarmService.AlarmLevel.WARN)
                        .bizType(AlarmConstant.WX_PAY_ALARM_BIZ_TYPE)
                        .msg(String.format("请求微信: %s %s , 响应验签, 验签失败",
                                request.httpMethod(),
                                request.url()))
                        .build());
                throw Exceptions.business("微信响应验签,验签失败");
            }
        } catch (SignatureException e) {
            applicationContext.publishEvent(BizAlarmEvent.builder()
                    .alarmLevel(AlarmService.AlarmLevel.WARN)
                    .bizType(AlarmConstant.WX_PAY_ALARM_BIZ_TYPE)
                    .msg(String.format("请求微信: %s %s 响应验签, 生成签名失败: %s",
                            request.httpMethod(),
                            request.url(),
                            e.getMessage()))
                    .build());
            throw Exceptions.business("微信响应验签, 生成签名失败", e);
        } catch (InvalidKeyException e) {
            applicationContext.publishEvent(BizAlarmEvent.builder()
                    .alarmLevel(AlarmService.AlarmLevel.CRITICAL)
                    .bizType(AlarmConstant.WX_PAY_ALARM_BIZ_TYPE)
                    .msg(String.format("请求微信: %s %s , 响应验签, 微信平台证书无效: %s",
                            request.httpMethod(),
                            request.url(),
                            e.getMessage()))
                    .build());
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
            applicationContext.publishEvent(BizAlarmEvent.builder()
                    .alarmLevel(AlarmService.AlarmLevel.CRITICAL)
                    .bizType(AlarmConstant.WX_PAY_ALARM_BIZ_TYPE)
                    .msg(String.format("无法加载获取平台证书: %s",
                            wxResponseHeader.getSerial()))
                    .build());
            throw Exceptions.business("无法加载获取平台证书: " + wxResponseHeader.getSerial());
        }

        return x509Certificate;
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
