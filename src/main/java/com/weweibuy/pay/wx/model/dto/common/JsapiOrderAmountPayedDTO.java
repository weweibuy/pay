package com.weweibuy.pay.wx.model.dto.common;

import com.weweibuy.framework.common.core.validate.annotation.Minimum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @NotNull(message = "用户支付金额不能为空")
    @Minimum(value = 0, message = "用户支付必须为正数")
    private Integer payerTotal;

    /**
     * 用户支付币种
     */
    @NotBlank(message = "用户支付币种不能为空")
    private String payerCurrency;

}
