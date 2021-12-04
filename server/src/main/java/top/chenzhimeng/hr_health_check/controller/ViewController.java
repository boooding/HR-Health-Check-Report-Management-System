package top.chenzhimeng.hr_health_check.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author M
 * @date 2021/04/07
 **/
@Slf4j
@Controller
public class ViewController {
    @GetMapping(value = "/page/**")
    public String page(HttpServletRequest request) {
        String page = request.getRequestURI().replace("/page/", "").replaceAll("/$", "");
        log.info("Get page: {}", page);
        return page;
    }
}
