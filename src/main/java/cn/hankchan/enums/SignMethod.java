package cn.hankchan.enums;

/**
 * 签名方法枚举类
 * @author hankChan
 *         2017/7/7 0007.
 */
public enum SignMethod {
    /**
     * MD5算法
     */
    MD5,
    /**
     * Hmac-MD5算法
     */
    HMAC_MD5,
    /**
     * Hmac_Sha1算法
     */
    HMAC_SHA1,
    /**
     * 无需签名
     */
    UN_SIGNATURE;
}
