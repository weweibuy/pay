package com.weweibuy.pay.wx.model.vo;

import lombok.Builder;
import lombok.Data;

import java.security.cert.X509Certificate;

/**
 * 证书
 *
 * @author durenhao
 * @date 2021/10/30 14:30
 **/
@Builder
@Data
public class SerialNoCertificate {

    private String serialNo;

    private X509Certificate certificate;

}
