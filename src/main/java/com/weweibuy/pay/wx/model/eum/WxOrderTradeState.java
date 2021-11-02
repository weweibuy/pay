package com.weweibuy.pay.wx.model.eum;

/**
 * 交易状态
 * SUCCESS：支付成功
 * REFUND：转入退款
 * NOTPAY：未支付
 * CLOSED：已关闭
 * REVOKED：已撤销（仅付款码支付会返回）
 * USERPAYING：用户支付中（仅付款码支付会返回）
 * PAYERROR：支付失败（仅付款码支付会返回）
 *
 * @author durenhao
 * @date 2021/11/2 22:07
 **/
public enum WxOrderTradeState {

    SUCCESS,

    REFUND,

    NOTPAY,

    CLOSED,

    REVOKED,

    USERPAYING,

    PAYERROR,
    ;


}
