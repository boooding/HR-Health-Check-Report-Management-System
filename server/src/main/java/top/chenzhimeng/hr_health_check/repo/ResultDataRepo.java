package top.chenzhimeng.hr_health_check.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import top.chenzhimeng.hr_health_check.model.po.ResultData;

import java.util.List;
import java.util.UUID;

/**
 * @author M
 * @date 2021/04/28
 **/
public interface ResultDataRepo extends JpaRepository<ResultData, UUID> {
    int countByQuestionUuid(UUID questionUuid);

    List<ResultData> findByResultUuid(UUID resultUuid);
}
