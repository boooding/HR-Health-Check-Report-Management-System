package top.chenzhimeng.hr_health_check.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import top.chenzhimeng.hr_health_check.model.dto.CommonResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败时返回 {@code 401} 而不是重定向
 *
 * @author M
 * @date 2021/04/15
 **/
@Slf4j
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException exception) throws IOException {
        HandlerUtil.handle(response, HttpStatus.UNAUTHORIZED, exception.getLocalizedMessage());
    }
}
