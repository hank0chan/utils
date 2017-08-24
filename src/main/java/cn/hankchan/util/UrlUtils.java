package cn.hankchan.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请求URL操作工具类
 * @author hankChan
 *         2017/7/11 0011.
 */
public class UrlUtils {

    static Pattern pattern = Pattern.compile("\\S*[?]\\S*");

    /**
     * 从url中获取文件后缀名
     * @param url URL地址
     * @param defaultType 默认后缀
     * @param legalImgType 合法后缀列表，请使用小写。如：{"xml", "jpg", "json", "wegp"}
     * @return 合法文件后缀名，注意返回值不会包含半角句点.
     */
    public static String getSuffix(String url, String defaultType, List<String> legalImgType) {
        String suffix = "";
        Matcher matcher = pattern.matcher(url);
        // 切割URL
        String[] spUrl = url.toString().split("/");
        int len = spUrl.length;
        // 获取最后一个/之后的字符串
        String endUrl = spUrl[len - 1];

        if(matcher.find()) {
            // url带有请求参数
            String[] spEndUrl = endUrl.split("\\?");
            if(spEndUrl[0].contains(".")) {
                suffix = spEndUrl[0].split("\\.")[1];
                // 判断是否合法图片格式
                return legalImgType.contains(suffix.toLowerCase()) ?
                        suffix.toLowerCase() : defaultType;
            }
            // 没有带图片后缀，返回默认图片格式
            return defaultType;
        }
        // 无请求参数的url
        if(endUrl.contains(".")) {
            // 得到文件后缀名
            suffix = endUrl.split("\\.")[1];
            // 判断是否合法图片格式
            return legalImgType.contains(suffix.toLowerCase()) ?
                    suffix.toLowerCase() : defaultType;
        }
        return defaultType;
    }

    /**
     * 从url中获取包含合法文件后缀的完整文件名
     * @param url 图片URL
     * @param defaultType 默认后缀
     * @param legalImgType 合法的文件后缀列表，请使用小写。如：{"xml", "jpg", "json", "wegp"}
     * @return 包含合法文件后缀的完整文件名
     */
    public static String parseSuffix(String url, String defaultType, List<String> legalImgType) {
        String suffix = "";
        Matcher matcher = pattern.matcher(url);
        // 切割URL
        String[] spUrl = url.toString().split("/");
        int len = spUrl.length;
        // 获取最后一个/之后的字符串
        String endUrl = spUrl[len - 1];

        if(matcher.find()) {
            // url带有请求参数
            String[] spEndUrl = endUrl.split("\\?");
            if(spEndUrl[0].contains(".")) {
                String[] spEndUrlArr = spEndUrl[0].split("\\.");
                suffix = spEndUrlArr[1];
                // 判断是否合法图片格式，拼接文件名
                return legalImgType.contains(suffix.toLowerCase()) ?
                        append(spEndUrlArr[0], suffix.toLowerCase())
                        : append(spEndUrlArr[0], defaultType);
            }
            // 没有带图片后缀，返回默认图片格式，拼接文件名
            return spEndUrl[0] + "." + defaultType;
        }
        // 无请求参数的url
        if(endUrl.contains(".")) {
            String[] endUrlArr = endUrl.split("\\.");
            // 得到文件后缀名
            suffix = endUrlArr[1];
            // 判断是否合法图片格式，拼接文件名
            return legalImgType.contains(suffix.toLowerCase()) ?
                    append(endUrlArr[0], suffix.toLowerCase())
                    : append(endUrlArr[0], defaultType);
        }
        return append(endUrl, defaultType);
    }

    /**
     * 拼接key value
     * @param params 参数集合
     * @return 返回空字符串或者正确形式的url拼接参数格式的字符串
     */
    public static String appendKeyAndValues(Map<String, String> params) {
        if(params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, String> entry : params.entrySet()) {
            if(Strings.areNotEmpty(entry.getKey(), entry.getValue())) {
                try {
                    // 拼接成k1=v1&k2=v2&k3=v3的结果字符串
                    builder.append(entry.getKey()).append("=")
                            .append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        String result = builder.toString();
        return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
    }

    /**
     * 拼接Url和请求参数
     * @param url url
     * @param params 请求参数集合
     * @return 包含请求参数的完整url
     */
    public static String appendUrlWithParams(String url, Map<String, String> params) {
        if(Strings.isNullOrEmpty(url)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        if(url.endsWith("/")) {
            // 如果URI是以/结尾，需要去掉
            builder.append(url.substring(0, url.length() - 1)).append("?");
        } else {
            builder.append(url).append("?");
        }
        String keyValuePairs = appendKeyAndValues(params);
        return builder.append(keyValuePairs).toString();
        /*
        for(Map.Entry<String, String> entry : params.entrySet()) {
            if(Strings.areNotEmpty(entry.getKey(), entry.getValue())) {
                // 最终URL结尾是&符号，需要去除
                try {
                    builder.append(entry.getKey()).append("=")
                            .append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        String fullUrl = builder.toString();
        return fullUrl.length() > 0 ? fullUrl.substring(0, fullUrl.length() - 1) : null;
        */
    }

    /**
     * 拼接标题和后缀
     * @param title 标题
     * @param suffix 后缀
     * @return 形如 "标题.后缀" 的字符串
     */
    private static String append(String title, String suffix) {
        StringBuilder builder = new StringBuilder();
        return builder.append(title).append(".").append(suffix).toString();
    }
}
