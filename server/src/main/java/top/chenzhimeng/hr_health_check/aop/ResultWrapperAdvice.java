package top.chenzhimeng.hr_health_check.aop;

import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.chenzhimeng.hr_health_check.annotation.SensitiveClass;
import top.chenzhimeng.hr_health_check.annotation.SensitiveInfo;
import top.chenzhimeng.hr_health_check.model.dto.CommonResult;
import top.chenzhimeng.hr_health_check.model.dto.DataResult;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author M
 * @date 2021/04/16
 **/
@ControllerAdvice(basePackages = "top.chenzhimeng.hr_health_check.controller")
public class ResultWrapperAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter parameter, Class converterType) {
        return true;
    }

    @Override
    @SneakyThrows
    public CommonResult beforeBodyWrite(Object body, MethodParameter parameter, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (Objects.requireNonNull(parameter.getMethod()).getReturnType().getName()
                .equalsIgnoreCase("void")) {
            return new CommonResult(HttpStatus.OK, "");
        }

        List<String> currentRoles = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        eraseSensitiveInfo(body, currentRoles);
        return new DataResult<>(body);
    }

    /**
     * 根据当前用户身份擦除敏感信息
     * <p>
     * {@link List} 类型的对象将被 {@code foreach} 执行
     * {@link SensitiveInfo} 注释的字段将被决定是否设为 {@code null}
     * 其余字段将被递归执行
     *
     * @param obj 将被处理的对象
     */
    private static void eraseSensitiveInfo(Object obj, List<String> currentRoles) throws IllegalAccessException {
        if (obj == null) return;

        if (obj instanceof List) {
            for (Object item : (List<?>) obj) {
                eraseSensitiveInfo(item, currentRoles);
            }
            return;
        }

        // TODO: 2021/4/20 处理其他类型，用到了再写
        Class<?> clazz = obj.getClass();
        if (!clazz.isAnnotationPresent(SensitiveClass.class)) return;

        for (Field field : clazz.getFields()) {
            if (field.isAnnotationPresent(SensitiveInfo.class)) {
                Set<String> allowRoleSet = new HashSet<>(Arrays.asList(field.getAnnotation(SensitiveInfo.class).roles()));
                allowRoleSet.retainAll(currentRoles);
                if (allowRoleSet.isEmpty()) {
                    field.set(obj, null);
                }
                continue;
            }

            eraseSensitiveInfo(field.get(obj), currentRoles);
        }
    }
}
