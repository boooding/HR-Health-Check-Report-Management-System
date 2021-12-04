package top.chenzhimeng.hr_health_check.config.mvc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
import org.springframework.web.util.ContentCachingRequestWrapper;
import top.chenzhimeng.hr_health_check.annotation.JsonArg;
import top.chenzhimeng.hr_health_check.model.interfaces.JsonAble;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.stream.Collectors;

/**
 * @Author Zhimeng Chen
 * @Date 2021-02-08 15:35
 **/
@Slf4j
@Component
public class JsonArgMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {
    /**
     * 表示关于 namedValue 的信息，包括名称、是否需要它以及默认值。
     *
     * @param parameter 待处理的方法参数
     * @return {@link JsonArgNamedValueInfo}
     */
    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        JsonArg ann = parameter.getParameterAnnotation(JsonArg.class);
        return (ann != null ? new JsonArgNamedValueInfo(ann) : new JsonArgNamedValueInfo());
    }

    /**
     * 解析方法
     *
     * @param name      待解析的 JSON 的 key
     * @param parameter 待处理的方法参数
     * @return 解析出来的值
     */
    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        ContentCachingRequestWrapper contentCachingRequestWrapper = request.getNativeRequest(ContentCachingRequestWrapper.class);
        String body;
        assert contentCachingRequestWrapper != null;
        byte[] contentAsByteArray = contentCachingRequestWrapper.getContentAsByteArray();
        if (contentAsByteArray.length == 0) {
            try (BufferedReader reader = contentCachingRequestWrapper.getReader()) {
                body = reader.lines().collect(Collectors.joining(""));
            }
        } else {
            body = new String(contentAsByteArray);
        }
        return JSON.parseObject(body).get(name);
    }

    /**
     * 是否支持该方法参数
     *
     * @param parameter 待处理的方法参数
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonArg.class);
    }

    private static class JsonArgNamedValueInfo extends NamedValueInfo {
        public JsonArgNamedValueInfo() {
            super("", false, ValueConstants.DEFAULT_NONE);
        }

        public JsonArgNamedValueInfo(JsonArg annotation) {
            super(annotation.name(), annotation.required(), annotation.defaultValue());
        }
    }
}
