package cn.hankchan.util;

import org.junit.Test;

/**
 * @author hankChan
 *         24/08/2017.
 */
public class SnowflakeIdWorkerTest {

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        SnowflakeIdWorker idWorker0 = new SnowflakeIdWorker(0, 0);
        for (int i = 0; i < 10000; i++) {
            long id = idWorker0.nextId();
            System.out.println(id);
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - start));
    }
}
