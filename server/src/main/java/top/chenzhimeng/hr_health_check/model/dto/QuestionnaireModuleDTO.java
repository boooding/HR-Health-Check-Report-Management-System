package top.chenzhimeng.hr_health_check.model.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import top.chenzhimeng.hr_health_check.model.interfaces.JsonAble;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.UUID;

/**
 * @author M
 * @date 2021/04/18
 **/
@AllArgsConstructor
public class QuestionnaireModuleDTO implements JsonAble {
    @NotNull
    @Size(max = 20)
    public String moduleName;
    @NotEmpty
    @Valid
    public List<QuestionDTO> questions;

    @Component
    protected static class QuestionnaireModuleDTOConverter implements Converter<JSONObject, QuestionnaireModuleDTO> {
        @Override
        public QuestionnaireModuleDTO convert(JSONObject jsonObject) {
            return new QuestionnaireModuleDTO(
                    jsonObject.getString("moduleName"),
                    jsonObject.getJSONArray("questions").toJavaList(QuestionDTO.class)
            );
        }
    }

    @AllArgsConstructor
    public static class QuestionDTO implements JsonAble {
        @Null
        public UUID questionUuid;
        @NotBlank
        @Size(max = 50)
        public String question;
        public List<String> options;
    }
}
