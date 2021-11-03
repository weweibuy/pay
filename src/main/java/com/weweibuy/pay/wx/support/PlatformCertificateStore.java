package com.weweibuy.pay.wx.support;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.support.AlarmService;
import com.weweibuy.framework.common.log.support.LogTraceContext;
import com.weweibuy.pay.wx.client.DownloadCertificateClient;
import com.weweibuy.pay.wx.client.dto.resp.DownloadCertificateRespDTO;
import com.weweibuy.pay.wx.config.properties.WxAppProperties;
import com.weweibuy.pay.wx.model.constant.AlarmConstant;
import com.weweibuy.pay.wx.model.constant.CertificateCacheConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 平台证书存储
 *
 * @author durenhao
 * @date 2021/10/30 13:27
 **/
@Slf4j
@Component
@RequiredArgsConstructor
@CacheConfig(cacheNames = CertificateCacheConstant.PLATFORM_CERTIFICATE_CACHE_NAME)
public class PlatformCertificateStore {

    private final WxAppProperties wxAppProperties;

    private final ApplicationContext applicationContext;

    private final AlarmService alarmService;

    private final ConcurrentHashMap<String, X509Certificate> certificateMap = new ConcurrentHashMap<>(8);


    @Cacheable(key = "'fetchPlatformCertificate'")
    public Map<String, X509Certificate> fetchPlatformCertificate() {
        DownloadCertificateClient downloadCertificateClient = applicationContext.getBean(DownloadCertificateClient.class);

        DownloadCertificateRespDTO downloadCertificateRespDTO = downloadCertificateClient.downloadCertificate();
        Map<String, X509Certificate> stringX509CertificateMap = CertificatesHelper.platformCertificate(downloadCertificateRespDTO, wxAppProperties);
        if (MapUtils.isEmpty(stringX509CertificateMap)) {
            alarmService.sendAlarm(AlarmService.AlarmLevel.CRITICAL, AlarmConstant.WX_PAY_ALARM_BIZ_TYPE, "无法成功加载平台证书, trace: " + LogTraceContext.getTraceCodeOrEmpty());
            throw Exceptions.business("无法成功加载平台证书");
        }
        return stringX509CertificateMap;
    }


    @CacheEvict(allEntries = true)
    public void evict() {
        log.info("失效平台证书缓存成功");
    }


}
