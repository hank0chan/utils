package cn.hankchan.concurrent;

import com.google.common.collect.Iterables;

import java.util.concurrent.*;

/**
 * 通用的多线程任务处理框架（已经实现支持任务排序功能）
 * @author hankChan
 * @author chings
 *         2017/7/14 0014.
 */
public class CommonTaskHandler {

    public static final int availableProcessors = Runtime.getRuntime().availableProcessors();

    ExecutorService executor;

    public CommonTaskHandler() {
        executor = Executors.newFixedThreadPool(availableProcessors);
    }

    public void shutdown() {
        executor.shutdown();
    }

    /**
     * 执行任务
     * @param task 任务
     * @param <IN> 输入
     * @param <MID> 输入计算中间结果
     * @param <OUT> 输出结果
     * @return 输出执行任务的结果
     */
    public <IN, MID, OUT> OUT execute(final CommonTask<IN, MID, OUT> task) {
        try {
            CompletionService<CommonTaskMiddle<IN, MID>> completionService =
                    new ExecutorCompletionService<CommonTaskMiddle<IN, MID>>(executor);

            int tasks = 0, taskFrom = 0;

            for (IN taskInput : task) {
                final int index = tasks;
                final int from = taskFrom;
                final int to = from + (taskInput instanceof Iterable ?
                        Iterables.size((Iterable<?>) taskInput) : 1);
                final IN input = taskInput;
                completionService.submit(new Callable<CommonTaskMiddle<IN, MID>>() {
                    @Override
                    public CommonTaskMiddle<IN, MID> call() throws Exception {
                        CommonTaskMiddle<IN, MID> taskMiddle = new CommonTaskMiddle<>();
                        // 对任务进行标记编号
                        taskMiddle.setIndex(index);
                        taskMiddle.setFrom(from);
                        taskMiddle.setTo(to);
                        taskMiddle.setInput(input);
                        // 提交执行具体的任务
                        taskMiddle.setOutput(task.execute(input));
                        return taskMiddle;
                    }
                });
                tasks++;
                taskFrom = to;
            }

            for (int i = 0; i < tasks; i++) {
                CommonTaskMiddle<IN, MID> taskMiddle = completionService.take().get();
                synchronized (task) {
                    task.gather(taskMiddle);
                }
            }
            return task.success();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
