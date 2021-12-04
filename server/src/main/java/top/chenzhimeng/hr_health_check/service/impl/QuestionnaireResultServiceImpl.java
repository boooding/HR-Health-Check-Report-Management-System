package top.chenzhimeng.hr_health_check.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.chenzhimeng.hr_health_check.model.dto.PageQuestionnaireResultsDTO;
import top.chenzhimeng.hr_health_check.model.dto.QuestionnaireReportDTO;
import top.chenzhimeng.hr_health_check.model.enums.QuestionType;
import top.chenzhimeng.hr_health_check.model.po.Questionnaire;
import top.chenzhimeng.hr_health_check.model.po.QuestionnaireQuestion;
import top.chenzhimeng.hr_health_check.model.po.QuestionnaireResult;
import top.chenzhimeng.hr_health_check.model.po.ResultData;
import top.chenzhimeng.hr_health_check.repo.QuestionnaireQuestionRepo;
import top.chenzhimeng.hr_health_check.repo.QuestionnaireRepo;
import top.chenzhimeng.hr_health_check.repo.QuestionnaireResultRepo;
import top.chenzhimeng.hr_health_check.repo.ResultDataRepo;
import top.chenzhimeng.hr_health_check.service.IQuestionnaireResultService;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author M
 * @date 2021/04/17
 **/
@Service
public class QuestionnaireResultServiceImpl implements IQuestionnaireResultService {
    private final QuestionnaireRepo questionnaireRepo;
    private final QuestionnaireResultRepo questionnaireResultRepo;
    private final ResultDataRepo resultDataRepo;
    private final QuestionnaireQuestionRepo questionnaireQuestionRepo;

    public QuestionnaireResultServiceImpl(
            QuestionnaireRepo questionnaireRepo,
            QuestionnaireResultRepo questionnaireResultRepo,
            ResultDataRepo resultDataRepo,
            QuestionnaireQuestionRepo questionnaireQuestionRepo
    ) {
        this.questionnaireRepo = questionnaireRepo;
        this.questionnaireResultRepo = questionnaireResultRepo;
        this.resultDataRepo = resultDataRepo;
        this.questionnaireQuestionRepo = questionnaireQuestionRepo;
    }

    @Override
    @Transactional
    public UUID create(
            UUID questionnaireUuid,
            String companyName,
            String contactName,
            String ipAddr,
            Map<UUID, String> questionAnswerMap
    ) {
        QuestionnaireResult questionnaireResult = new QuestionnaireResult(
                questionnaireUuid,
                companyName,
                contactName,
                ipAddr
        );
        questionnaireResultRepo.saveAndFlush(questionnaireResult);

        UUID resultUuid = questionnaireResult.getUuid();
        resultDataRepo.saveAll(
                questionAnswerMap.entrySet()
                        .stream()
                        .map(entry -> new ResultData(
                                resultUuid,
                                entry.getKey(),
                                entry.getValue()
                        ))
                        .collect(Collectors.toList())
        );
        return resultUuid;
    }

    @Override
    public PageQuestionnaireResultsDTO getSortedPage(Integer page, Integer size, Sort sort) {
        int totalPages = 1;
        Stream<QuestionnaireResult> questionnaireResultStream;
        if (page == null || size == null) {
            questionnaireResultStream = questionnaireResultRepo.findAll(sort).stream();
        } else {
            Page<QuestionnaireResult> questionnaireResultPage = questionnaireResultRepo.findAll(PageRequest.of(page, size, sort));
            totalPages = questionnaireResultPage.getTotalPages();
            questionnaireResultStream = questionnaireResultPage.get();
        }

        return new PageQuestionnaireResultsDTO(
                totalPages,
                questionnaireResultStream.map(questionnaireResult -> new PageQuestionnaireResultsDTO.QuestionnaireResultDTO(
                        questionnaireRepo.findById(questionnaireResult.getQuestionnaireUuid())
                                .map(Questionnaire::getQuestionnaireName).orElse(null),
                        questionnaireResult.getUuid(),
                        questionnaireResult.getCompanyName(),
                        questionnaireResult.getContactName(),
                        questionnaireResult.getCreateTime()
                )).collect(Collectors.toList())
        );
    }


