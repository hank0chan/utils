package cn.hankchan.exception;

/**
 * 自定义异常，请求参数值不得为null
 * @author hankChan
 *         2017/7/17 0017.
 */
public class ParameterRequiredNotNullException extends RuntimeException {

    public ParameterRequiredNotNullException(String parameterName) {
        super("目标方法输入参数不得为null，该参数名是：" + parameterName);
    }

    public ParameterRequiredNotNullException() {
        // 抛出异常，方法入参不得为null
        super("目标方法必须参数不得为null");
    }

}
