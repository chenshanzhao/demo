package com.example.demo.utils;

import org.apache.commons.net.util.Base64;
import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RsaUtil {
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
        byte[] keyBytes = org.apache.commons.codec.binary.Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(signatureAlgorithm);
        signature.initSign(privateK);
        signature.update(data);
        return org.apache.commons.codec.binary.Base64.encodeBase64String(signature.sign());
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
        byte[] keyBytes = org.apache.commons.codec.binary.Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(signatureAlgorithm);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(org.apache.commons.codec.binary.Base64.decodeBase64(sign));
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

}
