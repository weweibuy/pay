package com.weweibuy.pay.wx.controller;

import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import com.weweibuy.pay.wx.model.dto.req.Oath2LoginReqDTO;
import com.weweibuy.pay.wx.service.WxOath2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * oath2 接口
 *
 * @author durenhao
 * @date 2021/11/4 21:21
 **/
@RestController
@RequestMapping("/wx/h5/oath2")
@RequiredArgsConstructor
public class WxOath2Controller {

    private final WxOath2Service oath2Service;


    /**
     * 登录接口
     *
     * @return
     */
    @PostMapping("/login")
    public CommonDataResponse<String> login(@RequestBody @Valid Oath2LoginReqDTO loginReq) {
        String token = oath2Service.login(loginReq);
        return CommonDataResponse.success(token);
    }


}
