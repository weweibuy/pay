package com.weweibuy.pay.wx.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.pay.wx.client.dto.resp.WxResponseHeader;
import com.weweibuy.pay.wx.config.WxFeignSnakeCaseEncoderAndDecoder;
import com.weweibuy.pay.wx.config.properties.WxAppProperties;
import com.weweibuy.pay.wx.manager.PlatformCertificateManager;
import com.weweibuy.pay.wx.model.dto.common.JsapiOrderInfoDTO;
import com.weweibuy.pay.wx.model.dto.common.WxEncryptDataDTO;
import com.weweibuy.pay.wx.utils.SignAndVerifySignUtils;
import com.weweibuy.pay.wx.utils.WxDecryptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * @author durenhao
 * @date 2021/11/2 22:54
 **/
@Component
@RequiredArgsConstructor
public class JsapiNotifyHelper {

    private final WxAppProperties wxAppProperties;

    private final WxSignHelper wxSignHelper;

    private final PlatformCertificateManager platformCertificateManager;

    /**
     * 解密 通知请求
     *
     * @param encryptDataDTO
     * @return
     */
    public JsapiOrderInfoDTO decryptReqContent(WxEncryptDataDTO encryptDataDTO) {
        String data = null;
        try {
            data = WxDecryptUtils.decrypt(encryptDataDTO, wxAppProperties.getApiSecretKey());
        } catch (GeneralSecurityException e) {
            throw Exceptions.business("解密微信Jsapi通知密文失败");
        }
        ObjectMapper wxObjectMapper = WxFeignSnakeCaseEncoderAndDecoder.getWxObjectMapper();
        try {
            return wxObjectMapper.readValue(data, JsapiOrderInfoDTO.class);
        } catch (JsonProcessingException e) {
            throw Exceptions.business("微信Jsapi通知订单数据转化异常", e);
        }
    }


    public void verifySign(Map<String, String> header, HttpServletRequest request) throws InvalidKeyException {
        WxResponseHeader wxResponseHeader = WxResponseHeader.fromHeader(header);
        wxSignHelper.verifyResponseHeader(wxResponseHeader);
        String body = HttpRequestUtils.readRequestBodyForJson(request, true);

        X509Certificate x509Certificate = platformCertificateManager.queryPlatformCertificateOrThrow(wxResponseHeader.getSerial());

        try {
            SignAndVerifySignUtils.verifySign(wxResponseHeader, body, x509Certificate);
        } catch (SignatureException e) {
            throw Exceptions.business("微信通知请求签名错误");
        }
    }
}
