package top.chenzhimeng.hr_health_check.model.dto;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * @author M
 * @date 2021/04/21
 **/
@AllArgsConstructor
public class QuestionnaireDetailsDTO {
    public UUID questionnaireUuid;
    public String questionnaireName;
    public String description;
    public Integer pageNum;
    public List<QuestionnairePageDTO> questionnairePages;
}
