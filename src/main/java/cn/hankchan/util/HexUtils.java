package cn.hankchan.util;

import cn.hankchan.util.Strings;

/**
 * 16进制转换工具类
 * @author hankChan
 *         2017/7/10 0010.
 */
public class HexUtils {



    /**
     * 16进制字符串转为二进制byte
     * @param hexString 16进制字符串
     * @return 二进制byte
     */
    public static byte[] parseHexString2Byte(String hexString) {
        if(Strings.isNullOrEmpty(hexString)) {
            return null;
        }
        byte[] result = new byte[hexString.length() / 2];
        for(int i = 0; i < hexString.length() / 2; i++) {
            int high = Integer.parseInt(hexString.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexString.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 把二进制转化为大写的十六进制
     * @param bytes 字节流
     * @return 大写的十六进制字符串
     */
    public static String parseByte2UppercaseHexString(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }
}