    @Override
    public Optional<QuestionnaireReportDTO> getReportByUuid(UUID resultUuid) {
        return questionnaireResultRepo.findById(resultUuid).map(questionnaireResult -> {
                    /*
                        按模块分组
                     */
                    Map<String, List<QuestionnaireReportDTO.ModuleReportDTO.QuestionDataDTO>> groupByModule =
                            resultDataRepo.findByResultUuid(resultUuid).stream().map(resultData -> {
                                Optional<QuestionnaireQuestion> questionnaireQuestionOptional =
                                        questionnaireQuestionRepo.findById(resultData.getQuestionUuid());
                                return new QuestionnaireReportDTO.ModuleReportDTO.QuestionDataDTO(
                                        questionnaireQuestionOptional.map(QuestionnaireQuestion::getModule)
                                                .orElse(null),
                                        questionnaireQuestionOptional.map(QuestionnaireQuestion::getQuestion)
                                                .orElse(null),
                                        resultData.getContent(),
                                        questionnaireQuestionOptional.map(QuestionnaireQuestion::getQuestionNo)
                                                .orElse(null),
                                        questionnaireQuestionOptional.map(QuestionnaireQuestion::getQuestionType)
                                                .orElse(QuestionType.TEXT)
                                                == QuestionType.RADIO
                                                ? Float.parseFloat(resultData.getContent()) * 33
                                                : null
                                );
                            })
                                    .sorted(Comparator.comparingInt(questionDataDTO -> questionDataDTO.questionNo))
                                    .collect(Collectors.groupingBy(questionDataDTO -> questionDataDTO.moduleName));

                    /*
                        问卷头
                     */
                    List<QuestionnaireReportDTO.ModuleReportDTO.QuestionDataDTO> header = groupByModule.get("");

                    /*
                        各模块计算
                     */
                    groupByModule.remove("");
                    List<QuestionnaireReportDTO.ModuleReportDTO> moduleReportDTOList = groupByModule.entrySet().stream()
                            .map(entry -> new QuestionnaireReportDTO.ModuleReportDTO(
                                    entry.getKey(),
                                    entry.getValue().stream().map(questionDataDTO -> questionDataDTO.score)
                                            .reduce(0f, Float::sum) / entry.getValue().size(),
                                    entry.getValue()
                            ))
                            .sorted(Comparator.comparingInt(
                                    moduleReportDTO -> moduleReportDTO.questionData.get(0).questionNo)
                            )
                            .collect(Collectors.toList());

                    /*
                        返回report
                     */
                    return new QuestionnaireReportDTO(
                            questionnaireResult.getQuestionnaireUuid(),
                            questionnaireRepo.findById(questionnaireResult.getQuestionnaireUuid())
                                    .map(Questionnaire::getQuestionnaireName)
                                    .orElse(null),
                            resultUuid,
                            questionnaireResult.getCompanyName(),
                            questionnaireResult.getContactName(),
                            questionnaireResult.getCreateTime(),
                            header.stream()
                                    .sorted(Comparator.comparingInt(questionDataDTO -> questionDataDTO.questionNo))
                                    .collect(Collectors.toMap(
                                            QuestionnaireReportDTO.ModuleReportDTO.QuestionDataDTO::getQuestion,
                                            QuestionnaireReportDTO.ModuleReportDTO.QuestionDataDTO::getContent,
                                            (v1, v2) -> v2,
                                            LinkedHashMap::new
                                    )),
                            moduleReportDTOList.stream()
                                    .map(moduleReportDTO -> moduleReportDTO.moduleScore)
                                    .reduce(0f, Float::sum) / moduleReportDTOList.size(),
                            moduleReportDTOList
                    );
                }
        );
    }

//    @Override
//    public List<UUID> getUuidByIp(String ipAddress) {
//        return questionnaireResultRepo.findByIpAddr(ipAddress).stream()
//                .map(QuestionnaireResult::getUuid)
//                .co;
//    }
}
