package com.weweibuy.pay.wx.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.pay.wx.client.dto.resp.WxResponseHeader;
import com.weweibuy.pay.wx.config.WxFeignSnakeCaseEncoderAndDecoder;
import com.weweibuy.pay.wx.config.properties.WxAppProperties;
import com.weweibuy.pay.wx.model.dto.common.JsapiOrderInfoDTO;
import com.weweibuy.pay.wx.model.dto.common.WxEncryptDataDTO;
import com.weweibuy.pay.wx.utils.WxDecryptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;
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


    public void verifySign(Map<String, String> header) {
        WxResponseHeader wxResponseHeader = WxResponseHeader.fromHeader(header);
        wxSignHelper.verifyResponseHeader(wxResponseHeader);
        // TODO
//        SignAndVerifySignUtils.verifySign(wxResponseHeader, )
    }
}
