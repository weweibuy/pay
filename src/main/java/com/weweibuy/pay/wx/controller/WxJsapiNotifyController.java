package com.weweibuy.pay.wx.controller;

import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.pay.wx.model.dto.req.WxJsapiNotifyReqDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Wx jsapi 支付通知接口
 *
 * @author durenhao
 * @date 2021/11/2 22:14
 **/
@RestController
public class WxJsapiNotifyController {


    /**
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_5.shtml
     * Jsapi 支付通知接口
     *
     * @param notifyReq
     * @return
     */
    @PostMapping("/wx/pay/notify")
    public CommonCodeResponse payCallBack(@RequestBody WxJsapiNotifyReqDTO notifyReq) {
        return CommonCodeResponse.success();
    }

}
