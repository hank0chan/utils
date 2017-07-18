package cn.hankchan.util;

/**
 * 格式工具类
 * @author hankChan
 *         2017/7/14 0014.
 */
public class FormatUtils {

    /**
     * 替换参数占位符
     * <p>在目标字符串中，使用两个大括号“{}”作为一个占位符</p>
     * @param targetString 包含请求占位符的目标字符串
     * @param args 替换占位符的参数值
     * @return 结果字符串
     */
    public static String format(String targetString, String ... args) {
        return String.format(targetString.replace("{}", "%s"), args);
    }
}
