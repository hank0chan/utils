package cn.hankchan.util;

import java.io.IOException;
import java.util.Properties;

/**
 * 全局唯一ID生成工具类
 * <h>需要通过读取根目录下的配置文件global.properties，获取agent.workerId及agent.dataCenterId的值。如下：</h>
 * <p>agent.workerId=0</p>
 * <p>agent.dataCenterId=0</p>
 * 这里两个参数的取值返回都是0-31，如果没有读取到该指定配置文件，默认的该两个值都为0
 * <p>对使用方法存在疑问，可以查看{@link SnowflakeIdWorker}类的说明</p>
 * @author hankChan
 *         2017/8/2 0002.
 */
public class UniqueUtils {

    static SnowflakeIdWorker snowflakeIdWorker;
    static int workerId;
    static int dataCenterId;
    static {
        Properties properties = new Properties();
        try {
            properties.load(UniqueUtils.class.getResourceAsStream("/global.properties"));
            String workerIdProps = properties.getProperty("agent.workerId");
            String dataCenterIdProps = properties.getProperty("agent.dataCenterId");
            workerId = Strings.isNullOrEmpty(workerIdProps) ? 0 : Integer.valueOf(workerIdProps);
            dataCenterId = Strings.isNullOrEmpty(dataCenterIdProps) ? 0 : Integer.valueOf(dataCenterIdProps);
        } catch (IOException e) {
            workerId = 0;
            dataCenterId = 0;
        }
        snowflakeIdWorker = new SnowflakeIdWorker(workerId, dataCenterId);
    }

    private UniqueUtils() {}

	/**
	 * 获取全局唯一ID，支持分布式环境下的唯一
	 * <p>有疑问，联系：hankChan</p>
	 * @return 返回唯一ID
	 */
	public static long getId() {
        return snowflakeIdWorker.nextId();
    }

}
