package com.weweibuy.pay.wx.config;

import com.weweibuy.pay.wx.config.properties.WxAppProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * bean 配置
 *
 * @author durenhao
 * @date 2021/10/31 21:05
 **/
@Configuration
@EnableConfigurationProperties({WxAppProperties.class})
public class BeanPropertiesConfig {
}
