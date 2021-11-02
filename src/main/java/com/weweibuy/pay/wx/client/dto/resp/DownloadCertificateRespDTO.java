package com.weweibuy.pay.wx.client.dto.resp;

import com.weweibuy.pay.wx.model.dto.common.WxEncryptDataDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 平台证书
 * https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay5_1.shtml
 *
 *
 * @author durenhao
 * @date 2021/10/30 11:22
 **/
@Data
public class DownloadCertificateRespDTO {

    private List<Certificate> data;

    @Data
    public static class Certificate {

        private String serialNo;

        private LocalDateTime effectiveTime;

        private LocalDateTime expireTime;

        private WxEncryptDataDTO encryptCertificate;

    }


}
