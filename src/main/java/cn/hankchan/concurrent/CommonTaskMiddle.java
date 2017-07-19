package cn.hankchan.concurrent;

/**
 * 公共并发任务处理框架之中间处理结果类
 * @author hankChan
 * @author chings
 *         2017/7/14 0014.
 */
public class CommonTaskMiddle<IN, OUT> implements Comparable<CommonTaskMiddle<IN, OUT>> {

    int index;
    int from;
    int to;

    IN input;
    OUT output;

    @Override
    public int compareTo(CommonTaskMiddle<IN, OUT> o) {
        return new Integer(index).compareTo(o.index);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public IN getInput() {
        return input;
    }

    public void setInput(IN input) {
        this.input = input;
    }

    public OUT getOutput() {
        return output;
    }

    public void setOutput(OUT output) {
        this.output = output;
    }

}
