package com.weweibuy.pay.wx.service;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import com.weweibuy.pay.wx.config.properties.WxAppProperties;
import com.weweibuy.pay.wx.model.dto.common.JsapiOrderInfoDTO;
import com.weweibuy.pay.wx.model.dto.common.WxEncryptDataDTO;
import com.weweibuy.pay.wx.model.dto.req.WxJsapiNotifyReqDTO;
import com.weweibuy.pay.wx.utils.WxDecryptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;

/**
 * 微信jsapi 通知服务
 *
 * @author durenhao
 * @date 2021/11/2 23:19
 **/
@Service
@RequiredArgsConstructor
public class WxJsapiNotifyService {

    private final WxAppProperties wxAppProperties;

    /**
     * 微信支付通知
     *
     * @param notifyReq
     */
    public void onPayCallBack(WxJsapiNotifyReqDTO notifyReq) {
        // 解密数据
        WxEncryptDataDTO resource = notifyReq.getResource();
        String decryptData = null;
        try {
            decryptData = WxDecryptUtils.decrypt(resource, wxAppProperties.getApiSecretKey());
        } catch (GeneralSecurityException e) {
            throw Exceptions.business("解密微信通知数据失败");
        }
        // 订单信息
        JsapiOrderInfoDTO orderInfo = JackJsonUtils.readSnakeCaseValue(decryptData, JsapiOrderInfoDTO.class);


    }
}
