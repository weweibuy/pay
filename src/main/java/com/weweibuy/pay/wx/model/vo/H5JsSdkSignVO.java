package com.weweibuy.pay.wx.model.vo;

import lombok.Data;

/**
 * H5 js sdk签名字段
 *
 * @author durenhao
 * @date 2021/11/3 23:26
 **/
@Data
public class H5JsSdkSignVO {

    private String noncestr;

    /**
     * 此处文档要求的是 jsapi_ticket
     */
    private String jsapiTicket;

    private String timestamp;

    private String url;


}
