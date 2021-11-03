package com.weweibuy.pay.wx.config;

import com.weweibuy.framework.common.core.concurrent.LogExceptionThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 *
 * @author durenhao
 * @date 2021/11/3 21:20
 **/
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ExecutorService alarmThreadPool() {
        return new ThreadPoolExecutor(1, 3,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(20),
                new LogExceptionThreadFactory("alarm-"),
                new ThreadPoolExecutor.DiscardPolicy());
    }


}
