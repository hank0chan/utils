package cn.hankchan.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * List工具类
 * @author hankChan
 *         2017/7/7 0007.
 */
public class ListUtils {

    /**
     * 转换
     * @param src 目标对象
     * @param converter 转换函数
     * @param <F> 输入
     * @param <T> 输出
     * @return 新对象
     */
    public static <F, T> List<T> transform(List<? extends F> src, Function<F, T> converter) {
        List<T> result = new ArrayList<>();
        for(F from : src) {
            result.add(converter.apply(from));
        }
        return result;
    }

}
