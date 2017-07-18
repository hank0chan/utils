package cn.hankchan.util;

import cn.hankchan.ext.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Base64编码工具类
 * @author hankChan
 *         09/07/2017.
 */
public class Base64Utils {

    /**
     * 使用base64解码
     * @param str 目标字符串
     * @return 成功返回byte结果，否则null
     */
    public static byte[] base64Decode(String str) {
        return Base64.isBase64Value(str) ? Base64.decode(str) : null;
    }

    /**
     * 使用Base64编码，默认UTF-8
     * @param src 目标数据
     * @return 编码结果
     */
    public static String base64Encode(byte[] src) {
        try {
            return base64Encode(src, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 使用Base64编码
     * @param src 目标数据
     * @param charsetName 字符类型
     * @return 编码结果
     * @throws UnsupportedEncodingException exception
     */
    public static String base64Encode(byte[] src, String charsetName) throws UnsupportedEncodingException {
        byte[] res = Base64.encodeToByte(src, false);
        return (src != null ? new String(res, charsetName) : null);
    }
}
