package com.weweibuy.pay.wx.client.dto.req;

import lombok.Data;

/**
 * 通过code换取网页授权access_token 请求
 *
 * @author durenhao
 * @date 2021/11/4 22:53
 **/
@Data
public class Oath2AccessTokenReqDTO {

    /**
     * 公众号的唯一标识
     */
    private String appid;

    /**
     * 公众号的appsecret
     */
    private String secret;

    /**
     * 微信的授权码
     * https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html#1
     */
    private String code;

    /**
     * 填写为authorization_code
     */
    private String grant_type = "authorization_code";


}
