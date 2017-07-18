package cn.hankchan.util;

import cn.hankchan.ext.AESConfig;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * AES加密算法工具类
 * <p>在加密码时，要注意加密只能用字符串或数据加密码,不能使用二进制加密</p>
 * <p>使用前调用{@link AESConfig }初始化AES算法的加密种子字符串，该配置类是单例模式，只需要初始化一次</p>
 * @author hankChan
 *         2017/7/10 0010.
 */
public class AESUtils {

    // AES加密算法的种子
    static final String MASTER_PASSWORD = AESConfig.getInstance().getPassword();
    static final String AES = "AES";
    static final String SHA1PRNG = "SHA1PRNG";
    static final String UTF8 = "UTF-8";

    /**
     * 解密文本
     * @param content 文本
     * @return 解密结果
     * @throws Exception exception
     */
    public static String decrypt(String content) throws Exception {
        return decrypt(content, MASTER_PASSWORD);
    }

    /**
     * 加密文本
     * @param content 文本
     * @return 加密结果
     * @throws Exception exception
     */
    public static String encrypt(String content) throws Exception {
        return encrypt(content, MASTER_PASSWORD);
    }

    /**
     * 使用指定密码对文本进行AES加密
     * @param content 文本
     * @param password 密码
     * @return 加密结果
     * @throws Exception exception
     */
    private static String decrypt(String content, String password) throws Exception {
        byte[] bytes = HexUtils.parseHexString2Byte(content);
        bytes = decryptByte(bytes, password);
        return new String(bytes);
    }

    /**
     * 使用指定密码对二进制文本进行AES加密
     * @param content 二进制文本
     * @param password 密码
     * @return 加密结果
     * @throws Exception exception
     */
    private static byte[] decryptByte(byte[] content, String password) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        SecureRandom secureRandom = SecureRandom.getInstance(SHA1PRNG);
        secureRandom.setSeed(password.getBytes());
        keyGenerator.init(128, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();

        byte[] encodeFormat = secretKey.getEncoded();
        SecretKeySpec keySpec = new SecretKeySpec(encodeFormat, AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(content);
    }

    /**
     * 使用指定密码对文本进行AES解密
     * @param content 文本
     * @param password 密码
     * @return 解密结果
     * @throws Exception exception
     */
    private static String encrypt(String content, String password) throws Exception {
        byte[] bytes = encryptByte(content, password);
        return HexUtils.parseByte2UppercaseHexString(bytes);
    }

    /**
     * 使用指定密码对文本进行AES解密
     * @param content 文本
     * @param password 密码
     * @return 二进制解密结果
     * @throws Exception
     */
    private static byte[] encryptByte(String content, String password) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        SecureRandom secureRandom = SecureRandom.getInstance(SHA1PRNG);
        secureRandom.setSeed(password.getBytes());
        keyGenerator.init(128, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();

        byte[] encodeFormat = secretKey.getEncoded();
        SecretKeySpec keySpec = new SecretKeySpec(encodeFormat, AES);
        // 创建密码器
        Cipher cipher = Cipher.getInstance(AES);
        byte[] contentBytes = content.getBytes(UTF8);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        return cipher.doFinal(contentBytes);
    }
}
