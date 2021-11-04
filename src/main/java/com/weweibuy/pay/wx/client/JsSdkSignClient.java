package com.weweibuy.pay.wx.client;

import com.weweibuy.pay.wx.client.dto.resp.GzhAccessTokenRespDTO;
import com.weweibuy.pay.wx.client.dto.resp.H5JsapiTicketRespDTO;
import com.weweibuy.pay.wx.config.WxFeignSnakeCaseEncoderAndDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * JS SDK 签名接口
 *
 * @author durenhao
 * @date 2021/11/3 23:06
 **/
@FeignClient(name = "jsSdkSignClient", contextId = "jsSdkSignClient",
        url = "https://api.weixin.qq.com/cgi-bin", configuration = WxFeignSnakeCaseEncoderAndDecoder.class)
public interface JsSdkSignClient {


    /**
     * 公众号获取token
     * <p>
     * https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Get_access_token.html
     * <p>
     * 公众号和小程序均可以使用AppID和AppSecret调用本接口来获取access_token。
     * AppID和AppSecret可在“微信公众平台-开发-基本配置”页中获得（需要已经成为开发者，且帐号没有异常状态）。
     * **调用接口时，请登录“微信公众平台-开发-基本配置”提前将服务器IP地址添加到IP白名单中，否则将无法调用成功。
     * **小程序无需配置IP白名单。
     *
     * @param appId
     * @param appSecret
     * @param grantType 获取access_token填写client_credential
     */
    @GetMapping("/token")
    GzhAccessTokenRespDTO gzhAccessToken(@RequestParam("appid") String appId,
                                         @RequestParam("secret") String appSecret,
                                         @RequestParam("grant_type") String grantType);


    /**
     * 获得jsapi_ticket
     * <p>
     * https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/JS-SDK.html#62
     * <p>
     * 用第一步{@link #gzhAccessToken}拿到的access_token 采用http GET方式请求获得jsapi_ticket（有效期7200秒，开发者必须在自己的服务全局缓存jsapi_ticket）：
     * https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi
     *
     * @param accessToken
     * @param type        输入:   jsapi
     */
    @GetMapping("/ticket/getticket")
    H5JsapiTicketRespDTO h5JsapiTicket(@RequestParam("access_token") String accessToken,
                                       @RequestParam("type") String type);


}
