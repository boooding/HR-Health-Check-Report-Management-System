package top.chenzhimeng.hr_health_check.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.chenzhimeng.hr_health_check.model.dto.QuestionnaireQuestionAddDTO;
import top.chenzhimeng.hr_health_check.model.dto.QuestionnaireQuestionDTO;
import top.chenzhimeng.hr_health_check.model.dto.QuestionnaireQuestionUpdateDTO;
import top.chenzhimeng.hr_health_check.model.po.QuestionnaireQuestion;
import top.chenzhimeng.hr_health_check.repo.QuestionnaireQuestionRepo;
import top.chenzhimeng.hr_health_check.repo.ResultDataRepo;
import top.chenzhimeng.hr_health_check.service.IQuestionnaireQuestionService;
import top.chenzhimeng.hr_health_check.util.UpdateUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author M
 * @date 2021/05/06
 **/
@Service
public class QuestionnaireQuestionServiceImpl implements IQuestionnaireQuestionService {
    private final QuestionnaireQuestionRepo questionnaireQuestionRepo;
    private final ResultDataRepo resultDataRepo;

    public QuestionnaireQuestionServiceImpl(
            QuestionnaireQuestionRepo questionnaireQuestionRepo,
            ResultDataRepo resultDataRepo
    ) {
        this.questionnaireQuestionRepo = questionnaireQuestionRepo;
        this.resultDataRepo = resultDataRepo;
    }

    @Override
    public List<QuestionnaireQuestionDTO> getByQuestionnaireIdAndDeleted(UUID questionnaireUuid, Boolean deleted) {
        return Optional.ofNullable(deleted)
                .map((isDeleted) -> questionnaireQuestionRepo
                        .findByQuestionnaireUuidAndDeleted(questionnaireUuid, isDeleted))
                .orElseGet(() -> questionnaireQuestionRepo.findByQuestionnaireUuid(questionnaireUuid))
                .stream()
                .map(QuestionnaireQuestion::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<QuestionnaireQuestionDTO> getByQuestionnaireIdAndId(UUID questionnaireUuid, UUID uuid) {
        return questionnaireQuestionRepo.findByQuestionnaireUuidAndUuid(questionnaireUuid, uuid)
                .map(QuestionnaireQuestion::toDTO);
    }

    @Override
    @Transactional
    public void change(List<QuestionnaireQuestionUpdateDTO> questionnaireQuestionUpdateDTOList) {
        questionnaireQuestionUpdateDTOList.forEach(questionnaireQuestionUpdateDTO -> change(
                QuestionnaireQuestion.builder()
                        .uuid(questionnaireQuestionUpdateDTO.uuid)
                        .question(questionnaireQuestionUpdateDTO.question)
                        .options(QuestionnaireQuestion.options2MapString(questionnaireQuestionUpdateDTO.options))
                        .build()
                )
        );
    }

    @Override
    @Transactional
    public void create(List<QuestionnaireQuestionAddDTO> questionnaireQuestionAddDTOList) {
        questionnaireQuestionRepo.saveAll(questionnaireQuestionAddDTOList.stream()
                .map(questionnaireQuestionAddDTO ->
                        QuestionnaireQuestion.builder()
                                .questionnaireUuid(questionnaireQuestionAddDTO.questionnaireUuid)
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
    }

    @Override
    @Transactional
    public void remove(List<UUID> questionnaireQuestionUuidList) {
        questionnaireQuestionUuidList.forEach(questionUuid -> {
            if (resultDataRepo.countByQuestionUuid(questionUuid) > 0) {
                change(QuestionnaireQuestion.builder()
                        .uuid(questionUuid)
                        .deleted(true)
                        .build());
                return;
            }
            questionnaireQuestionRepo.deleteById(questionUuid);
        });
    }

    private void change(QuestionnaireQuestion questionnaireQuestion) {
        questionnaireQuestionRepo.findById(questionnaireQuestion.getUuid())
                .ifPresent(oldQuestionnaireQuestion -> {
                            BeanUtils.copyProperties(
                                    oldQuestionnaireQuestion,
                                    questionnaireQuestion,
                                    UpdateUtil.getNotNullFields(questionnaireQuestion));
                            questionnaireQuestionRepo.save(questionnaireQuestion);
                        }
                );
    }
}
