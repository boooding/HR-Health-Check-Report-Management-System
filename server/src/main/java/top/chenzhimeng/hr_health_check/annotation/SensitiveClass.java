package top.chenzhimeng.hr_health_check.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指示某个类下是否有被 {@link SensitiveInfo} 注释的敏感字段
 *
 * @author M
 * @date 2021/04/20
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SensitiveClass {
}
