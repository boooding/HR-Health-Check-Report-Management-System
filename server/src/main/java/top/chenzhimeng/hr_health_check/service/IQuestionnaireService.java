package top.chenzhimeng.hr_health_check.service;

import top.chenzhimeng.hr_health_check.model.dto.QuestionnaireDTO;
import top.chenzhimeng.hr_health_check.model.dto.QuestionnaireDetailsDTO;
import top.chenzhimeng.hr_health_check.model.dto.QuestionnaireQuestionAddDTO;
import top.chenzhimeng.hr_health_check.model.po.Questionnaire;
import top.chenzhimeng.hr_health_check.model.po.QuestionnaireQuestion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author M
 * @date 2021/04/15
 **/
public interface IQuestionnaireService {
    /**
     * 创建新问卷
     *
     * @param questionnaireName         问卷名
     * @param description               问卷描述
     * @param questionnaireQuestionList 问卷内容，包括问卷头和问卷正文
     * @return 该问卷的 uuid
     * @see Questionnaire
     * @see QuestionnaireQuestion
     */
    UUID create(String questionnaireName, String description, List<QuestionnaireQuestionAddDTO> questionnaireQuestionList);

    /**
     * 删除一份问卷，但不删除已收集的结果
     * 如果该问卷没有被任何人填写，同时删除其问题列表
     *
     * @param questionnaireUuid 问卷 uuid
     * @see Questionnaire
     * @see QuestionnaireQuestion
     */
    void remove(UUID questionnaireUuid);

    /**
     * null将不会被更新
     *
     * @param uuid 根据 {@link Questionnaire#getUuid()} 更新
     */
    void change(UUID uuid, String name, String description, Boolean servicing);

    /**
     * @param servicing 正在收集或停止收集
     *                  为 {@literal null} 时返回所有
     * @return 所有问卷
     */
    List<QuestionnaireDTO> get(Boolean servicing);

    /**
     * @param questionnaireUuid 根据问卷 uuid 返回问卷
     */
    Optional<QuestionnaireDTO> getById(UUID questionnaireUuid);

    Optional<QuestionnaireDetailsDTO> getDetailsById(UUID questionnaireUuid);
}
