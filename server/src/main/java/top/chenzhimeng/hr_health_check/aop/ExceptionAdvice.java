package top.chenzhimeng.hr_health_check.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import top.chenzhimeng.hr_health_check.exception.CustomException;
import top.chenzhimeng.hr_health_check.model.dto.CommonResult;

import javax.validation.ConstraintViolationException;

/**
 * 统一进行异常处理
 *
 * @Author Zhimeng Chen
 * @Date 2021-02-10 13:45
 **/

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<CommonResult> handleCustomException(CustomException ex) {
        return buildResponse(ex.getHttpStatus(), ex.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<CommonResult> handleMethodNotSupportException(HttpRequestMethodNotSupportedException ex) {
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, ServletRequestBindingException.class,
            ConstraintViolationException.class})
    protected ResponseEntity<CommonResult> handleBadRequest(Exception ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private static ResponseEntity<CommonResult> buildResponse(HttpStatus httpStatus, String msg) {
        log.error(msg);
        return new ResponseEntity<>(
                new CommonResult(httpStatus, msg),
                httpStatus
        );
    }
}
