package top.chenzhimeng.hr_health_check.config.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import top.chenzhimeng.hr_health_check.model.dto.CommonResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author M
 * @date 2021/04/20
 **/
public class HandlerUtil {
    public static void handle(HttpServletResponse response, HttpStatus httpStatus, String msg) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(
                new CommonResult(httpStatus, msg).toJsonString());
    }
}
