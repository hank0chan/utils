package cn.hankchan.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 请求URL操作工具类
 * @author hankChan
 *         2017/7/11 0011.
 */
public class UrlUtils {

    /**
     * 拼接Url和请求参数：http://localhost:8080/api?a=1&b=2
      * @param url url
     * @param params 请求参数集合
     * @return 包含请求参数的完整url
     */
    public static String appendUrlWithParams(String url, Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        if(url.endsWith("/")) {
            // 如果URI是以/结尾，需要去掉
            builder.append(url.substring(0, url.length() - 1)).append("?");
        } else {
            builder.append(url).append("?");
        }

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
        return fullUrl.substring(0, fullUrl.length() - 1);
    }
}
