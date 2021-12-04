package top.chenzhimeng.hr_health_check.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.chenzhimeng.hr_health_check.model.po.QuestionnaireQuestion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author M
 * @date 2021/04/16
 **/
@Repository
public interface QuestionnaireQuestionRepo extends JpaRepository<QuestionnaireQuestion, UUID> {
    void deleteByQuestionnaireUuid(UUID questionnaireUuid);

    List<QuestionnaireQuestion> findByQuestionnaireUuid(UUID questionnaireUuid);

    List<QuestionnaireQuestion> findByQuestionnaireUuidAndDeleted(UUID questionnaireUuid, Boolean deleted);

    Optional<QuestionnaireQuestion> findByQuestionnaireUuidAndUuid(UUID questionnaireUuid, UUID uuid);
}
