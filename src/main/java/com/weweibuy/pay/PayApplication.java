package com.weweibuy.pay;

import com.weweibuy.framework.common.core.utils.IdWorker;
import com.weweibuy.framework.common.log.support.LogTraceContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 支付服务
 *
 * @author durenhao
 * @date 2021/10/30 10:37
 **/
@EnableCaching
@EnableFeignClients
@SpringBootApplication
public class PayApplication {

    public static void main(String[] args) {
        LogTraceContext.setTraceCode(IdWorker.nextStringId());
        try {
            SpringApplication.run(PayApplication.class, args);
        } finally {
            LogTraceContext.clear();
        }
    }

}
