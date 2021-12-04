package top.chenzhimeng.hr_health_check.model.dto;

import lombok.AllArgsConstructor;
import top.chenzhimeng.hr_health_check.model.enums.QuestionType;

import java.util.UUID;

/**
 * @author M
 * @date 2021/05/06
 **/
@AllArgsConstructor
public class QuestionnaireQuestionDTO {
    public UUID uuid;

    public UUID questionnaireUuid;

    public Integer page;

    public String module;

    public Integer questionNo;

    public String question;

    public QuestionType questionType;

    public String options;

    public Boolean deleted;

    public Integer getPage() {
        return page;
    }

    public String getModule() {
        return module;
    }
}
