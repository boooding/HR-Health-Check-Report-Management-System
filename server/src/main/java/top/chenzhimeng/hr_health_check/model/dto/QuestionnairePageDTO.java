package top.chenzhimeng.hr_health_check.model.dto;

import lombok.AllArgsConstructor;

import java.util.List;

/**
 * @author M
 * @date 2021/05/30
 **/
@AllArgsConstructor
public class QuestionnairePageDTO {
    public Integer page;
    public Integer moduleNum;
    public List<QuestionnaireModuleDTO> questionnaireModules;
}
