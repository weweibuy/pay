package com.weweibuy.pay.wx.client.dto.resp;

import lombok.Data;

/**
 * 工作号获取 access_token 响应
 *
 * @author durenhao
 * @date 2021/11/3 23:12
 **/
@Data
public class GzhAccessTokenRespDTO {

    /**
     * 获取到的凭证
     */
    private String accessToken;

    /**
     * 凭证有效时间
     */
    private Integer expiresIn;


}
