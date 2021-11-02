package com.weweibuy.pay.wx.model.dto.common;

import lombok.Data;

/**
 * 微信以支付 订单金额信息
 *
 * @author durenhao
 * @date 2021/11/2 22:00
 **/
@Data
public class JsapiOrderAmountPayedDTO extends JsapiOrderAmountDTO {

    /**
     * 用户支付金额
     */
    private Integer payerTotal;

    /**
     * 用户支付币种
     */
    private String payerCurrency;

}
