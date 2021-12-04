package top.chenzhimeng.hr_health_check.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指示某个字段是否敏感需要被保护。
 * 若当前用户身份权限不足，则会在返回前擦除字段值。
 * 类需要被 {@link SensitiveClass} 注释才有效,
 * 被注释的字段必须是引用类型
 *
 * @author M
 * @date 2021/04/20
 * @see top.chenzhimeng.hr_health_check.aop.ResultWrapperAdvice
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SensitiveInfo {
    /**
     * 需要什么身份才能查看
     */
    String[] roles() default {"ROLE_ADMIN"};
}
