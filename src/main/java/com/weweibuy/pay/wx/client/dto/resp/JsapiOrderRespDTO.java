package com.weweibuy.pay.wx.client.dto.resp;

import lombok.Data;

/**
 * jsapi 下单响应
 *
 * @author durenhao
 * @date 2021/11/2 21:50
 **/
@Data
public class JsapiOrderRespDTO {

    /**
     * 预支付交易会话标识
     * 用于后续接口调用中使用，该值有效期为2小时
     * 示例值：wx201410272009395522657a690389285100
     */
    private String prepayId;


}
