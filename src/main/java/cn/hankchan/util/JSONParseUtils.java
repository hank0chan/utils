package cn.hankchan.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * JSON转换工具类
 * @author hankChan
 *         2017/7/10 0010.
 */
public class JSONParseUtils {

    private static ObjectMapper mapper;

    /**
     * 将对象转换为JSON格式的字符串
     * @param obj 目标对象
     * @return 返回JSON格式字符串，或者null
     */
    public static String object2JsonString(Object obj) {
        if(obj == null) {
            return null;
        }
        if(mapper == null) {
            mapper = new ObjectMapper();
        }
        String result = null;
        try {
            result = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将JSON字符串还原为目标对象
     * @param jsonString JSON字符串
     * @param resultType 目标对象类型
     * @param <T> 目标对象类型
     * @return 成功则目标对象，否则返回null
     */
    public static <T> T json2Object(String jsonString, Class<T> resultType) {
        if(Strings.isNullOrEmpty(jsonString)) {
            return null;
        }
        if(mapper == null) {
            mapper = new ObjectMapper();
        }
        T result = null;
        try {
            result = mapper.readValue(jsonString, resultType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
