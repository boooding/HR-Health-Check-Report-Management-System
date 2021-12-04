package top.chenzhimeng.hr_health_check.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.ValueConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link org.springframework.web.bind.annotation.RequestParam} 的 json 版
 * 处理请求体里的 json 字段
 *
 * @Author Zhimeng Chen
 * @Date 2021-02-08 15:31
 * @see top.chenzhimeng.hr_health_check.config.mvc.JsonArgMethodArgumentResolver
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface JsonArg {
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    boolean required() default true;

    String defaultValue() default ValueConstants.DEFAULT_NONE;
}
