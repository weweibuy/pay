package com.weweibuy.pay.wx.client.dto.req;

import com.weweibuy.pay.wx.constant.WxApiConstant;
import com.weweibuy.pay.wx.utils.SignAndVerifySignUtils;
import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import feign.Request;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpMethod;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.SignatureException;

/**
 * 微信支付请求 签名字段
 *
 * @author durenhao
 * @date 2021/10/31 0:10
 **/
@Data
@Builder
public class WxRequestSign {

    /**
     * 请求方法
     */
    private HttpMethod httpMethod;

    /**
     * 请求uri
     * eg:  /v3/certificates?name=11
     */
    private String requestUri;

    /**
     * 时间戳秒
     */
    private Long timestampSecond;

    /**
     * 随机数
     */
    private String nonce;

    /**
     * 请求体
     */
    private String body;


    public static WxRequestSign wxRequestSign(Request request) {
        return WxRequestSign.builder()
                .httpMethod(HttpMethod.valueOf(request.httpMethod().toString()))
                .requestUri(SignAndVerifySignUtils.generateSignUri(request.url()))
                .timestampSecond(DateTimeUtils.currentTimeSeconds())
                .nonce(SignAndVerifySignUtils.generateNonceStr())
                .body(new String(request.body()))
                .build();
    }

    public String toSignature(PrivateKey privateKey) throws SignatureException, InvalidKeyException {
        String str = String.join(WxApiConstant.SIGN_DELIMITER,
                httpMethod + "",
                requestUri,
                timestampSecond + "",
                nonce,
                body) + WxApiConstant.SIGN_DELIMITER;
        return SignAndVerifySignUtils.sign(str, privateKey);
    }


}
