//package com.weweibuy.apy.wx.demo;
//
//import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
//import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
//import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
//import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
//import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
//import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
//import org.apache.commons.codec.binary.Base64;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//
//import java.io.ByteArrayInputStream;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.security.*;
//import java.security.cert.Certificate;
//import java.security.cert.X509Certificate;
//import java.util.Enumeration;
//
///**
// * @author durenhao
// * @date 2021/10/30 11:19
// **/
//public class WxSdkDemo {
//
//
//    public void demo() throws Exception {
//        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(
//                new FileInputStream("/path/to/apiclient_key.pem"));
//
//        PrivateKey merchantPrivateKey1 = PemUtil.loadPrivateKey(
//                new ByteArrayInputStream("privateKey".getBytes("utf-8")));
//
//
//        //不需要传入微信支付证书了
//        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
//                new WechatPay2Credentials(merchantId, new PrivateKeySigner(merchantSerialNumber, merchantPrivateKey)),
//                apiV3Key.getBytes("utf-8"));
//
//        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
//                .withMerchant(merchantId, merchantSerialNumber, merchantPrivateKey)
//                .withValidator(new WechatPay2Validator(verifier))
//// ... 接下来，你仍然可以通过builder设置各种参数，来配置你的HttpClient
//
//// 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签，并进行证书自动更新
//        HttpClient httpClient = builder.build();
//
//// 后面跟使用Apache HttpClient一样
//        HttpResponse response = httpClient.execute(...);
//
//    }
//
//
//    public static void main(String[] args) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
//        String keyStorePath = "E:/client_01.p12";
//        String password = "123456";
//        // 实例化密钥库，默认JKS类型
//        KeyStore ks = KeyStore.getInstance("PKCS12");
//        // 获得密钥库文件流
//        FileInputStream is = new FileInputStream(keyStorePath);
//        // 加载密钥库
//        ks.load(is, password.toCharArray());
//        // 关闭密钥库文件流
//        is.close();
//
//        //私钥
//        Enumeration aliases = ks.aliases();
//        String keyAlias = null;
//        if (aliases.hasMoreElements()) {
//            keyAlias = (String) aliases.nextElement();
//            System.out.println("p12's alias----->" + keyAlias);
//        }
//        PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias, password.toCharArray());
//        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
//        System.out.println("私钥------------->" + privateKeyStr);
//
//        //公钥
//        X509Certificate certificate = (X509Certificate)ks.getCertificate(keyAlias);
//        String publicKeyStr = Base64.encodeBase64String(certificate.getPublicKey().getEncoded());
//        System.out.println("公钥------------->" + publicKeyStr);
//    }
//
//}
