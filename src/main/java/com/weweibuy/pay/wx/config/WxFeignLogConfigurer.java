package com.weweibuy.pay.wx.config;

import com.weweibuy.framework.common.feign.support.FeignLogConfigurer;
import com.weweibuy.framework.common.feign.support.FeignLogSetting;
import com.weweibuy.pay.wx.model.constant.WxApiConstant;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * wx 请求日志输出配置
 *
 * @author durenhao
 * @date 2021/10/31 23:10
 **/
@Component
public class WxFeignLogConfigurer implements FeignLogConfigurer {

    @Override
    public void configurer(List<FeignLogSetting> feignLogSettingList) {
        feignLogSettingList.add(FeignLogSetting.builder()
                .host(WxApiConstant.WX_MCH_PAY_HOST)
                .reqHeaderList(Arrays.asList(WxApiConstant.AUTHORIZATION_HEADER))
                .respHeaderList(Arrays.asList(WxApiConstant.WX_RESP_ID,
                        WxApiConstant.WX_SIGN_TIMESTAMP_HEADER, WxApiConstant.WX_SIGN_NONCE_HEADER,
                        WxApiConstant.WX_SIGN_SERIAL_HEADER, WxApiConstant.WX_SIGN_SIGNATURE_HEADER))
                .build());
    }
}
