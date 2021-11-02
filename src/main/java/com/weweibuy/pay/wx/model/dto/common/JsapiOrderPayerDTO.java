package com.weweibuy.pay.wx.model.dto.common;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 订单支付者信息
 *
 * @author durenhao
 * @date 2021/11/2 22:01
 **/
@Data
public class JsapiOrderPayerDTO {

    @NotBlank(message = "用户标识openid不能为空")
    private String openid;

}
