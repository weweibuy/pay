package com.weweibuy.pay.wx.model.eum;

/**
 * 微信订单交易类型
 * <p>
 * 交易类型，枚举值：
 * JSAPI：公众号支付
 * NATIVE：扫码支付
 * APP：APP支付
 * MICROPAY：付款码支付
 * MWEB：H5支付
 * FACEPAY：刷脸支付
 *
 * @author durenhao
 * @date 2021/11/2 22:05
 **/
public enum WxOrderTradeType {

    JSAPI,

    NATIVE,

    APP,

    MICROPAY,

    MWEB,

    FACEPAY,;


}
