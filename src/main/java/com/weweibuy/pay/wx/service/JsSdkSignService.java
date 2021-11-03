package com.weweibuy.pay.wx.service;

import com.weweibuy.pay.wx.model.dto.resp.WxJsSdkSignRespDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * jsAPI 签名服务
 *
 * @author durenhao
 * @date 2021/11/3 22:20
 **/
@Service
@RequiredArgsConstructor
public class JsSdkSignService {

    /**
     * 生成JS SDK 签名
     *
     * @param url
     * @return
     */
    public WxJsSdkSignRespDTO generateSign(String url) {
        // 查询
        return null;
    }
}
