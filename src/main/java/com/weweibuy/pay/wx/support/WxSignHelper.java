package com.weweibuy.pay.wx.support;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.support.AlarmService;
import com.weweibuy.framework.common.log.support.LogTraceContext;
import com.weweibuy.pay.wx.client.dto.resp.WxResponseHeader;
import com.weweibuy.pay.wx.model.constant.AlarmConstant;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.time.Duration;
import java.time.Instant;

/**
 * @author durenhao
 * @date 2021/11/3 0:01
 **/
@Component
@RequiredArgsConstructor
public class WxSignHelper implements InitializingBean {

    private final Validator validator;

    private final AlarmService alarmService;

    private final Environment environment;

    private boolean isMock = false;


    public void verifyResponseHeader(WxResponseHeader wxResponseHeader) {
        validator.validate(wxResponseHeader).stream()
                .findFirst()
                .map(c -> c.getMessage())
                .ifPresent(m -> {
                    String msg = m + ", 请求id: " + wxResponseHeader.getRequestId();
                    alarmService.sendAlarm(AlarmService.AlarmLevel.WARN, AlarmConstant.WX_PAY_ALARM_BIZ_TYPE, msg + ", trace: "
                            + LogTraceContext.getTraceCode().orElse(""));
                    throw Exceptions.business(msg);
                });
        Long timestamp = wxResponseHeader.getTimestamp();

        Instant instant = Instant.ofEpochSecond(timestamp);
        // 拒绝5分钟之外的应答  mock 环境不检验
        if (!isMock && Duration.between(instant, Instant.now()).abs().toMinutes() >= 5) {
            String msg = "微信通知/响应时间戳异常, 请求id: " + wxResponseHeader.getRequestId();
            alarmService.sendAlarm(AlarmService.AlarmLevel.WARN, AlarmConstant.WX_PAY_ALARM_BIZ_TYPE, msg + ", trace: "
                    + LogTraceContext.getTraceCode().orElse(""));
            throw Exceptions.business(msg);
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        String profiles = environment.getProperty("spring.profiles.active");
        if (StringUtils.isNotBlank(profiles) && profiles.indexOf("mock") != -1) {
            isMock = true;
        }
    }
}
