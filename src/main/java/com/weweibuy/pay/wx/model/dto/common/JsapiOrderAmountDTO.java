package com.weweibuy.pay.wx.model.dto.common;

import lombok.Data;

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
    private Integer total;

    /**
     * CNY：人民币
     */
    private String currency;

}
