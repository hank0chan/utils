package cn.hankchan.concurrent;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用普通单线程测试Demo
 * @author hankChan
 *         2017/7/19 0019.
 */
public class SingleTaskDemo {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        List<Integer> lists = new ArrayList<>();
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            lists.add(i + 1);
        }

        for(Integer list : lists) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result.add(list + 1000);
        }
        System.out.println("take:" + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println(result);
    }
}
