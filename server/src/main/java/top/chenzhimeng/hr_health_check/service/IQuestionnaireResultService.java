package top.chenzhimeng.hr_health_check.service;

import org.springframework.data.domain.Sort;
import top.chenzhimeng.hr_health_check.model.dto.PageQuestionnaireResultsDTO;
import top.chenzhimeng.hr_health_check.model.dto.QuestionnaireReportDTO;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author M
 * @date 2021/04/17
 **/
public interface IQuestionnaireResultService {
    /**
     * 排序并分页查看问卷结果
     *
     * @param page 页码
     * @param size 页长
     * @param sort 排序方式
     * @return 带有总页码信息的一页问卷结果 {@link PageQuestionnaireResultsDTO}
     */
    PageQuestionnaireResultsDTO getSortedPage(Integer page, Integer size, Sort sort);

    UUID create(
            UUID questionnaireUuid,
            String companyName,
            String contactName,
            String ipAddr,
            Map<UUID, String> questionAnswerMap
    );

    Optional<QuestionnaireReportDTO> getReportByUuid(UUID uuid);

//    /**
//     * 根据用户 ip 获取其所填写的问卷结果列表 uuid
//     *
//     * @param ipAddress ip 地址
//     * @return 问卷结果 uuid
//     */
//    List<UUID> getUuidByIp(String ipAddress);
}
