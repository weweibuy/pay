package com.weweibuy.pay.wx.model.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录接口
 *
 * @author durenhao
 * @date 2021/11/4 21:57
 **/
@Data
public class Oath2LoginReqDTO {

    /**
     * 授权码
     */
    @NotBlank(message = "授权码不能为空")
    private String code;
}
