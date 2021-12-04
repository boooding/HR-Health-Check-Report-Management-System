package top.chenzhimeng.hr_health_check.exception;

import org.springframework.http.HttpStatus;

/**
 * @author M
 * @date 2021/04/20
 **/
public class CustomException extends RuntimeException {
    private HttpStatus httpStatus;

    public CustomException(String message) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public CustomException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
