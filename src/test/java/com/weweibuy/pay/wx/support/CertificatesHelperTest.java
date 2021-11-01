package com.weweibuy.pay.wx.support;

import com.weweibuy.framework.common.codec.aes.Aes256GcmUtils;
import org.junit.Test;

import javax.crypto.spec.SecretKeySpec;

public class CertificatesHelperTest {

    @Test
    public void platformCertificate() throws Exception {
        String pk = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwUT7DMHFwwnY5VXwqDC8\n" +
                "pGDLtVI1jImWMbpy1PZaOA4Zkp1tpzenlMKXJmSaDg6cD/ZtoRFmsTsAATyEz+LC\n" +
                "qpxdifRnh1LapFo4Fcsr0MrzCdwHnmif994MPLdkXqMH+x+YjkrxpyQU4wC3u17y\n" +
                "O0DXueyWDdLjfOOLTt2ZGTbco1QwfYHXW+RFYH07MyVvy3LxkAC7Zmr/kDDVr44Z\n" +
                "T8XU08PALi8YC6RKHLIqTeFK8gbPZRC0iGt2CHzCXX1LtF7NIA5TdVU3M9vXZrv0\n" +
                "4CvZhPqLS5h8aorSou1Axx36PF9fHvOChOw9523wEtWo5/H0+GNGfKA3lT5Vam5z\n" +
                "/QIDAQAB";

        SecretKeySpec keySpec = new SecretKeySpec("12345678901234567890123456789012".getBytes(), "AES");
        int length = "12345678901234567890123456789012".length();
        System.err.println(length);

        String all = pk.replaceAll("\n", "");

        String certificate = Aes256GcmUtils.encryptToBase64(all, "certificate", "61f9c719728a", keySpec);
        System.err.println(certificate);

    }

}