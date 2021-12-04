package top.chenzhimeng.hr_health_check.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import top.chenzhimeng.hr_health_check.exception.CustomException;

import javax.validation.ConstraintViolationException;

/**
 * @author M
 * @date 2021/04/20
 **/
@Aspect
@Component
public class CustomExceptionAspect {
    @Around(value = "execution(public * top.chenzhimeng.hr_health_check.controller.*.*(..))")
    protected Object unitaryControllerException(ProceedingJoinPoint pjp) {
        try {
            return pjp.proceed();
        } catch (CustomException | ConstraintViolationException ex) {
            throw ex;
        } catch (Throwable throwable) {
            throw new CustomException(throwable.getMessage());
        }
    }
}
