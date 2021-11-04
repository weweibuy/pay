package com.weweibuy.pay.wx.client.dto.req;

import lombok.Data;

/**
 *
 * 刷新 token请求
 * @author durenhao
 * @date 2021/11/4 23:07
 **/
@Data
public class Oath2RefreshTokenReqDTO {

    /**
     * 公众号的唯一标识
     */
    private String appid;

    /**
     * 填写为refresh_token
     */
    private String grant_type = "refresh_token";

    /**
     * 填写通过access_token获取到的refresh_token参数
     */
    private String refresh_token;




}
