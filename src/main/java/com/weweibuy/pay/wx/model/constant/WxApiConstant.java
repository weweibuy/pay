package com.weweibuy.pay.wx.model.constant;

import org.springframework.http.HttpHeaders;

/**
 * 微信接口常量
 *
 * @author durenhao
 * @date 2021/10/30 22:24
 **/
public interface WxApiConstant {

    /**
     * 微信商户支付 Host
     */
    String WX_MCH_PAY_HOST = "https://api.mch.weixin.qq.com";

    /**
     * 签名方法
     */
    String WX_SIGN_METHOD = "SHA256withRSA";

    /**
     * 商户签名方法
     */
    String CUSTOM_SIGN_SCHEMA = "WECHATPAY2-SHA256-RSA2048";

    /**
     * 微信  Authorization 请求头
     */
    String AUTHORIZATION_HEADER = HttpHeaders.AUTHORIZATION;

    /**
     * 签名分隔符
     */
    String SIGN_DELIMITER = "\n";

    /**
     * 下载证书接口地址
     */
    String DOWNLOAD_CERTIFICATE_URL = "https://api.mch.weixin.qq.com/v3/certificates";

    /**
     * 下载账单wenjai接口
     */
    String DOWNLOAD_BILL_FILE_URL = "/v3/billdownload/file";

    /**
     * 微信响应请求编号
     */
    String WX_RESP_ID = "Request-ID";


    /**
     * 微信签名时间戳 header
     */
    String WX_SIGN_TIMESTAMP_HEADER = "Wechatpay-Timestamp";

    /**
     * 微信签名随机数 header
     */
    String WX_SIGN_NONCE_HEADER = "Wechatpay-Nonce";

    /**
     * 微信签名证书编号 header
     */
    String WX_SIGN_SERIAL_HEADER = "Wechatpay-Serial";


    /**
     * 微信签名 签名 header
     */
    String WX_SIGN_SIGNATURE_HEADER = "Wechatpay-Signature";

    /**
     * 微信通知的支付成功类型，
     * 支付成功通知的类型为TRANSACTION.SUCCESS
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_5.shtml
     */
    String WX_NOTIFY_EVENT_TYPE_SUCCESS = "TRANSACTION.SUCCESS";

    /**
     * 微信通知的资源数据类型，
     * 支付成功通知为encrypt-resource
     */
    String WX_NOTIFY_RESOURCE_TYPE_SUCCESS = "encrypt-resource";



}
