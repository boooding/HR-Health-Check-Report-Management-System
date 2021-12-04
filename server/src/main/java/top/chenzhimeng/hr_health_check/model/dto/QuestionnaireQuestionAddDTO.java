package top.chenzhimeng.hr_health_check.model.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import top.chenzhimeng.hr_health_check.model.enums.QuestionType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author M
 * @date 2021/05/10
 **/
@AllArgsConstructor
public class QuestionnaireQuestionAddDTO {
    @Null
    public UUID questionnaireUuid;

    @NotNull
    public Integer page;

    @NotNull
    @Size(max = 20)
    public String module;

    @NotNull
    public Integer questionNo;

    @NotBlank
    @Size(max = 50)
    public String question;

    @NotNull
    public QuestionType questionType;

    public List<String> options;

    @Component
    protected static class QuestionnaireQuestionAddDTOConverter
            implements Converter<JSONObject, QuestionnaireQuestionAddDTO> {
        @Override
        public QuestionnaireQuestionAddDTO convert(JSONObject source) {
            return new QuestionnaireQuestionAddDTO(
                    null,
                    source.getInteger("page"),
                    source.getString("module"),
                    source.getInteger("questionNo"),
                    source.getString("question"),
                    QuestionType.valueOf(source.getString("questionType")),
                    Optional.ofNullable(source.getJSONArray("options"))
                            .map(jsonArray -> jsonArray.toJavaList(String.class))
                            .orElse(null)
            );
        }
    }
}
