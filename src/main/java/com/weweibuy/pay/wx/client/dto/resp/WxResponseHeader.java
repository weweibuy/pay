package com.weweibuy.pay.wx.client.dto.resp;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import com.weweibuy.pay.wx.model.constant.WxApiConstant;
import feign.Response;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 微信响应头
 *
 * @author durenhao
 * @date 2021/10/30 22:55
 **/
@Data
public class WxResponseHeader {

    /**
     * 请求id
     */
    @JsonSetter(WxApiConstant.WX_RESP_ID)
    private String requestId;

    /**
     * 随机数
     */
    @NotBlank(message = "微信响应随机数为空")
    @JsonSetter(WxApiConstant.WX_SIGN_NONCE_HEADER)
    private String nonce;

    /**
     * 签名 base64
     */
    @NotBlank(message = "微信响应签名为空")
    @JsonSetter(WxApiConstant.WX_SIGN_SIGNATURE_HEADER)
    private String signatureBase64;

    /**
     * 时间戳 秒
     */
    @NotNull(message = "微信响应时间戳为空")
    @JsonSetter(WxApiConstant.WX_SIGN_TIMESTAMP_HEADER)
    private Long timestamp;

    /**
     * 证书编号
     */
    @NotNull(message = "微信响应平台证书号为空")
    @JsonSetter(WxApiConstant.WX_SIGN_SERIAL_HEADER)
    private String serial;


    public static WxResponseHeader fromWxResponse(Response response) {
        Map<String, Collection<String>> headers = response.headers();
        Map<String, String> stringStringMap = convertMap(headers);
        return fromHeader(stringStringMap);

    }

    public static WxResponseHeader fromHeader(Map<String, String> header) {
        ObjectMapper objectMapper = JackJsonUtils.getCamelCaseMapper();
        JavaType javaType = JackJsonUtils.javaType(WxResponseHeader.class);
        return (WxResponseHeader) objectMapper.convertValue(header, javaType);
    }

    private static Map<String, String> convertMap(Map<String, Collection<String>> headers) {
        return headers.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e ->
                        Optional.ofNullable(e.getValue())
                                .filter(CollectionUtils::isNotEmpty)
                                .map(i -> i.iterator().next())
                                .orElse(null)));
    }


}
