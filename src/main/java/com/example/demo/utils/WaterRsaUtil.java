package com.example.demo.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WaterRsaUtil {
    public static final String KEY_ALGORITHM = "RSA";

    public static final String SIGNATURE_ALGORITHM_MD5 = "MD5withRSA";
    public static final String SIGNATURE_ALGORITHM_RSA256 = "SHA256withRSA";
    public static final String SIGNATURE_ALGORITHM_RSA128 = "SHA1WithRSA";

    private static final int KEY_SIZE = 1024;

    //1024位密钥
    //RSA加密块最大为117bytes
    public static final int ENCRYPT_BLOCK_1024 = 117;
    //RSA解密块最大为128bytes
    public static final int DECRYPT_BLOCK_1024 = 128;

    //2048位密钥
    public static final int ENCRYPT_BLOCK_2048 = 234;
    public static final int DECRYPT_BLOCK_2048 = 256;

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    // 2048位公钥，密钥
    public static final String str_pubK = "公钥";
    public static final String str_priK = "私钥";


    /**
     * 得到公钥
     *
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (Base64.decodeBase64(key));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 得到私钥
     *
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (Base64.decodeBase64(key));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    //***************************签名和验证*******************************

    /**私钥签名
     *
     * @param data 原文字节数组
     * @param privateKey 私钥
     * @param signatureAlgorithm 签名算法
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey,  String signatureAlgorithm) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(signatureAlgorithm);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encodeBase64String(signature.sign());
    }

    /**公钥验签
     *
     * @param data 原文字节数组
     * @param publicKey 公钥
     * @param sign 签名
     * @param signatureAlgorithm 签名算法
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign, String signatureAlgorithm)
            throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(signatureAlgorithm);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decodeBase64(sign));
    }

    //************************加密解密**************************
    //加密
    public static String encrypt(String data, String str_pubK, int ENCRYPT) throws Exception {
        PublicKey publicKey = getPublicKey(str_pubK);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] bytes = data.getBytes("UTF-8");
        int inputLen = bytes.length;
        int offLen = 0; //偏移量
        int i = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while(inputLen - offLen > 0){
            byte[] cache;
            if(inputLen - offLen > ENCRYPT){
                cache = cipher.doFinal(bytes, offLen, ENCRYPT);
            }else{
                cache = cipher.doFinal(bytes, offLen, inputLen - offLen);
            }
            byteArrayOutputStream.write(cache);
            i++;
            offLen = ENCRYPT * i;
        }
        byteArrayOutputStream.close();
        byte[] bt_encrypt = byteArrayOutputStream.toByteArray();
        String encryptStr = Base64.encodeBase64String(bt_encrypt);
        return encryptStr.replaceAll("\\r\\n", "");
    }

    //解密
    public static String decrypt(String data, String str_priK, int DECRYPT) throws Exception {
        PrivateKey privateKey = getPrivateKey(str_priK);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] bytes = Base64.decodeBase64(data);
        int inputLen = bytes.length;
        int offLen = 0;
        int i = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while(inputLen - offLen > 0){
            byte[] cache;
            if(inputLen - offLen > DECRYPT){
                cache = cipher.doFinal(bytes, offLen, DECRYPT);
            }else{
                cache = cipher.doFinal(bytes, offLen, inputLen - offLen);
            }
            byteArrayOutputStream.write(cache);
            i++;
            offLen = DECRYPT * i;
        }
        byteArrayOutputStream.close();
        byte[]  bt_decrypt = byteArrayOutputStream.toByteArray();

        return new String(bt_decrypt,"UTF-8");
    }
    private static final String PUBLICKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAglmI4BANJGbgDKqop/33G9Cjpf8Ic9IlGoV5FDtNnXh1JjVNJ7GTTAozUJELtbgyrbayX+v522BLAZQbHnHDGTQHLFTJyi96UvGDmA3j3ZvJrrrL+ryqY/qwD4ZE1MOLDbvCksliZGFhKZzaKEU293MunkssWJtpUrNgn6Cx33MkxI2bzD8gQP+Ml6eVQ5CmixAVx9olEBHD9nsGjhu/8+zdx8YvkpVamkDesDX/k971sTKhSzkUxxBHoQ0r4eDuacedaNiDIzEEn2BUv8hnLXO8ct678kKz/wlc4qaOmwotbAUIz/+z3y2eQcvmIkvIJuKDktXK61QXmsOeFxq6qQIDAQAB";
    private static final String PRIVATEKEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCCWYjgEA0kZuAMqqin/fcb0KOl/whz0iUahXkUO02deHUmNU0nsZNMCjNQkQu1uDKttrJf6/nbYEsBlBseccMZNAcsVMnKL3pS8YOYDePdm8muusv6vKpj+rAPhkTUw4sNu8KSyWJkYWEpnNooRTb3cy6eSyxYm2lSs2CfoLHfcyTEjZvMPyBA/4yXp5VDkKaLEBXH2iUQEcP2ewaOG7/z7N3Hxi+SlVqaQN6wNf+T3vWxMqFLORTHEEehDSvh4O5px51o2IMjMQSfYFS/yGctc7xy3rvyQrP/CVzipo6bCi1sBQjP/7PfLZ5By+YiS8gm4oOS1crrVBeaw54XGrqpAgMBAAECggEAVQYJ3f0aVXA1HI4EzO8/TAlfdVtRLBsa0J3XSHcWPWzhHTcPSKDvr13H8vlpZfLsO5s75o2JwKqiwjjVJ5qU48+oDiaQKS2m4ItghsIoq9h+SVbWzopZqjVOuhGevBmAjki2tgBeCZPrSLVjJ3leTTH4apAmMPXOUX9nuzeaTmFfz39nN90FMTcXEuwXTFf563GOT6kwalmv/1vhwrqH2De4qa8lzHGANrNgXTIz6ezhgBFKX9EnE6soWts4KXllUPCE+bNIDtjUuQwRyMOSpFXDkiLRgFLerxy7vU1jUiO8KFWhPSc7rEKiqq3ureiVWxzO6dn4pIthO+MqkbmgsQKBgQDUPFS1oPFH+RTRUkDU+JxFOD3OnwbKgsVPpL70Qi5FKWncUQwQDxwpiBGopoiRsi58rAKk4foNQEZJCkjrY7jyjxb8n94rgE9cHGrCDgvwtGBo2eUC7HUmXMGAt1AcJXtFTro+dYyYER5HieLSwmvfpT4300EAJPK8QMZs4L0lhwKBgQCdOosFsoyAyUroDbyK0vjAEP9+RWf3SbX7rdDbbO9bTHFSeAuw0rHQ2GKTJRb9RYCXaaKd55WPIIneW7jCfSQJ4KTCWxLosOOyattBE61mKR0NdbludTx94lBrKhv4iNK7S6ISFHPGPjlos0J9NKpZyc2hAoc+MiB0MlF5C8wqTwKBgQDIf+pi4BWxMc2J5lPbHra66ePyA+gtfpcao3/GT5NYwUQB4AsuTKgFUEemdRVF1vf1zaZ90AIjbJo5vF6hCceaVrWXmQwROpk+YpY0l4KONklyJI8mUO5nfG9Nw2AKBxrKohJumSxmKJ3bZZcEFqpEUN5TbN7WXRF36RXLnBD0lwKBgGIUTPjm4oLMDrAnMYAgDxAxClAIl8xI7P7s9KaMk9uOGfrp69EH/ehbs1I7BtZDbu4E9W4vMrqv/5D4Ao9AZSTMP7sF+QuynLeFtPQphDeAWiE1WNEwzOVyMdxHzl8BawNvMNC6dWYrFsDbYhScr8+G9YEFBLylpF0Xrn8l/tw9AoGBALbOYaC4U0NewSpK8wz1ORFN9oinDIOZNI+JZODJxZFRXtU1zQpxm0t5y/tJvSDnEXpFGerfYDCxUHItnG+gEAOOwGdEltSJKijwjxYvV6OvfKHvWijIXqjUNNQF3QGdX7N7+2K1aD5lJftFhDhi1ZhnNwfrEY26f/mz7keeTkPJ";
    private static final String public_key="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuLTIGknigE7xpP6oXmIIDEY1K0g/h8jfkE6KuMH3KrXYY7/+rrYKGIcyk+aZfb6P/3xJxAiXBIkGLUxtILy+6dinWUXdyg6jvciUP8O3Zr3zJ393OzObE2WSPpVtvNGGEseczIO6Ws/pajzzOWKVhOUAg7OahdIVhfTVJpe/j9RxuiLBtDUiZVUnbakcFAwkg1cqGG2jZlHdAeMUNQM1UTNT/1+N+Kp6d5ws0hEovkIhwz52pP8x9e4qJAfyotr7tgyuA8gtBKnfE9NlrdgBiffNzHCamC0uXUTCJIGfUj2o/9ORlbcfoWz9mK82OzSIP9qpfqiioSCz6xwLJnhrVwIDAQAB";
    public static void main(String[] args) throws Exception {
        String data="{\"data\":{\"type\":\"outlet\"},\"time\":\"2021-01-19 17:46:33\",\"source\":\"RYB\",\"orgid\":\"20210120\"}";
        JSONObject reqJson = new JSONObject();
        reqJson.put("orgid", "20210120");
        reqJson.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        reqJson.put("source", "RYB");
        JSONObject dataJson = new JSONObject();
//        dataJson.put("hh", hh);
        dataJson.put("type", "outlet");
        reqJson.put("data", dataJson);
        String result= WaterRsaUtil.encrypt(dataJson.toString(),public_key,ENCRYPT_BLOCK_2048);
       String str = "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";
       String resultde=   WaterRsaUtil.decrypt(str,PRIVATEKEY,DECRYPT_BLOCK_2048);
//        System.out.print(result);
        System.out.println(resultde);

    }
}
