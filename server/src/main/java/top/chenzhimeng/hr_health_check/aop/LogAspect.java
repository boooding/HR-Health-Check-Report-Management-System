package top.chenzhimeng.hr_health_check.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author M
 * @date 2021/04/23
 **/
@Slf4j
@Aspect
@Component
public class LogAspect {
    @Before(value = "execution(* top.chenzhimeng.hr_health_check.controller.*.* (..))")
    protected void log(JoinPoint jp) {
        log.info("Request [{}.{}], with args: {}", jp.getTarget().getClass().getSimpleName(), jp.getSignature().getName(), jp.getArgs());
    }
}
