package com.weweibuy.pay.wx.utils;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.pay.wx.client.dto.req.WxRequestAuthorizationHeader;
import com.weweibuy.pay.wx.client.dto.req.WxRequestSign;
import com.weweibuy.pay.wx.client.dto.resp.WxResponseHeader;
import com.weweibuy.pay.wx.config.properties.WxAppProperties;
import com.weweibuy.pay.wx.model.constant.WxApiConstant;
import feign.Request;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.cglib.beans.BeanMap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 签名验签工具
 *
 * @author durenhao
 * @date 2021/10/30 22:28
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SignAndVerifySignUtils {

    private static final String SYMBOLS =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String SIGN_FIELD_FORMAT = "%s=\"%s\"";

    /**
     * 微信请求头 Authorization 值
     *
     * @param request
     * @param appProperties
     * @return
     * @throws SignatureException
     * @throws InvalidKeyException
     */
    public static String authorization(Request request, WxAppProperties appProperties) throws SignatureException, InvalidKeyException {
        return WxApiConstant.CUSTOM_SIGN_SCHEMA + " " + signAndToken(request, appProperties);
    }

    /**
     * 签名 拼接 Authorization
     *
     * @param request
     * @param appProperties
     * @return
     * @throws SignatureException
     * @throws InvalidKeyException
     */
    public static String signAndToken(Request request, WxAppProperties appProperties) throws SignatureException, InvalidKeyException {
        WxRequestSign wxRequestSign = WxRequestSign.wxRequestSign(request);
        WxRequestAuthorizationHeader authorizationHeader = WxRequestAuthorizationHeader.authorizationHeader(wxRequestSign, appProperties);
        Map<Object, Object> map = BeanMap.create(authorizationHeader);
        return map.entrySet().stream()
                .map(e -> String.format(SIGN_FIELD_FORMAT, e.getKey(), e.getValue()))
                .collect(Collectors.joining(","));
    }


    /**
     * 生成随机数
     *
     * @return
     */
    public static String generateNonceStr() {
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RandomUtils.nextInt(0, SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    /**
     * 验证签名
     *
     * @param header
     * @param body
     * @param certificate
     * @return
     */
    public static boolean verifySign(WxResponseHeader header, String body, X509Certificate certificate) throws SignatureException, InvalidKeyException {
        String message = buildRespMessage(header, body);

        String signature = header.getSignatureBase64();

        return verify(certificate, message, signature);
    }

    /**
     * 响应验签  数据拼接
     *
     * @param header
     * @param body
     * @return
     */
    public static String buildRespMessage(WxResponseHeader header, String body) {
        return String.join(WxApiConstant.SIGN_DELIMITER,
                header.getTimestamp() + "",
                header.getNonce(),
                body) + WxApiConstant.SIGN_DELIMITER;
    }


    /**
     * 生成签名请求路径
     *
     * @param url
     * @return
     */
    public static String generateSignUri(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw Exceptions.business("请求地址路径格式错误", e);
        }
        return uri.getRawPath() + Optional.ofNullable(uri.getQuery())
                .map(s -> "?" + s)
                .orElse("");
    }

    /**
     * 验签
     *
     * @param certificate
     * @param message
     * @param signature
     * @return
     * @throws SignatureException
     * @throws InvalidKeyException
     */
    private static boolean verify(X509Certificate certificate, String message, String signature) throws SignatureException, InvalidKeyException {
        try {
            Signature sign = Signature.getInstance(WxApiConstant.WX_SIGN_METHOD);
            sign.initVerify(certificate);
            sign.update(message.getBytes());
            return sign.verify(Base64.getDecoder().decode(signature));
        } catch (NoSuchAlgorithmException e) {
            throw Exceptions.system("当前Java环境不支持" + WxApiConstant.WX_SIGN_METHOD, e);
        }
    }

    /**
     * 签名
     *
     * @param message
     * @param privateKey
     * @return
     * @throws SignatureException
     * @throws InvalidKeyException
     */
    public static String sign(String message, PrivateKey privateKey) throws SignatureException, InvalidKeyException {
        try {
            Signature sign = Signature.getInstance(WxApiConstant.WX_SIGN_METHOD);
            sign.initSign(privateKey);
            sign.update(message.getBytes());
            return Base64.getEncoder().encodeToString(sign.sign());
        } catch (NoSuchAlgorithmException e) {
            throw Exceptions.system("当前Java环境不支持" + WxApiConstant.WX_SIGN_METHOD, e);
        }
    }


    public static PrivateKey loadPrivateKey(InputStream inputStream) {
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                array.write(buffer, 0, length);
            }

            String privateKey = array.toString("utf-8")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw Exceptions.system("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw Exceptions.business("无效的密钥格式");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }



}
