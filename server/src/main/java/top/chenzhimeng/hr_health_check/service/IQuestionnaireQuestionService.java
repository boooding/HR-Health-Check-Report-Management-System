package top.chenzhimeng.hr_health_check.service;

import top.chenzhimeng.hr_health_check.model.dto.QuestionnaireQuestionAddDTO;
import top.chenzhimeng.hr_health_check.model.dto.QuestionnaireQuestionDTO;
import top.chenzhimeng.hr_health_check.model.dto.QuestionnaireQuestionUpdateDTO;
import top.chenzhimeng.hr_health_check.model.po.QuestionnaireQuestion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author M
 * @date 2021/05/06
 **/
public interface IQuestionnaireQuestionService {
    /**
     * 新增问题，添加到对应页面模块末尾
     */
    void create(List<QuestionnaireQuestionAddDTO> questionnaireQuestionAddDTOList);

    /**
     * 删除问题，若该问题不存在回答（数据库删除）；存在回答（标记删除）
     *
     * @param questionnaireQuestionUuidList 待删除的问题id列表 {@link QuestionnaireQuestion#getUuid()}
     */
    void remove(List<UUID> questionnaireQuestionUuidList);

    /**
     * 更新问题，问题的合法性需在外部检验
     */
    void change(List<QuestionnaireQuestionUpdateDTO> questionnaireQuestionUpdateDTOList);

    List<QuestionnaireQuestionDTO> getByQuestionnaireIdAndDeleted(UUID questionnaireUuid, Boolean deleted);

    Optional<QuestionnaireQuestionDTO> getByQuestionnaireIdAndId(UUID questionnaireUuid, UUID uuid);
}
