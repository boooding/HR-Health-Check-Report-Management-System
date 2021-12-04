package top.chenzhimeng.hr_health_check.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.chenzhimeng.hr_health_check.model.po.QuestionnaireResult;

import java.util.List;
import java.util.UUID;

/**
 * @author M
 * @date 2021/04/17
 **/
@Repository
public interface QuestionnaireResultRepo extends JpaRepository<QuestionnaireResult, UUID> {
    List<QuestionnaireResult> findByQuestionnaireUuid(UUID questionnaireUuid);

    List<QuestionnaireResult> findByIpAddr(String ipAddr);
}
