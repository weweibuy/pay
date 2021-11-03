package com.weweibuy.pay.wx.model.event;

import com.weweibuy.framework.common.core.support.AlarmService;
import lombok.Builder;
import lombok.Data;

/**
 * 业务报警事件
 *
 * @author durenhao
 * @date 2021/11/3 21:16
 **/
@Data
@Builder
public class BizAlarmEvent {

    private AlarmService.AlarmLevel alarmLevel;

    private String bizType;

    private String msg;

}
