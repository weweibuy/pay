package com.weweibuy.pay.wx.config.properties;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import com.weweibuy.pay.wx.utils.SignAndVerifySignUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 微信支付配置
 *
 * @author durenhao
 * @date 2021/10/30 10:51
 **/
@Slf4j
@Data
@ConfigurationProperties(prefix = "wx.pay")
public class WxAppProperties implements InitializingBean {

    /**
     * 商户号
     */
    @NotBlank(message = "商户号不能为空")
    private String merchantId;

    /**
     * appid
     */
    @NotBlank(message = "appId不能为空")
    private String appId;

    /**
     * api 秘钥
     */
    @Length(min = 32, max = 32, message = "api密钥长度必须为32位")
    private String apiKey;

    /**
     * 证书列表
     */
    @NotEmpty
    @Valid
    private List<SerialNoPrivateKeyInfo> privateKeyInfo;

    // ====================== 下面为计算出的配置 =========================

    /**
     * api 秘钥
     */
    private SecretKey apiSecretKey;


    /**
     * 处理后的证书信息
     */
    private List<SerialNoPrivateKey> privateKey;


    @Data
    public static class SerialNoPrivateKeyInfo {

        /**
         * 证书编号
         */
        @NotBlank(message = "证书编号不能为空")
        private String serialNo;

        /**
         * 证书路径地址
         * <p>
         * <p>
         * eg: classpath:db-encrypt.pem  or
         * <p>
         * db-encrypt.pem 项目路径
         * or
         * /db-encrypt.pem  绝对路径
         */
        @NotBlank(message = "证书路径地址不能为空")
        private String privateKeyPath;

        /**
         * 失效时间  yyyy-MM-dd HH:mm:ss
         */
        @NotBlank(message = "证书失效时间不能为空")
        private String expireTime;

    }

    @Data
    public static class SerialNoPrivateKey {

        /**
         * 证书编号
         */
        private String serialNo;

        /**
         * 私钥
         */
        private PrivateKey privateKey;

        /**
         * 失效时间
         */
        private LocalDateTime expireTime;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initApiSecretKey();
        initPrivateKey();
    }

    private void initApiSecretKey() {
        apiSecretKey = new SecretKeySpec(apiKey.getBytes(), "AES");
    }


    private void initPrivateKey() {
        List<SerialNoPrivateKey> serialNoPrivateKeyList = privateKeyInfo.stream()
                .map(this::serialNoPrivateKey)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(SerialNoPrivateKey::getExpireTime).reversed())
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(serialNoPrivateKeyList)) {
            throw Exceptions.business("无有效证书, 请检查证书信息");
        }
        privateKey = serialNoPrivateKeyList;
    }

    private SerialNoPrivateKey serialNoPrivateKey(SerialNoPrivateKeyInfo serialNoPrivateKeyInfo) {
        String expireTime = serialNoPrivateKeyInfo.getExpireTime();
        LocalDateTime localDateExpireTime = null;
        try {
            localDateExpireTime = DateTimeUtils.stringToLocalDateTime(expireTime);
        } catch (Exception e) {
            throw Exceptions.business("证书: " + serialNoPrivateKeyInfo + ", 失效时间错误");
        }
        if (localDateExpireTime.compareTo(LocalDateTime.now()) < 0) {
            log.error("证书: {} 已经失效, 请更换证书", serialNoPrivateKeyInfo);
            return null;
        } else if (localDateExpireTime.compareTo(LocalDateTime.now().plusDays(-1)) < 0) {
            log.warn("证书: {} 即将失效, 请更换证书", serialNoPrivateKeyInfo);
        }

        String privateKeyPath = serialNoPrivateKeyInfo.getPrivateKeyPath();
        String cl = "classpath:";
        if (privateKeyPath.startsWith(cl)) {
            File classPathFile = new File(WxAppProperties.class.getResource("/").getPath());
            String classPath = null;
            try {
                classPath = classPathFile.getCanonicalPath();
            } catch (IOException e) {
                throw Exceptions.uncheckedIO(e);
            }
            privateKeyPath = classPath + File.separator + privateKeyPath.substring(cl.length(), privateKeyPath.length());
        }

        try (FileInputStream fileInputStream = new FileInputStream(privateKeyPath)) {
            PrivateKey privateKey = SignAndVerifySignUtils.loadPrivateKey(fileInputStream);
            SerialNoPrivateKey serialNoPrivateKey = new SerialNoPrivateKey();
            serialNoPrivateKey.setSerialNo(serialNoPrivateKeyInfo.getSerialNo());
            serialNoPrivateKey.setExpireTime(localDateExpireTime);
            serialNoPrivateKey.setPrivateKey(privateKey);
            return serialNoPrivateKey;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

}
