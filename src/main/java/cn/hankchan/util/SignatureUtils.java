package cn.hankchan.util;

import cn.hankchan.util.HexUtils;
import cn.hankchan.enums.SignMethod;
import cn.hankchan.util.Strings;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;

/**
 * 参数签名工具类
 * @author hankchan
 * 2017年7月1日 - 下午3:30:24
 */
public class SignatureUtils {

    public static final String UTF8 = "UTF-8";

    private static final String MD5_ALGORITHM = "MD5";
    private static final String HMAC_MD5_ALGORITHM = "HmacMD5";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private SignatureUtils() {}

    /**
     * 对需要将urlPath加入签名字符串的请求参数进行签名
     * @param urlPath urlPath
     * @param params 请求参数集合
     * @param secret 密钥
     * @param method 签名方法，目前支持HMAC_SHA1
     * @return 大写的签名结果字符串
     */
    public static String signParamsWithUrlPath(String urlPath, Map<String, String> params,
            String secret, SignMethod method) {
        // 判断是否是HMAC_SHA1算法
        if(!SignMethod.HMAC_SHA1.equals(method)) {
            return null;
        }
        // 检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 拼接参数名和参数值
        StringBuilder stringBuilder = new StringBuilder(urlPath);

        // 开始拼接参数名和参数值，如：key1value1key2value2
        for(String key : keys) {
            String value = params.get(key);
            if(Strings.areNotEmpty(key, value)) {
                stringBuilder.append(key).append(value);
            }
        }
        byte[] bytes = null;
        // 使用Hmac_Sha1加密
        bytes = encryptHmacSha1(stringBuilder.toString(), secret);
        return HexUtils.parseByte2UppercaseHexString(bytes);
    }

    /**
     * 对请求参数进行签名摘要
     * @param params 请求参数
     * @param secret 密钥
     * @param method 签名摘要算法
     * @return 签名结果，如果不需要签名，返回null
     */
    public static String signParams(Map<String, String> params, String secret, SignMethod method) {

        // 判断是否需要签名
        if(SignMethod.UN_SIGNATURE.equals(method)) {
            return null;
        }

        // 检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 拼接参数名和参数值
        StringBuilder stringBuilder = new StringBuilder();

        // 如果是MD5算法，在字符串前面添加secret
        if(SignMethod.MD5.equals(method)) {
            stringBuilder.append(secret);
        }

        // 开始拼接参数名和参数值，如：key1value1key2value2
        for(String key : keys) {
            String value = params.get(key);
            if(Strings.areNotEmpty(key, value)) {
                stringBuilder.append(key).append(value);
            }
        }

        // 使用加密算法加密
        byte[] bytes = null;
        if(SignMethod.MD5.equals(method)) {
            // 使用MD5算法加密
            stringBuilder.append(secret);
            bytes = encryptMD5(stringBuilder.toString());
        } else if(SignMethod.HMAC_MD5.equals(method)) {
            // 使用HMAC_Md5加密
            bytes = encryptHmacMd5(stringBuilder.toString(), secret);
        } else if(SignMethod.HMAC_SHA1.equals(method)) {
            // 使用Hmac_Sha1加密
            bytes = encryptHmacSha1(stringBuilder.toString(), secret);
        }
        return HexUtils.parseByte2UppercaseHexString(bytes);
    }

    /**
     * 使用MD5算法加密
     * @param data 数据
     * @return byte字节流
     */
    public static byte[] encryptMD5(String data) {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance(MD5_ALGORITHM);
            bytes = md.digest(data.getBytes());
        } catch (GeneralSecurityException gse) {
            gse.printStackTrace();
        }
        return bytes;
    }

    /**
     * 使用HMAC_MD5算法加密
     * @param data 数据
     * @param secret 密钥
     * @return byte字节流
     */
    public static byte[] encryptHmacMd5(String data, String secret) {
        byte[] bytes = null;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(UTF8), HMAC_MD5_ALGORITHM);
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes(UTF8));
        } catch (GeneralSecurityException | UnsupportedEncodingException gse) {
            gse.printStackTrace();
        }
        return bytes;
    }

    /**
     * 使用HMAC_SHA1算法加密
     * @param data 数据
     * @param secret 密钥
     * @return byte字节流
     */
    public static byte[] encryptHmacSha1(String data, String secret) {
        byte[] bytes = null;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(UTF8), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes(UTF8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
