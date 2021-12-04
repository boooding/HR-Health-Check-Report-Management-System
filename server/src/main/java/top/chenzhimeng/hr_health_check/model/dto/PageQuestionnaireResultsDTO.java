package top.chenzhimeng.hr_health_check.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import top.chenzhimeng.hr_health_check.annotation.JsonArg;
import top.chenzhimeng.hr_health_check.model.interfaces.JsonAble;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * @author M
 * @date 2021/04/17
 **/
@AllArgsConstructor
public class PageQuestionnaireResultsDTO implements JsonAble {
    public int totalPage;
    public List<QuestionnaireResultDTO> results;

    @AllArgsConstructor
    public static class QuestionnaireResultDTO {
        public String questionnaireName;
        public UUID uuid;
        public String companyName;
        public String contactName;
        public Instant createTime;
    }
}
