package com.weweibuy.pay.wx.config.properties;

import com.weweibuy.framework.common.codec.rsa.RsaKeyHelper;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.ClassPathFileUtils;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;
import java.io.FileInputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author durenhao
 * @date 2021/11/4 22:17
 **/
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenProperties implements InitializingBean {

    /**
     * 有效时间 秒
     */
    private Long effectiveTimeSecond = 7200L;

    /**
     * 私钥路径
     */
    @NotBlank(message = "jwt私钥地址不能为空")
    private String privateKeyPath;

    /**
     * 公钥
     */
    private PublicKey publicKey;

    /**
     * 私钥
     */
    private PrivateKey privateKey;


    @Override
    public void afterPropertiesSet() throws Exception {
        String path = ClassPathFileUtils.getClassPath(privateKeyPath);
        if (path == null) {
            path = privateKeyPath;
        }
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            String string = IOUtils.toString(fileInputStream, CommonConstant.CharsetConstant.UT8);
            KeyPair keyPair = RsaKeyHelper.parseKeyPair(string);
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        }
    }
}
