package com.weweibuy.pay.wx.manager;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.support.AlarmService;
import com.weweibuy.framework.common.log.support.LogTraceContext;
import com.weweibuy.pay.wx.model.constant.AlarmConstant;
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

    private final AlarmService alarmService;

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

    public X509Certificate queryPlatformCertificateOrThrow(String serialNumber) {
        X509Certificate x509Certificate = queryPlatformCertificate(serialNumber);
        if (x509Certificate == null) {
            reloadPlatformCertificate();
        }
        x509Certificate = queryPlatformCertificate(serialNumber);
        if (x509Certificate == null) {
            alarmService.sendAlarmFormatMsg(AlarmService.AlarmLevel.CRITICAL, AlarmConstant.WX_PAY_ALARM_BIZ_TYPE,
                    "无法加载获取平台证书: %s, trace: %s",
                    serialNumber,
                    LogTraceContext.getTraceCode().orElse(""));
            throw Exceptions.business("无法查询到平台证书, 编号: " + serialNumber);
        }
        return x509Certificate;
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
