package com.weweibuy.pay.wx.model.dto.resp;

import lombok.Data;

/**
 * 微信 jsapi 通知响应
 *
 * @author durenhao
 * @date 2021/11/2 23:03
 **/
@Data
public class WxJsapiNotifyRespDTO {

    private String code;

    private String message;


    public static WxJsapiNotifyRespDTO success(){
        WxJsapiNotifyRespDTO wxJsapiNotifyRespDTO = new WxJsapiNotifyRespDTO();
        wxJsapiNotifyRespDTO.setCode("SUCCESS");
        return wxJsapiNotifyRespDTO;
    }

}
