package cn.hankchan.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被修饰的方法，其入参不得为null的注解
 * <p>默认所有的参数都不得为null。可以设置参数值指定允许为空的参数索引，索引值从0开始</p>
 * <h1>注意，这是基于Spring AOP实现，在使用时修饰的目标方法必须是SpringBeans对象的方法，否则不会生效</h1>
 * @author hankChan
 *         2017/7/17 0017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ParameterRequiredNotNull {

    /**
     * 将该索引指定的参数值排除在不为null的限制中，
     * 即该索引指定的值可以为null。索引值从0开始
     * @return 允许为null的参数索引
     */
    int[] except() default {};

}
