package com.weweibuy.pay.wx.model.dto.common;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 微信加密传输的数据
 *
 * @author durenhao
 * @date 2021/11/2 22:28
 **/
@Data
public class WxEncryptDataDTO {

    /**
     * 对开启结果数据进行加密的加密算法，目前只支持AEAD_AES_256_GCM
     */
    @NotBlank(message = "加密算法类型不能为空")
    private String algorithm;

    /**
     * 加密使用的随机串
     */
    @NotBlank(message = "加密使用的随机串不能为空")
    private String nonce;

    /**
     * 附加数据
     * 可能为空
     */
    private String associatedData;

    /**
     * Base64编码后的开启/停用结果数据密文
     */
    @NotBlank(message = "数据密文不能为空")
    private String ciphertext;

}
