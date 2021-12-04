package top.chenzhimeng.hr_health_check.controller;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.chenzhimeng.hr_health_check.annotation.JsonArg;
import top.chenzhimeng.hr_health_check.exception.CustomException;
import top.chenzhimeng.hr_health_check.model.dto.PageQuestionnaireResultsDTO;
import top.chenzhimeng.hr_health_check.model.dto.QuestionnaireDTO;
import top.chenzhimeng.hr_health_check.model.dto.QuestionnaireReportDTO;
import top.chenzhimeng.hr_health_check.service.IQuestionnaireQuestionService;
import top.chenzhimeng.hr_health_check.service.IQuestionnaireResultService;
import top.chenzhimeng.hr_health_check.service.IQuestionnaireService;
import top.chenzhimeng.hr_health_check.util.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author M
 * @date 2021/04/17
 **/
@RestController
@Validated
public class QuestionnaireResultController {
    private final IQuestionnaireResultService questionnaireResultService;
    private final IQuestionnaireService questionnaireService;
    private final IQuestionnaireQuestionService questionnaireQuestionService;

    public QuestionnaireResultController(
            IQuestionnaireResultService questionnaireResultService,
            IQuestionnaireService questionnaireService,
            IQuestionnaireQuestionService questionnaireQuestionService
    ) {
        this.questionnaireResultService = questionnaireResultService;
        this.questionnaireService = questionnaireService;
        this.questionnaireQuestionService = questionnaireQuestionService;
    }

