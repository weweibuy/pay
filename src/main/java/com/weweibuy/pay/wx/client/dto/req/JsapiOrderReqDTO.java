package com.weweibuy.pay.wx.client.dto.req;

import com.weweibuy.pay.wx.model.dto.common.JsapiOrderAmountDTO;
import com.weweibuy.pay.wx.model.dto.common.JsapiOrderPayerDTO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * JSAPI 下单请求
 * <p>
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_1.shtml
 *
 * @author durenhao
 * @date 2021/11/2 21:41
 **/
@Data
public class JsapiOrderReqDTO {

    private String appid;

    /**
     * 商户号
     */
    private String mchid;

    /**
     * 商品描述
     * 示例值：Image形象店-深圳腾大-QQ公仔
     */
    private String description;

    /**
     * 商户订单号
     * 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一
     * 示例值：1217752501201407033233368018
     */
    private String outTradeNo;

    /**
     * 交易结束时间
     * 非必须
     */
    private LocalDateTime timeExpire;

    /**
     * 附加数据
     * 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用
     * 非必须
     */
    private String attach;

    /**
     * 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
     * 公网域名必须为https，如果是走专线接入，使用专线NAT IP或者私有回调域名可使用http
     */
    private String notifyUrl;

    /**
     * 订单金额
     */
    private JsapiOrderAmountDTO amount;

    /**
     * 支付者
     */
    private JsapiOrderPayerDTO payer;




}
