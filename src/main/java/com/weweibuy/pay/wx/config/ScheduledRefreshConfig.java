package com.weweibuy.pay.wx.config;

import com.weweibuy.framework.common.core.concurrent.LogExceptionThreadFactory;
import com.weweibuy.framework.common.core.support.AlarmService;
import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import com.weweibuy.pay.wx.config.properties.WxAppProperties;
import com.weweibuy.pay.wx.manager.PlatformCertificateManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 定时刷新 配置/缓存 配置
 *
 * @author durenhao
 * @date 2021/10/31 20:17
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledRefreshConfig implements InitializingBean {

    private final PlatformCertificateManager platformCertificateManager;

    private final WxAppProperties wxAppProperties;

    private final AlarmService alarmService;

    private ScheduledExecutorService schedule = new ScheduledThreadPoolExecutor(
            1,
            new LogExceptionThreadFactory("refresh-schedule-"),
            new ThreadPoolExecutor.DiscardPolicy());

    @Override
    public void afterPropertiesSet() throws Exception {
        // 每天凌晨 00:10 刷新平台证书
        schedule.scheduleAtFixedRate(() -> checkCertificate(),
                delay(), 24 * 60, TimeUnit.MINUTES);
    }

    private long delay() {
        LocalDate localDate = LocalDate.now().plusDays(1);
        LocalTime localTime = LocalTime.of(0, 10);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        Duration between = DateTimeUtils.between(localDateTime, LocalDateTime.now());
        return between.toMinutes();
    }

    private void reloadPlatformCertificate() {
        try {
            platformCertificateManager.reloadPlatformCertificate();
        } catch (Exception e) {
            log.error("重新加载平台证书异常:", e);
        }
    }

    public void checkCertificate() {
        log.info("检查证书任务启动");
        reloadPlatformCertificate();
        checkMerchantCertificate();
        log.info("检查证书任务完成");
    }

    /**
     * 检查商户证书
     */
    private void checkMerchantCertificate() {
        try {
            List<WxAppProperties.SerialNoPrivateKeyInfo> privateKeyInfo = wxAppProperties.getPrivateKeyInfo();
            privateKeyInfo.forEach(i -> {
                String expireTime = i.getExpireTime();
                LocalDateTime localDateTime = DateTimeUtils.stringToLocalDateTime(expireTime);
                Duration between = DateTimeUtils.between(LocalDateTime.now(), localDateTime);
                long day = between.toDays();
                if (day <= 0) {
                    log.warn("商户证书: {}, 已经失效, 请更换证书", i);
                    alarmService.sendAlarmFormatMsg(
                            "商户证书", "商户证书: 编号: %s, 路径: %s, 已经失效, 请更换证书",
                            i.getSerialNo(), i.getPrivateKeyPath());
                } else if (day <= 10) {
                    log.warn("商户证书: {}, 还有: {} 天失效, 请续期或更换证书", privateKeyInfo, day);
                    alarmService.sendAlarmFormatMsg(
                            "商户证书", "商户证书: 编号: %s, 路径: %s, 还有: %s 天失效, 请续期或更换证书",
                            i.getSerialNo(), i.getPrivateKeyPath(), day);
                }
            });
        } catch (Exception e) {
            log.error("检查商户证书异常:", e);
        }
    }


}
