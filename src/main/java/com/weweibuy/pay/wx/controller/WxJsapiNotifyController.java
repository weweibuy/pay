package com.weweibuy.pay.wx.controller;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.pay.wx.model.dto.req.WxJsapiNotifyReqDTO;
import com.weweibuy.pay.wx.model.dto.resp.WxJsapiNotifyRespDTO;
import com.weweibuy.pay.wx.service.WxJsapiNotifyService;
import com.weweibuy.pay.wx.support.JsapiNotifyHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.InvalidKeyException;
import java.util.Map;

/**
 * Wx jsapi 支付通知接口
 *
 * @author durenhao
 * @date 2021/11/2 22:14
 **/
@RestController
@RequestMapping("/wx/pay/jsapi")
@RequiredArgsConstructor
public class WxJsapiNotifyController {

    private final JsapiNotifyHelper jsapiNotifyHelper;

    private final WxJsapiNotifyService jsapiNotifyService;

    /**
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_5.shtml
     * Jsapi 支付通知接口
     *
     * @param notifyReq
     * @return
     */
    @PostMapping("/notify")
    public WxJsapiNotifyRespDTO payCallBack(@RequestHeader Map<String, String> header,
                                            @RequestBody @Valid WxJsapiNotifyReqDTO notifyReq,
                                            HttpServletRequest request) throws InvalidKeyException {
        if (MapUtils.isEmpty(header)) {
            throw Exceptions.business("无签名数据");
        }
        // 签名验证
        jsapiNotifyHelper.verifySign(header, request);
        jsapiNotifyService.onPayCallBack(notifyReq);
        return WxJsapiNotifyRespDTO.success();
    }

}
