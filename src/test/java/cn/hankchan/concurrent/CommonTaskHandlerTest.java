package cn.hankchan.concurrent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 采用自定义并发框架测试Demo
 * @author hankChan
 *         2017/7/19 0019.
 */
public class CommonTaskHandlerTest {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        List<Integer> lists = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            lists.add(i + 1);
        }
        AbstractCommonTask<Integer, Integer, List<Integer>> task =
                new AbstractCommonTask<Integer, Integer, List<Integer>>() {
            List<Integer> result = new ArrayList<>();
            @Override
            public Integer execute(Integer aLong) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return (aLong + 1000);
            }
            @Override
            public List<Integer> success() {
                for(CommonTaskMiddle<Integer, Integer> middle : taskMiddles) {
                    result.add(middle.getOutput());
                }
                return result;
            }
            @Override
            public Iterator<Integer> iterator() {
                return lists.iterator();
            }
            /*
            // 这是使用自定义的任务排序方式（直接调用任务接口：CommonTask）
            List<CommonTaskMiddle<Integer, Integer>> middles = new ArrayList<>();
            @Override
            public void gather(CommonTaskMiddle<Integer, Integer> taskMiddle) {
                middles.add(taskMiddle);
                Collections.sort(middles);
            }
            */
        };
        CommonTaskHandler handler = new CommonTaskHandler();
        List<Integer> out = handler.execute(task);
        System.out.println("take:" + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println(out);
        handler.shutdown();
    }
}
