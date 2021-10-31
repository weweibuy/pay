package com.weweibuy.pay.wx.client.dto.resp;

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

        private EncryptCertificate encryptCertificate;

    }

    @Data
    public static class EncryptCertificate {

        private String algorithm;

        private String nonce;

        private String associatedData;

        private String ciphertext;

    }

}
