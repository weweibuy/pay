package com.weweibuy.pay.wx.service;

import com.weweibuy.pay.wx.model.dto.req.Oath2LoginReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author durenhao
 * @date 2021/11/4 22:42
 **/
@Service
@RequiredArgsConstructor
public class WxOath2Service {

    /**
     * 登录
     *
     * @param loginReq
     * @return
     */
    public String login(Oath2LoginReqDTO loginReq) {
        return "";
    }
}
