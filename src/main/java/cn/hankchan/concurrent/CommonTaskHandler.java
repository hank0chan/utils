package cn.hankchan.concurrent;

import com.google.common.collect.Iterables;

import java.util.concurrent.*;

/**
 * 通用的多线程任务处理框架（已经实现支持任务排序功能）
 * @author hankChan
 *         2017/7/14 0014.
 */
public class CommonTaskHandler {

    public static final int availableProcessors = Runtime.getRuntime().availableProcessors();

    ExecutorService executor;

    /**
     * 默认构造器，默认的线程池核心线程数等于系统所在机器的cup核心数
     */
    public CommonTaskHandler() {
        executor = Executors.newFixedThreadPool(availableProcessors);
    }

    /**
     * 带参构造器
     * @param availableProcessors 线程池的核心线程数
     */
    public CommonTaskHandler(int availableProcessors) {
        executor = Executors.newFixedThreadPool(availableProcessors);
    }

    /**
     * 线程池资源回收
     */
    public void shutdown() {
        executor.shutdown();
    }

    /**
     * 并发执行具有超时时间的任务
     * @param task 任务
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @param <IN> 输入
     * @param <MID> 输入计算中间结果
     * @param <OUT> 输出结果
     * @return 输出与输入次序相同的执行任务的结果
     */
    public <IN, MID, OUT> OUT execute(final CommonTask<IN, MID, OUT> task, long timeout, TimeUnit timeUnit) {
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
            try {
                if(timeout == -1) {
                    for (int i = 0; i < tasks; i++) {
                        // 不需要超时处理
                        CommonTaskMiddle<IN, MID> taskMiddle = completionService.take().get();
                        synchronized (task) {
                            task.gather(taskMiddle);
                        }
                    }
                } else {
                    for (int i = 0; i < tasks; i++) {
                        // 需要超时处理
                        Future<CommonTaskMiddle<IN, MID>> taskMiddleFuture =
                                completionService.poll(timeout, timeUnit);
                        if (taskMiddleFuture == null) {
                            // 任务处理超时，抛出异常
                            throw new TimeoutException("WARN... the task execute out of time!");
                        }
                        CommonTaskMiddle<IN, MID> taskMiddle = taskMiddleFuture.get();
                        synchronized (task) {
                            task.gather(taskMiddle);
                        }
                    }
                }
            } catch (TimeoutException e) {
                // 如果任务处理超时，在此处进行操作
                for(int i = 0; i < tasks; i++) {
                    Future<CommonTaskMiddle<IN, MID>> timeoutTaskMiddleFuture = completionService.poll();
                    if(timeoutTaskMiddleFuture != null) {
                        if(timeoutTaskMiddleFuture.isDone()) {
//                            System.out.println("is Done..");
                        }
                        //timeoutTaskMiddleFuture.cancel(true);
                    }
                }
            }
            return task.success();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 并发执行任务
     * @param task 任务
     * @param <IN> 输入
     * @param <MID> 输入计算中间结果
     * @param <OUT> 输出结果
     * @return 输出与输入次序相同的执行任务的结果
     */
    public <IN, MID, OUT> OUT execute(final CommonTask<IN, MID, OUT> task) {
        // 添加了超时功能，但是尚未完备
        return execute(task, -1, null);
        /*try {
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
        }*/
    }
}
