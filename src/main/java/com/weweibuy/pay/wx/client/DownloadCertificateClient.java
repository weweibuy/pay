package com.weweibuy.pay.wx.client;

import com.weweibuy.pay.wx.client.dto.resp.DownloadCertificateRespDTO;
import com.weweibuy.pay.wx.constant.WxApiConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 下载平台证书
 * https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay5_1.shtml
 *
 * @author durenhao
 * @date 2021/10/30 11:14
 **/
@FeignClient(name = "downloadCertificateClient", contextId = "downloadCertificateClient", url = WxApiConstant.DOWNLOAD_CERTIFICATE_URL)
public interface DownloadCertificateClient {


    /**
     * 下载平台证书
     *
     * @return
     */
    @GetMapping
    DownloadCertificateRespDTO downloadCertificate();

}
