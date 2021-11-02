package com.weweibuy.pay.wx.support;

import com.weweibuy.pay.wx.client.dto.resp.DownloadCertificateRespDTO;
import com.weweibuy.pay.wx.config.properties.WxAppProperties;
import com.weweibuy.pay.wx.model.dto.common.WxEncryptDataDTO;
import com.weweibuy.pay.wx.model.vo.SerialNoCertificate;
import com.weweibuy.pay.wx.utils.WxDecryptUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 平台证书工具类
 *
 * @author durenhao
 * @date 2021/10/30 14:12
 **/
@Slf4j
public class CertificatesHelper {


    /**
     * 平台证书
     *
     * @param wxEncryptDataDTO
     * @param secretKey
     * @return
     */
    public static X509Certificate platformCertificate(WxEncryptDataDTO wxEncryptDataDTO,
                                                      SecretKey secretKey) {

        X509Certificate x509Cert = null;
        try {
            String cert = WxDecryptUtils.decrypt(wxEncryptDataDTO, secretKey);
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            x509Cert = (X509Certificate) cf.generateCertificate(
                    new ByteArrayInputStream(cert.getBytes("utf-8")));
        } catch (Exception e) {
            log.error("解密平台证书异常: ", e);
            return null;
        }
        try {
            x509Cert.checkValidity();
        } catch (CertificateExpiredException | CertificateNotYetValidException e) {
            log.warn("无效的平台证书: {}", wxEncryptDataDTO);
            return null;
        }
        return x509Cert;
    }


    public static SerialNoCertificate platformCertificate(DownloadCertificateRespDTO.Certificate certificate, WxAppProperties wxAppProperties) {
        X509Certificate x509Certificate = CertificatesHelper.platformCertificate(certificate.getEncryptCertificate(), wxAppProperties.getApiSecretKey());
        return SerialNoCertificate.builder()
                .serialNo(certificate.getSerialNo())
                .certificate(x509Certificate)
                .build();
    }


    public static Map<String, X509Certificate> platformCertificate(DownloadCertificateRespDTO downloadCertificateRespDTO, WxAppProperties wxAppProperties) {
        return Optional.ofNullable(downloadCertificateRespDTO)
                .map(DownloadCertificateRespDTO::getData)
                .filter(CollectionUtils::isNotEmpty)
                .map(list -> list.stream()
                        .filter(c -> c.getEffectiveTime().compareTo(LocalDateTime.now()) < 0)
                        .filter(c -> c.getExpireTime().compareTo(LocalDateTime.now().plusHours(12)) < 0)
                        .map(c -> platformCertificate(c, wxAppProperties))
                        .filter(s -> s.getCertificate() != null)
                        .collect(Collectors.toMap(SerialNoCertificate::getSerialNo, SerialNoCertificate::getCertificate, (o, n) -> n)))
                .orElse(Collections.emptyMap());
    }


}
