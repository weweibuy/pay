package com.weweibuy.pay.wx.model.vo;

import lombok.Data;

/**
 * token 信息
 *
 * @author durenhao
 * @date 2021/11/4 22:15
 **/
@Data
public class JwtTokenVo {

    private String uid;

    private String openId;


}
