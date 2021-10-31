package com.weweibuy.pay.wx.support;

import com.weweibuy.pay.wx.client.DownloadCertificateClient;
import com.weweibuy.pay.wx.client.dto.resp.DownloadCertificateRespDTO;
import com.weweibuy.pay.wx.config.VerifySignFeignFilter;
import com.weweibuy.pay.wx.config.WxAppProperties;
import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.support.AlarmService;
import com.weweibuy.framework.common.log.support.LogTraceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author durenhao
 * @date 2021/10/30 13:27
 **/
@Slf4j
@Component
@RequiredArgsConstructor
@CacheConfig(cacheNames = "platform_certificate_cache")
public class PlatformCertificateStore implements InitializingBean {

    private final WxAppProperties wxAppProperties;

    private final ApplicationContext applicationContext;

    private DownloadCertificateClient downloadCertificateClient;

    private final AlarmService alarmService;

    private final ConcurrentHashMap<String, X509Certificate> certificateMap = new ConcurrentHashMap<>(8);


    @Cacheable(key = "'fetchPlatformCertificate'")
    public Map<String, X509Certificate> fetchPlatformCertificate() {
        DownloadCertificateRespDTO downloadCertificateRespDTO = downloadCertificateClient.downloadCertificate();
        Map<String, X509Certificate> stringX509CertificateMap = CertificatesHelper.platformCertificate(downloadCertificateRespDTO, wxAppProperties);
        if (MapUtils.isEmpty(stringX509CertificateMap)) {
            alarmService.sendAlarm(VerifySignFeignFilter.ALARM_BIZ_TYPE, "无法成功加载平台证书, trace: " + LogTraceContext.getTraceCode().orElse(""));
            throw Exceptions.business("无法成功加载平台证书");
        }
        return stringX509CertificateMap;
    }


    @CacheEvict(allEntries = true)
    public void evict() {
        log.info("失效平台证书缓存成功");
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        downloadCertificateClient = applicationContext.getBean(DownloadCertificateClient.class);
    }


}
