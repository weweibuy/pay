package com.weweibuy.pay.wx.client.dto.req;

import lombok.Data;

/**
 * 关单请求
 *
 * @author durenhao
 * @date 2021/11/2 22:12
 **/
@Data
public class CloseOrderReqDTO {

    /**
     * 商户id
     */
    private String mchid;

}
