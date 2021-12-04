package top.chenzhimeng.hr_health_check.repo;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.chenzhimeng.hr_health_check.model.po.Questionnaire;

import java.util.List;
import java.util.UUID;

/**
 * @author M
 * @date 2021/04/16
 **/
@Repository
public interface QuestionnaireRepo extends JpaRepository<Questionnaire, UUID> {
    List<Questionnaire> findByServicing(Boolean servicing, Sort sort);
}
