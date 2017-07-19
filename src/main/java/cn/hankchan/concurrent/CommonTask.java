package cn.hankchan.concurrent;

/**
 * 任务接口类
 * @author hankChan
 * @author chings
 *         2017/7/14 0014.
 */
public interface CommonTask<IN, MID, OUT> extends Iterable<IN> {

    /**
     * 执行输入获得中间结果
     * @param in 输入
     * @return 中间结果
     */
    public MID execute(IN in);

    /**
     * 对输入和中间结果聚合
     * @param taskMiddle 任务处理中间结果类
     */
    public void gather(CommonTaskMiddle<IN, MID> taskMiddle);

    /**
     * 成功返回结果
     * @return 输出
     */
    public OUT success();

}
