package com.weweibuy.pay.wx.controller;

import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import com.weweibuy.pay.wx.config.properties.WxAppProperties;
import com.weweibuy.pay.wx.model.dto.req.EncryptReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 签名加密
 *
 * @author durenhao
 * @date 2021/11/1 18:03
 **/
@RestController
@RequestMapping("/sign-and-encrypt")
@RequiredArgsConstructor
public class SignAndEncryptController {

    private final WxAppProperties wxAppProperties;


    @GetMapping("/encrypt")
    public CommonDataResponse<String> encrypt(@RequestBody EncryptReqDTO encryptReqDTO) {
        return CommonDataResponse.success("");
    }


}
