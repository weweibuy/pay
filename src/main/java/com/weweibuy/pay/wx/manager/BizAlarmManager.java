package com.weweibuy.pay.wx.manager;

import com.weweibuy.framework.common.core.support.AlarmService;
import com.weweibuy.framework.common.log.support.LogTraceContext;
import com.weweibuy.pay.wx.model.event.BizAlarmEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

/**
 * 报警管理
 *
 * @author durenhao
 * @date 2021/11/3 21:16
 **/
@Component
@RequiredArgsConstructor
public class BizAlarmManager {

    @Autowired
    @Qualifier("alarmThreadPool")
    private ExecutorService alarmThreadPool;

    private final AlarmService alarmService;

    @EventListener
    public void onAlarmEvent(BizAlarmEvent alarmEvent) {
        String msg = alarmEvent.getMsg() + "  【trace:" + LogTraceContext.getTraceCodeOrEmpty() + "】";
        alarmThreadPool.execute(() -> alarmService.sendAlarm(alarmEvent.getAlarmLevel(), alarmEvent.getBizType(), msg));
    }


}
