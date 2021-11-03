package com.weweibuy.pay.wx.client.dto.resp;

import lombok.Data;

/**
 * @author durenhao
 * @date 2021/11/3 23:21
 **/
@Data
public class H5JsapiTicketRespDTO {

    private String errcode;

    private String errmsg;

    private String ticket;

    private Integer expiresIn;


}
