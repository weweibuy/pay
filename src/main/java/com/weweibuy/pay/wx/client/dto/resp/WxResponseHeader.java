package com.weweibuy.pay.wx.client.dto.resp;

import com.weweibuy.pay.wx.constant.WxApiConstant;
import feign.Response;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

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
    private String requestId;

    /**
     * 随机数
     */
    @NotBlank(message = "微信响应随机数为空")
    private String nonce;

    /**
     * 签名 base64
     */
    @NotBlank(message = "微信响应签名为空")
    private String signatureBase64;

    /**
     * 时间戳 秒
     */
    @NotNull(message = "微信响应时间戳为空")
    private Long timestamp;

    /**
     * 证书编号
     */
    @NotNull(message = "微信响应平台证书号为空")
    private String serial;


    public static WxResponseHeader fromWxResponse(Response response) {
        Map<String, Collection<String>> headers = response.headers();
        WxResponseHeader wxResponseHeader = new WxResponseHeader();

        valueFromHeader(headers, WxApiConstant.WX_RESP_ID, wxResponseHeader::setRequestId);
        valueFromHeader(headers, WxApiConstant.WX_SIGN_NONCE_HEADER, wxResponseHeader::setNonce);
        valueFromHeader(headers, WxApiConstant.WX_SIGN_SIGNATURE_HEADER, wxResponseHeader::setSignatureBase64);
        valueFromHeader(headers, WxApiConstant.WX_SIGN_SERIAL_HEADER, wxResponseHeader::setSerial);
        Optional.ofNullable(headers.get(WxApiConstant.WX_SIGN_TIMESTAMP_HEADER))
                .filter(CollectionUtils::isNotEmpty)
                .map(l -> l.iterator().next())
                .filter(StringUtils::isNumeric)
                .map(Long::valueOf)
                .ifPresent(wxResponseHeader::setTimestamp);
        return wxResponseHeader;

    }

    private static void valueFromHeader(Map<String, Collection<String>> headers, String key, Consumer<String> consumer) {
        Optional.ofNullable(headers.get(key))
                .filter(CollectionUtils::isNotEmpty)
                .map(l -> l.iterator().next())
                .ifPresent(consumer);
    }

}
