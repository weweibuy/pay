package com.weweibuy.pay.wx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author durenhao
 * @date 2021/10/31 21:19
 **/
@RestController
public class DemoController {

    @GetMapping("/demo")
    public String demo() {
        return "success";
    }

}
