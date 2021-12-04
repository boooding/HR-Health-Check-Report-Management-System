package top.chenzhimeng.hr_health_check.model.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import top.chenzhimeng.hr_health_check.model.interfaces.JsonAble;

/**
 * @author M
 * @date 2021/04/14
 **/
public class CommonResult implements JsonAble {
    public int code;
    public String msg;

    public CommonResult() {
        this(HttpStatus.OK, "");
    }

    public CommonResult(HttpStatus httpStatus, String msg) {
        this.code = httpStatus.value();
        this.msg = msg;
    }
}
