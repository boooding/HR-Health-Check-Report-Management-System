package top.chenzhimeng.hr_health_check.model.dto;

import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author M
 * @date 2021/06/01
 **/
@AllArgsConstructor
public class QuestionnaireReportDTO {
    public UUID questionnaireUuid;
    public String questionnaireName;
    public UUID questionnaireResultUuid;
    public String companyName;
    public String contactName;
    public Instant createTime;
    public Map<String, String> header;
    public Float reportScore;
    public List<ModuleReportDTO> moduleReports;

    @AllArgsConstructor
    public static class ModuleReportDTO {
        public String moduleName;
        public Float moduleScore;
        public List<QuestionDataDTO> questionData;

        @AllArgsConstructor
        public static class QuestionDataDTO {
            public String moduleName;
            public String question;
            public String content;
            public Integer questionNo;
            public Float score;

            public String getQuestion() {
                return question;
            }

            public String getContent() {
                return content;
            }
        }
    }
}