    /**
     * showdoc
     *
     * @param questionnaireUuid 必填  UUID    问卷uuid
     * @param companyName       必填  String  公司名
     * @param contactName       必填  String  填写人姓名
     * @param headers           必填  Map<UUID,String>    问卷头
     * @param content           必填  Map<UUID,String>    问卷正文
     * @return {"code":200,"msg":"","data":{"questionnaireUuid":"32aeb0d2-fa3f-4a46-b3a5-8cd6adf8152c","questionnaireName":"大学生心理健康调察问卷","questionnaireResultUuid":"3ba2fb74-91ec-4a3c-95e4-78bf181206fd","companyName":"公司1","contactName":"陈大帅","createTime":"2021-06-02T13:24:35.304289200Z","header":{"头1":"头回答1","头2":"头回答2"},"reportScore":55.0,"moduleReports":[{"moduleName":"模块1","moduleScore":82.5,"questionData":[{"moduleName":"模块1","question":"问题1","content":"3","questionNo":2,"score":99.0},{"moduleName":"模块1","question":"问题2","content":"2","questionNo":3,"score":66.0}]},{"moduleName":"模块2","moduleScore":49.5,"questionData":[{"moduleName":"模块2","question":"问题3","content":"2","questionNo":4,"score":66.0},{"moduleName":"模块2","question":"问题4","content":"1","questionNo":5,"score":33.0}]},{"moduleName":"模块3","moduleScore":33.0,"questionData":[{"moduleName":"模块3","question":"问题7","content":"1","questionNo":6,"score":33.0}]}]}}
     * @json_param {"companyName":"公司1","contactName":"陈大帅","headers":{"ee884c5e-071a-496d-ae39-cf34fbe98b63":"头回答1","fcdf3da2-dcaf-48cd-a33a-863cfc966ce2":"头回答2"},"content":{"2aa1e943-6cbc-4d26-a3f9-08ca502ec2b6":1,"4b08c5f3-47be-4b0f-ac36-0388d4aa9104":2,"7591b972-48e7-442d-866a-11e2e4900644":1,"d4f9f5d0-15f3-450f-9848-d13a8ca5026f":2,"ebc6579a-1236-40ab-9aed-1ca2b0e7ff1c":3}}
     * @catalog row/问卷相关/结果相关
     * @title 新建
     * @method post
     * @url /questionnaires/:questionnaire-uuid/results
     */
    @PostMapping(value = "/questionnaires/{questionnaire-uuid}/results")
    public QuestionnaireReportDTO submit(
            HttpServletRequest request,
            @PathVariable("questionnaire-uuid") UUID questionnaireUuid,
            @JsonArg @NotBlank @Size(max = 50) String companyName,
            @JsonArg @NotBlank @Size(max = 20) String contactName,
            @JsonArg Map<UUID, String> headers,
            @JsonArg Map<UUID, Integer> content
    ) {
        // 检查问卷是否可用
        Optional<QuestionnaireDTO> optionalQuestionnaireDTO = questionnaireService.getById(questionnaireUuid);
        if (!optionalQuestionnaireDTO.map(questionnaireDTO -> questionnaireDTO.servicing).orElse(false))
            throw new CustomException(HttpStatus.BAD_REQUEST, "This questionnaire isn't exist or stop service");

        // 检查回答是否合法: [1,3]
        content.values().forEach(contentValue -> {
                    if (contentValue < 1 || contentValue > 3)
                        throw new CustomException(HttpStatus.BAD_REQUEST, "Answer has invalid value");
                }
        );

        // 检查是否回答了所有问题
        headers.putAll(
                content.entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.valueOf(entry.getValue())))
        );
        Set<UUID> validUuidSet = questionnaireQuestionService
                .getByQuestionnaireIdAndDeleted(questionnaireUuid, false)
                .stream()
                .map(questionnaireQuestionDTO -> questionnaireQuestionDTO.uuid)
                .collect(Collectors.toSet());
        if (!validUuidSet.equals(headers.keySet()))
            throw new CustomException(HttpStatus.BAD_REQUEST, "Please answer all questions");

        return questionnaireResultService.getReportByUuid(
                questionnaireResultService.create(
                        questionnaireUuid,
                        companyName,
                        contactName,
                        HttpUtil.getInstance().getIPAddress(request),
                        headers
                )
        ).orElse(null);
    }

    /**
     * showdoc
     *
     * @param page 选填  Integer 页码
     * @param size 选填  Integer 页面大小
     * @return {"code":200,"msg":"","data":{"totalPage":2,"results":[{"questionnaireName":"大学生心理健康调察问卷","uuid":"1757ec1f-a6da-4a53-b087-b3a8129a7bef","companyName":"公司3","contactName":"打工人3","createTime":"2021-04-17T16:43:26Z"},{"questionnaireName":"大学生心理健康调察问卷","uuid":"1757ec1f-a6da-4a53-b087-b3a8129a7bed","companyName":"公司2","contactName":"打工人2","createTime":"2021-04-17T16:43:26Z"}]}}
     * @return_param totalPage  Integer 总页数
     * @return_param results    List<QuestionnaireResultDTO>    结果列表
     * @catalog row/问卷相关/结果相关
     * @title 查询
     * @method get
     * @url /questionnaires/results?page=<int>&size=<int>
     * @json_param {"page":0,"size":2}
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/questionnaires/results")
    public PageQuestionnaireResultsDTO search(
            @RequestParam(required = false) @Min(0) Integer page,
            @RequestParam(required = false) @Min(1) Integer size
    ) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        return questionnaireResultService.getSortedPage(page, size, sort);
    }

    /**
     * showdoc
     *
     * @param resultUuid 必填  UUID 问卷填写结果uuid
     * @return {"code":200,"msg":"","data":{"questionnaireUuid":"32aeb0d2-fa3f-4a46-b3a5-8cd6adf8152c","questionnaireName":"大学生心理健康调察问卷","questionnaireResultUuid":"c0a0ccd6-4747-4aab-b30c-102acb6dae76","companyName":"公司1","contactName":"陈大帅","createTime":"2021-06-02T13:17:34.480897Z","header":{"头1":"头回答1","头2":"头回答2"},"reportScore":55.0,"moduleReports":[{"moduleName":"模块1","moduleScore":82.5,"questionData":[{"moduleName":"模块1","question":"问题1","content":"3","questionNo":2,"score":99.0},{"moduleName":"模块1","question":"问题2","content":"2","questionNo":3,"score":66.0}]},{"moduleName":"模块2","moduleScore":49.5,"questionData":[{"moduleName":"模块2","question":"问题3","content":"2","questionNo":4,"score":66.0},{"moduleName":"模块2","question":"问题4","content":"1","questionNo":5,"score":33.0}]},{"moduleName":"模块3","moduleScore":33.0,"questionData":[{"moduleName":"模块3","question":"问题7","content":"1","questionNo":6,"score":33.0}]}]}}
     * @catalog row/问卷相关/结果相关
     * @title 查询（报告）
     * @method get
     * @url /questionnaires/reports/:result-uuid
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/questionnaires/reports/{result-uuid}")
    public QuestionnaireReportDTO searchReports(@PathVariable("result-uuid") UUID resultUuid) {
        return questionnaireResultService.getReportByUuid(resultUuid)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "doesn't exists this report"));
    }
}
