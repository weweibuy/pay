package com.weweibuy.pay.wx.model.dto.common;

import com.weweibuy.framework.common.core.validate.annotation.Minimum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 订单金额
 *
 * @author durenhao
 * @date 2021/11/2 22:00
 **/
@Data
public class JsapiOrderAmountDTO {

    /**
     * 订单总金额，单位为分
     */
    @NotNull(message = "订单总金额不能为空")
    @Minimum(value = 0, message = "订单总金额必须为正数")
    private Integer total;

    /**
     * CNY：人民币
     */
    @NotBlank(message = "货币类型不能为空")
    private String currency;

}
