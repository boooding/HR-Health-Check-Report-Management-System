package top.chenzhimeng.hr_health_check.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.chenzhimeng.hr_health_check.model.dto.*;
import top.chenzhimeng.hr_health_check.repo.QuestionnaireRepo;
import top.chenzhimeng.hr_health_check.repo.QuestionnaireQuestionRepo;
import top.chenzhimeng.hr_health_check.model.po.Questionnaire;
import top.chenzhimeng.hr_health_check.model.po.QuestionnaireQuestion;
import top.chenzhimeng.hr_health_check.repo.QuestionnaireResultRepo;
import top.chenzhimeng.hr_health_check.service.IQuestionnaireQuestionService;
import top.chenzhimeng.hr_health_check.service.IQuestionnaireService;
import top.chenzhimeng.hr_health_check.util.UpdateUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author M
 * @date 2021/04/15
 **/
@Service
public class QuestionnaireServiceImpl implements IQuestionnaireService {
    private final QuestionnaireRepo questionnaireRepo;
    private final QuestionnaireQuestionRepo questionnaireQuestionRepo;
    private final QuestionnaireResultRepo questionnaireResultRepo;
    private final IQuestionnaireQuestionService questionnaireQuestionService;

    public QuestionnaireServiceImpl(
            QuestionnaireRepo questionnaireRepo,
            QuestionnaireQuestionRepo questionnaireQuestionRepo,
            QuestionnaireResultRepo questionnaireResultRepo,
            IQuestionnaireQuestionService questionnaireQuestionService
    ) {
        this.questionnaireRepo = questionnaireRepo;
        this.questionnaireQuestionRepo = questionnaireQuestionRepo;
        this.questionnaireResultRepo = questionnaireResultRepo;
        this.questionnaireQuestionService = questionnaireQuestionService;
    }

    @Override
    @Transactional
    public UUID create(
            String questionnaireName,
            String description,
            List<QuestionnaireQuestionAddDTO> questionnaireQuestionAddDTOList
    ) {
        Questionnaire questionnaire = new Questionnaire(null, questionnaireName, description, true);
        questionnaireRepo.save(questionnaire);

        UUID questionnaireUuid = questionnaire.getUuid();
        questionnaireQuestionRepo.saveAll(questionnaireQuestionAddDTOList.stream()
                .map(questionnaireQuestionAddDTO -> QuestionnaireQuestion.builder()
                        .questionnaireUuid(questionnaireUuid)
                        .page(questionnaireQuestionAddDTO.page)
                        .module(questionnaireQuestionAddDTO.module)
                        .questionNo(questionnaireQuestionAddDTO.questionNo)
                        .question(questionnaireQuestionAddDTO.question)
                        .questionType(questionnaireQuestionAddDTO.questionType)
                        .options(QuestionnaireQuestion.options2MapString(questionnaireQuestionAddDTO.options))
                        .deleted(false)
                        .build()
                )
                .collect(Collectors.toList())
        );
        return questionnaireUuid;
    }

    @Override
    public List<QuestionnaireDTO> get(Boolean servicing) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        return (servicing == null
                ? questionnaireRepo.findAll(sort)
                : questionnaireRepo.findByServicing(servicing, sort)).stream()
                .map(questionnaire -> new QuestionnaireDTO(
                        questionnaire.getUuid(),
                        questionnaire.getQuestionnaireName(),
                        questionnaire.getDescription(),
                        questionnaire.getServicing(),
                        questionnaire.getCreateTime(),
                        questionnaire.getUpdateTime()
                )).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void remove(UUID questionnaireUuid) {
        questionnaireRepo.deleteById(questionnaireUuid);
        if (questionnaireResultRepo.findByQuestionnaireUuid(questionnaireUuid).size() == 0) {
            questionnaireQuestionRepo.deleteByQuestionnaireUuid(questionnaireUuid);
        }
    }

    @Override
    public void change(UUID uuid, String name, String description, Boolean servicing) {
        questionnaireRepo.findById(uuid)
                .ifPresent(oldQuestionnaire -> {
                            Questionnaire newQuestionnaire = new Questionnaire(uuid, name, description, servicing);
                            BeanUtils.copyProperties(
                                    oldQuestionnaire,
                                    newQuestionnaire,
                                    UpdateUtil.getNotNullFields(newQuestionnaire));
                            questionnaireRepo.save(newQuestionnaire);
                        }
                );
    }

    @Override
    public Optional<QuestionnaireDTO> getById(UUID questionnaireUuid) {
        return questionnaireRepo.findById(questionnaireUuid)
                .map(questionnaire -> new QuestionnaireDTO(
                        questionnaireUuid,
                        questionnaire.getQuestionnaireName(),
                        questionnaire.getDescription(),
                        questionnaire.getServicing(),
                        questionnaire.getCreateTime(),
                        questionnaire.getUpdateTime()
                ));
    }

    @Override
    public Optional<QuestionnaireDetailsDTO> getDetailsById(UUID questionnaireUuid) {
        return questionnaireRepo.findById(questionnaireUuid).map(questionnaire -> {
                    Map<Integer, Map<String, List<QuestionnaireQuestionDTO>>> groupedByPageAndModule = questionnaireQuestionService
                            .getByQuestionnaireIdAndDeleted(questionnaireUuid, false)
                            .stream()
                            .collect(Collectors.groupingBy(
                                    QuestionnaireQuestionDTO::getPage,
                                    Collectors.groupingBy(QuestionnaireQuestionDTO::getModule)
                            ));
                    return new QuestionnaireDetailsDTO(
                            questionnaireUuid,
                            questionnaire.getQuestionnaireName(),
                            questionnaire.getDescription(),
                            groupedByPageAndModule.size(),
                            groupedByPageAndModule.entrySet()
                                    .stream()
                                    .map(pageEntry -> {
                                        Map<String, List<QuestionnaireQuestionDTO>> groupedByModules = pageEntry.getValue();
                                        return new QuestionnairePageDTO(
                                                pageEntry.getKey(),
                                                groupedByModules.size(),
                                                groupedByModules.entrySet().stream()
                                                        .sorted(Comparator.comparingInt(moduleEntry -> moduleEntry.getValue().get(0).questionNo))
                                                        .map(moduleEntry -> new QuestionnaireModuleDTO(
                                                                moduleEntry.getKey(),
                                                                moduleEntry.getValue().stream()
                                                                        .sorted(Comparator.comparingInt(questionnaireQuestionDTO -> questionnaireQuestionDTO.questionNo))
                                                                        .map(questionnaireQuestionDTO -> new QuestionnaireModuleDTO.QuestionDTO(
                                                                                questionnaireQuestionDTO.uuid,
                                                                                questionnaireQuestionDTO.question,
                                                                                QuestionnaireQuestion.options2ListString(questionnaireQuestionDTO.options)
                                                                        ))
                                                                        .collect(Collectors.toList())
                                                        ))
                                                        .collect(Collectors.toList())
                                        );
                                    })
                                    .sorted(Comparator.comparingInt(questionnairePageDTO -> questionnairePageDTO.page))
                                    .collect(Collectors.toList())
                    );
                }
        );
    }
}
