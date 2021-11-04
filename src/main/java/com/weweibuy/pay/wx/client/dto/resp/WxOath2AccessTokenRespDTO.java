package com.weweibuy.pay.wx.client.dto.resp;

import lombok.Data;

/**
 *
 * 获取 access_token 响应
 * @author durenhao
 * @date 2021/11/4 22:56
 **/
@Data
public class WxOath2AccessTokenRespDTO {

    private String accessToken;

    /**
     * 有效时间秒  7200
     */
    private Long expiresIn;

    /**
     * refresh_token有效期为30天
     */
    private String refreshToken;

    private String openid;

    private String scope;

}
