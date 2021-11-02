package com.weweibuy.pay.wx.support;

import com.weweibuy.framework.common.codec.aes.Aes256GcmUtils;
import com.weweibuy.pay.wx.model.dto.common.WxEncryptDataDTO;
import org.junit.Test;

import javax.crypto.spec.SecretKeySpec;
import java.security.cert.X509Certificate;

public class CertificatesHelperTest {

    @Test
    public void platformCertificate() throws Exception {
        String pk = "MIIDADCCAegCCQC2LOOmdHFC4DANBgkqhkiG9w0BAQsFADBCMQswCQYDVQQGEwJY\n" +
                "WDEVMBMGA1UEBwwMRGVmYXVsdCBDaXR5MRwwGgYDVQQKDBNEZWZhdWx0IENvbXBh\n" +
                "bnkgTHRkMB4XDTIxMTEwMjEwMjkxMFoXDTIyMTEwMjEwMjkxMFowQjELMAkGA1UE\n" +
                "BhMCWFgxFTATBgNVBAcMDERlZmF1bHQgQ2l0eTEcMBoGA1UECgwTRGVmYXVsdCBD\n" +
                "b21wYW55IEx0ZDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMFE+wzB\n" +
                "xcMJ2OVV8KgwvKRgy7VSNYyJljG6ctT2WjgOGZKdbac3p5TClyZkmg4OnA/2baER\n" +
                "ZrE7AAE8hM/iwqqcXYn0Z4dS2qRaOBXLK9DK8wncB55on/feDDy3ZF6jB/sfmI5K\n" +
                "8ackFOMAt7te8jtA17nslg3S43zji07dmRk23KNUMH2B11vkRWB9OzMlb8ty8ZAA\n" +
                "u2Zq/5Aw1a+OGU/F1NPDwC4vGAukShyyKk3hSvIGz2UQtIhrdgh8wl19S7RezSAO\n" +
                "U3VVNzPb12a79OAr2YT6i0uYfGqK0qLtQMcd+jxfXx7zgoTsPedt8BLVqOfx9Phj\n" +
                "RnygN5U+VWpuc/0CAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAoRBNh+M1hARinQ3Z\n" +
                "r1tq57D9691pOQbN6pBQXGRfkyAehZDixBZeix1bRD1PO92uqULoDqtY65o9w0Vu\n" +
                "rMRCGCRGGbQ1AoKxzsyakLXCvf6AE7ssay/PtWAFZsTVOiuAW3XJBLU60Q35/j8+\n" +
                "k3j80tDVfTkBJcC7IMeBcR8N3Q5JN53IT1CPrq+zVyewSZf8UKniC0UwSRFwDebu\n" +
                "sWqBN9fC7nDcZOPrLxZx3pIT3hX7/mpYWZF6nc1eUZdYAtZytAjjVSGJSN/K1Q5r\n" +
                "coV9n0xsdE63MYcJ6qastjzHHhCxdg2LK/ItG8n+dD+/SHOwRrRwhKVE2F043KlI\n" +
                "hoyKbA==";

        SecretKeySpec keySpec = new SecretKeySpec("12345678901234567890123456789012".getBytes(), "AES");

        String all = pk.replaceAll("\n", "");

        String certificate = Aes256GcmUtils.encryptToBase64(all, "certificate", "61f9c719728a", keySpec);

        System.err.println(certificate);

        WxEncryptDataDTO encryptCertificate = new WxEncryptDataDTO();


        encryptCertificate.setNonce("61f9c719728a");
        encryptCertificate.setAssociatedData("certificate");
        encryptCertificate.setCiphertext(certificate);

        X509Certificate x509Certificate = CertificatesHelper.platformCertificate(encryptCertificate, keySpec);
        x509Certificate.checkValidity();


    }

}