package cn.hankchan.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 全局配置文件读取工具类（单例）
 * <p>配置文件为根目录下的global.properties</p>
 * @author hankChan
 *         2017/8/11 0011.
 */
public class GlobalConfigUtils {

    private Map<String, String> configs;

    /**
     * 获取所有配置信息
     * @return 配置集合
     */
    public Map<String, String> getConfigs() {
        return configs;
    }

    private static final String GLOBAL_CONFIG_LOCATION = "/global.properties";

    private static class GlobalConfigUtilsHolder {
        private static GlobalConfigUtils instance = new GlobalConfigUtils();
    }
    public static GlobalConfigUtils getInstance() {
        return GlobalConfigUtilsHolder.instance;
    }
    private GlobalConfigUtils() {
        configs = new HashMap<>();
        loadConfig();
    }

    /**
     * 重载配置
     */
    public void reload() {
        configs.clear();
        loadConfig();
    }

    /**
     * 根据配置文件Key获取Value
     * @param key key值
     * @return 存在返回Value值，否则返回null
     */
    public String get(String key) {
        return configs.containsKey(key) ? configs.get(key) : null;
    }

    /**
     * 载入配置内容
     */
    private void loadConfig() {
        // 读取全局配置文件
        Properties props = new Properties();
        // 载入Redis配置文件
        try {
            props.load(GlobalConfigUtils.class.getResourceAsStream(GLOBAL_CONFIG_LOCATION));
            // 获取配置文件key-value
            for(Object keys : props.keySet()) {
                String value = (String) props.get(keys);
                // 载入到configs中
                configs.put(keys.toString(), value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
