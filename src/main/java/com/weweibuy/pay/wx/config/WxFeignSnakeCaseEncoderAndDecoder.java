package com.weweibuy.pay.wx.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import com.weweibuy.framework.common.feign.support.EncoderAndDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * WX feign 序列化发序列化
 *
 * @author durenhao
 * @date 2021/11/1 22:26
 **/
public class WxFeignSnakeCaseEncoderAndDecoder extends EncoderAndDecoder {

    private static ObjectFactory<HttpMessageConverters> httpMessageConvertersObjectFactory;

    private static final ObjectMapper WX_OBJECT_MAPPER = JackJsonUtils.createObjectMapper("yyyy-MM-dd'T'HH:mm:ss'+08:00'",
            CommonConstant.DateConstant.STANDARD_DATE_FORMAT_STR, PropertyNamingStrategies.SNAKE_CASE);

    public WxFeignSnakeCaseEncoderAndDecoder() {
        super(messageConverters());
    }

    static synchronized ObjectFactory<HttpMessageConverters> messageConverters() {
        if (httpMessageConvertersObjectFactory == null) {
            MappingJackson2HttpMessageConverter messageConverter =
                    new MappingJackson2HttpMessageConverter(WX_OBJECT_MAPPER);
            HttpMessageConverters converters = new HttpMessageConverters(messageConverter);
            httpMessageConvertersObjectFactory = () -> converters;
        }
        return httpMessageConvertersObjectFactory;
    }

    public static ObjectMapper getWxObjectMapper() {
        return WX_OBJECT_MAPPER;
    }


}
