package com.weweibuy.pay.wx.client.dto.req;

import com.weweibuy.pay.wx.config.WxAppProperties;
import lombok.Builder;
import lombok.Data;

import java.security.InvalidKeyException;
import java.security.SignatureException;

/**
 * 微信支付 签名请求头
 *
 * @author durenhao
 * @date 2021/10/30 23:24
 **/
@Data
@Builder
public class WxRequestAuthorizationHeader {

    /**
     * 商户号
     */
    private String mchid;

    /**
     * 随机数
     */
    private String nonce_str;

    /**
     * 签名
     */
    private String signature;

    /**
     * 时间戳 单位秒
     */
    private Long timestamp;

    /**
     * 证书编号
     */
    private String serial_no;


    public static WxRequestAuthorizationHeader authorizationHeader(WxRequestSign requestSign, WxAppProperties wxAppProperties) throws SignatureException, InvalidKeyException {
        WxAppProperties.SerialNoPrivateKey serialNoPrivateKey = wxAppProperties.getPrivateKey().get(0);
        return WxRequestAuthorizationHeader.builder()
                .mchid(wxAppProperties.getMerchantId())
                .nonce_str(requestSign.getNonce())
                // 签名
                .signature(requestSign.toSignature(serialNoPrivateKey.getPrivateKey()))
                .timestamp(requestSign.getTimestampSecond())
                .serial_no(serialNoPrivateKey.getSerialNo())
                .build();
    }

}
