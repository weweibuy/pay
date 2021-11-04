package com.weweibuy.pay.wx.client.dto.resp;

import lombok.Data;

/**
 * 用户信息响应
 *
 * @author durenhao
 * @date 2021/11/4 23:13
 **/
@Data
public class UserInfoRespDTO {

    private String openid;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    private Integer sex;

    private String province;

    private String city;

    private String country;

    /**
     * 用户头像
     */
    private String headimgurl;

    /**
     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
     */
    private String unionid;


}
