package top.chenzhimeng.hr_health_check.model.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import top.chenzhimeng.hr_health_check.model.interfaces.JsonAble;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author M
 * @date 2021/05/10
 **/
@AllArgsConstructor
public class QuestionnaireQuestionUpdateDTO implements JsonAble {
    @NotNull
    public UUID uuid;
    @NotBlank
    @Size(max = 50)
    public String question;
    public List<String> options;

    @Component
    protected static class QuestionnaireQuestionUpdateDTOConverter
            implements Converter<JSONObject, QuestionnaireQuestionUpdateDTO> {
        @Override
        public QuestionnaireQuestionUpdateDTO convert(JSONObject source) {
            return new QuestionnaireQuestionUpdateDTO(
                    source.getObject("uuid", UUID.class),
                    source.getString("question"),
                    Optional.ofNullable(source.getJSONArray("options"))
                            .map(jsonArray -> jsonArray.toJavaList(String.class))
                            .orElse(null)
            );
        }
    }
}
