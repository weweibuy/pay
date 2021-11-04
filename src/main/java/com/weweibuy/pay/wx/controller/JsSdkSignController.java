package com.weweibuy.pay.wx.controller;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import com.weweibuy.pay.wx.model.dto.resp.WxJsSdkSignRespDTO;
import com.weweibuy.pay.wx.service.JsSdkSignService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JS-SDK 签名获取接口
 *
 * @author durenhao
 * @date 2021/11/3 21:55
 **/
@RestController
@RequestMapping("/wx/h5/js-sdk")
@RequiredArgsConstructor
public class JsSdkSignController {

    private final JsSdkSignService jsSdkSignService;

    /**
     * 生成签名
     *
     * @return
     */
    @GetMapping("/sign")
    public CommonDataResponse<WxJsSdkSignRespDTO> generateSign(String url) {
        if (StringUtils.isBlank(url)) {
            throw Exceptions.business("请输入调用JS接口页面的完整URL");
        }
        return CommonDataResponse.success(jsSdkSignService.generateSign(url));
    }


}
