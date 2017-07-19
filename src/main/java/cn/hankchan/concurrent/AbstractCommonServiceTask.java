package cn.hankchan.concurrent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 对任务进行顺序编排实现的抽象任务类
 * @author hankChan
 * @author chings
 *         2017/7/14 0014.
 */
public abstract class AbstractCommonServiceTask<IN, MID, OUT> implements CommonServiceTask<IN, MID, OUT> {

    protected List<CommonTaskMiddle<IN, MID>> taskMiddles;

    @Override
    public void gather(CommonTaskMiddle<IN, MID> taskMiddle) {
        if(taskMiddles == null) {
            taskMiddles = new ArrayList<>();
        }
        taskMiddles.add(taskMiddle);
        Collections.sort(taskMiddles);
    }

}
