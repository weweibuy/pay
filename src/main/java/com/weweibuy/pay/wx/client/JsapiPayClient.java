package com.weweibuy.pay.wx.client;

import com.weweibuy.pay.wx.client.dto.req.CloseOrderReqDTO;
import com.weweibuy.pay.wx.client.dto.req.JsapiOrderReqDTO;
import com.weweibuy.pay.wx.client.dto.resp.JsapiOrderRespDTO;
import com.weweibuy.pay.wx.model.dto.common.JsapiOrderInfoDTO;
import com.weweibuy.pay.wx.config.WxFeignSnakeCaseEncoderAndDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Jsapi 支付接口
 *
 * @author durenhao
 * @date 2021/11/2 21:39
 **/
@FeignClient(name = "jsapiPayClient", contextId = "jsapiPayClient",
        url = "https://api.mch.weixin.qq.com", configuration = WxFeignSnakeCaseEncoderAndDecoder.class)
public interface JsapiPayClient {


    /**
     * 下单接口
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_1.shtml
     */
    @PostMapping("/v3/pay/transactions/jsapi")
    JsapiOrderRespDTO order(JsapiOrderReqDTO orderReq);

    /**
     * 微信单号查询订单接口
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_2.shtml
     *
     * @param transactionId 微信支付订单号
     * @param mchId         直连商户号
     */
    @GetMapping("/v3/pay/transactions/id/{transactionId}")
    JsapiOrderInfoDTO queryOrderByTransactionId(@PathVariable("transactionId") String transactionId,
                                                @RequestParam("mchid") String mchId);

    /**
     * 商户订单号查询订单
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_2.shtml
     *
     * @param outTradeNo
     * @param mchId
     */
    @GetMapping("/v3/pay/transactions/out-trade-no/{outTradeNo}")
    JsapiOrderInfoDTO queryOrderByOutTradeNo(@PathVariable("outTradeNo") String outTradeNo,
                                             @RequestParam("mchid") String mchId);


    /**
     * 关单接口
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_3.shtml
     *
     * @param outTradeNo
     * @param closeOrderReq
     */
    @PostMapping("/v3/pay/transactions/out-trade-no/{outTradeNo}/close")
    void closeOrder(@PathVariable("outTradeNo") String outTradeNo,
                    CloseOrderReqDTO closeOrderReq);


}
