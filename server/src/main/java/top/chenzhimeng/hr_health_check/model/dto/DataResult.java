package top.chenzhimeng.hr_health_check.model.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author M
 * @date 2021/04/14
 **/
public class DataResult<T> extends CommonResult {
    public T data;

    public DataResult(T data) {
        super();
        this.data = data;
    }

    public DataResult(HttpStatus httpStatus, String msg, T data) {
        super(httpStatus, msg);
        this.data = data;
    }
}
