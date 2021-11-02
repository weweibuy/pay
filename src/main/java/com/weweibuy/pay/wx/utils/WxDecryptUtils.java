package com.weweibuy.pay.wx.utils;

import com.weweibuy.framework.common.codec.aes.Aes256GcmUtils;
import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.pay.wx.model.dto.common.WxEncryptDataDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

/**
 * 微信解密工具
 *
 * @author durenhao
 * @date 2021/11/2 23:11
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WxDecryptUtils {


    /**
     * 微信数据解密
     *
     * @param encryptDataDTO
     * @param secretKey
     * @return
     * @throws GeneralSecurityException
     */
    public static String decrypt(WxEncryptDataDTO encryptDataDTO, SecretKey secretKey) throws GeneralSecurityException {
        String cipherText = encryptDataDTO.getCiphertext().replaceAll("\"", "");
        String associatedData = encryptDataDTO.getAssociatedData().replaceAll("\"", "");
        String nonce = encryptDataDTO.getNonce().replaceAll("\"", "");
        X509Certificate x509Cert = null;
        try {
            return Aes256GcmUtils.decryptBase64Text(cipherText, associatedData, nonce, secretKey);
        } catch (IOException e) {
            throw Exceptions.uncheckedIO(e);
        }
    }


}
