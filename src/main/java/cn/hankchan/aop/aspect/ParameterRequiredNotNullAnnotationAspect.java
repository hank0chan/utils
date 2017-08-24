package cn.hankchan.aop.aspect;

import cn.hankchan.aop.annotation.ParameterRequiredNotNull;
import cn.hankchan.exception.ParameterRequiredNotNullException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 方法入参不得为空的切面实现
 * @author hankChan
 *         2017/7/17 0017.
 */
@Aspect
@Component
public class ParameterRequiredNotNullAnnotationAspect {

    @Before("@annotation(cn.hankchan.aop.annotation.ParameterRequiredNotNull)")
    public void before(JoinPoint jp) {
        Object[] args = jp.getArgs();
        String[] parameterNames = getParameterNames(jp);
        ParameterRequiredNotNull annotation = getAnnotation(jp, ParameterRequiredNotNull.class);
        int[] excepts = annotation.except();
        int index = 0;
        for(Object arg : args) {
            // 排除的参数索引不包括当前索引，则继续判断是否为null
            if (Arrays.binarySearch(excepts, index) < 0) {
                if (arg == null) {
                    String parameterName = parameterNames[index];
                    // 抛出异常，方法入参不得为null
                    throw new ParameterRequiredNotNullException(parameterName);
                }
            }
            index++;
        }
    }

    private <T extends Annotation> T getAnnotation(JoinPoint pjp, Class<T> clazz) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(clazz);
    }

    private String[] getParameterNames(JoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        return signature.getParameterNames();
    }
}
