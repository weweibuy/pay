package com.weweibuy.pay.wx.manager;

import com.weweibuy.pay.wx.support.PlatformCertificateStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * 平台证书管理
 *
 * @author durenhao
 * @date 2021/10/31 13:56
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class PlatformCertificateManager {

    private final PlatformCertificateStore platformCertificateStore;

    /**
     * 获取平台证书
     *
     * @param serialNumber
     * @return
     */
    public X509Certificate queryPlatformCertificate(String serialNumber) {
        Map<String, X509Certificate> x509CertificateMap = platformCertificateStore.fetchPlatformCertificate();
        return x509CertificateMap.get(serialNumber);
    }

    /**
     * 重新加载平台证书
     */
    public void reloadPlatformCertificate() {
        platformCertificateStore.evict();
        platformCertificateStore.fetchPlatformCertificate();
        log.info("加载平台证书成功");
    }


}
