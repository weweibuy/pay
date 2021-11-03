package com.weweibuy.pay.wx.model.dto.resp;

import lombok.Data;

/**
 * JsSDK 签名信息生成
 * <p>
 * https://qydev.weixin.qq.com/wiki/index.php?title=%E5%BE%AE%E4%BF%A1JS-SDK%E6%8E%A5%E5%8F%A3
 *
 * @author durenhao
 * @date 2021/11/3 21:58
 **/
@Data
public class WxJsSdkSignRespDTO {

    /**
     * 公众号的唯一标识
     */
    private String appId;

    /**
     * 生成签名的时间戳
     */
    private Long timestamp;

    /**
     * 生成签名的随机串
     */
    private String nonceStr;

    /**
     * 签名
     */
    private String signature;


}
