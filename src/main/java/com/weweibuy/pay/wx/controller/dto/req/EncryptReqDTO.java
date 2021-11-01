package com.weweibuy.pay.wx.controller.dto.req;

import lombok.Data;

/**
 * @author durenhao
 * @date 2021/11/1 18:18
 **/
@Data
public class EncryptReqDTO {

    private String nonce;

    private String associatedData;

    private String text;
}
