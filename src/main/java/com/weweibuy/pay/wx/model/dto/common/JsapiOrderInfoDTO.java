package com.weweibuy.pay.wx.model.dto.common;

import com.weweibuy.pay.wx.model.eum.WxOrderTradeState;
import com.weweibuy.pay.wx.model.eum.WxOrderTradeType;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 微信订单信息
 *
 * @author durenhao
 * @date 2021/11/2 21:56
 **/
@Data
public class JsapiOrderInfoDTO {

    @NotBlank(message = "应用ID不能为空")
    private String appid;

    /**
     * 商户号
     */
    @NotBlank(message = "商户号不能为空")
    private String mchid;

    /**
     * 商户订单号
     */
    @NotBlank(message = "商户订单号不能为空")
    private String outTradeNo;

    /**
     * 微信支付订单号
     */
    @NotBlank(message = "微信支付订单号不能为空")
    private String transactionId;

    /**
     * 交易类型
     */
    @NotBlank(message = "交易类型不能为空")
    private WxOrderTradeType tradeType;

    /***
     * 交易状态
     */
    @NotBlank(message = "交易状态不能为空")
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
    @NotNull(message = "支付完成时间不能为空")
    private LocalDateTime successTime;

    /**
     * 支付者
     */
    @NotNull(message = "支付者不能为空")
    @Valid
    private JsapiOrderPayerDTO payer;

    /**
     * 订单金额信息，当支付成功时返回该字段
     */
    @Valid
    @NotNull(message = "订单金额不能为空")
    private JsapiOrderAmountPayedDTO amount;


}
