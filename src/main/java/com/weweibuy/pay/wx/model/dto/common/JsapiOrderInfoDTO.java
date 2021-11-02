package com.weweibuy.pay.wx.model.dto.common;

import com.weweibuy.pay.wx.model.eum.WxOrderTradeState;
import com.weweibuy.pay.wx.model.eum.WxOrderTradeType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 微信订单信息
 *
 * @author durenhao
 * @date 2021/11/2 21:56
 **/
@Data
public class JsapiOrderInfoDTO {

    private String appid;

    /**
     * 商户号
     */
    private String mchid;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 交易类型
     */
    private WxOrderTradeType tradeType;

    /***
     * 交易状态
     */
    private WxOrderTradeState tradeState;

    /**
     * 交易状态描述
     */
    private String tradeStateDesc;

    /**
     * 付款银行
     * https://pay.weixin.qq.com/wiki/doc/apiv3/terms_definition/chapter1_1_3.shtml#part-6
     */
    private String bankType;

    /**
     * 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用
     */
    private String attach;

    /**
     * 支付完成时间
     */
    private LocalDateTime successTime;

    /**
     * 支付者
     */
    private JsapiOrderPayerDTO payer;

    /**
     * 订单金额信息，当支付成功时返回该字段
     */
    private JsapiOrderAmountPayedDTO amount;


}
