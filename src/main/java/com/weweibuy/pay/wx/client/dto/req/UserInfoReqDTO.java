package com.weweibuy.pay.wx.client.dto.req;

import lombok.Data;

/**
 * 用户信息请求 获取微信 unionid
 * <p>
 * (需scope为 snsapi_userinfo)
 * <p>
 * https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html#1
 *
 * @author durenhao
 * @date 2021/11/4 23:11
 **/
@Data
public class UserInfoReqDTO {

    /**
     * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     */
    private String access_token;

    private String openid;

    /**
     * 返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
     */
    private String lang = "zh_CN";

}
