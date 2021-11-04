package com.weweibuy.pay.wx.client;

import com.weweibuy.pay.wx.client.dto.req.Oath2AccessTokenReqDTO;
import com.weweibuy.pay.wx.client.dto.req.Oath2RefreshTokenReqDTO;
import com.weweibuy.pay.wx.client.dto.req.UserInfoReqDTO;
import com.weweibuy.pay.wx.client.dto.resp.UserInfoRespDTO;
import com.weweibuy.pay.wx.client.dto.resp.WxOath2AccessTokenRespDTO;
import com.weweibuy.pay.wx.config.WxFeignSnakeCaseEncoderAndDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * oath2 接口
 * https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html#1
 *
 * @author durenhao
 * @date 2021/11/4 22:51
 **/
@FeignClient(name = "wxOath2Client", contextId = "wxOath2Client",
        url = "https://api.weixin.qq.com/sns", configuration = WxFeignSnakeCaseEncoderAndDecoder.class)
public interface WxOath2Client {

    /**
     * 获取 access_token
     * <p>
     * https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html#1
     *
     * @param accessTokenReq
     */
    @GetMapping("/oauth2/access_token")
    WxOath2AccessTokenRespDTO oath2AccessToken(@SpringQueryMap Oath2AccessTokenReqDTO accessTokenReq);

    /**
     * 刷新token
     *
     * @param refreshTokenReqDTO
     * @return
     */
    @GetMapping("/oauth2/refresh_token")
    WxOath2AccessTokenRespDTO oath2RefreshToken(@SpringQueryMap Oath2RefreshTokenReqDTO refreshTokenReqDTO);


    /**
     * 拉取用户信息(需scope为 snsapi_userinfo)
     *
     * @param userInfoReqDTO
     * @return
     */
    @GetMapping("/userinfo")
    UserInfoRespDTO snsApiUserInfo(@SpringQueryMap UserInfoReqDTO userInfoReqDTO);

}
