package top.chenzhimeng.hr_health_check.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.chenzhimeng.hr_health_check.annotation.JsonArg;
import top.chenzhimeng.hr_health_check.exception.CustomException;
import top.chenzhimeng.hr_health_check.model.dto.*;
import top.chenzhimeng.hr_health_check.model.enums.QuestionType;
import top.chenzhimeng.hr_health_check.service.IQuestionnaireQuestionService;
import top.chenzhimeng.hr_health_check.service.IQuestionnaireService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author M
 * @date 2021/04/15
 **/
@Slf4j
@RestController
@Validated
public class QuestionnaireController {
    private final IQuestionnaireService questionnaireService;
    private final IQuestionnaireQuestionService questionnaireQuestionService;

    public QuestionnaireController(
            IQuestionnaireService questionnaireService,
            IQuestionnaireQuestionService questionnaireQuestionService
    ) {
        this.questionnaireService = questionnaireService;
        this.questionnaireQuestionService = questionnaireQuestionService;
    }

    /**
     * showdoc
     *
     * @param questionnaireName    必填  String   问卷名
     * @param description          选填  String   问卷描述
     * @param questionnaireHeaders 必填  List<String> 问卷头
     * @param questionnaireBody    必填  List<List<QuestionnaireModuleDTO>>   问卷正文
     * @return {"code":200,"msg":"","data":"4b4b4274-011c-4e61-8ea4-e6b4444fdae8"}
     * @return_param data   String  问卷uuid
     * @catalog row/问卷相关
     * @title 新建
     * @method post
     * @url /questionnaires
     * @json_param {"questionnaireName":"大学生心理健康调察问卷","description":"描述","questionnaireHeaders":["姓名","公司名"],"questionnaireBody":[[{"moduleName":"模块1","questions":[{"question":"问题1","options":["选项1","选项2","选项3"]},{"question":"问题2","options":["选项1","选项2","选项3"]}]},{"moduleName":"模块2","questions":[{"question":"问题3","options":["选项1","选项2","选项3"]},{"question":"问题4","options":["选项1","选项2","选项3"]}]}],[{"moduleName":"模块3","questions":[{"question":"问题7","options":["选项1","选项2","选项3"]}]}]]}
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/questionnaires")
    public UUID create(
            @JsonArg @Size(max = 20) String questionnaireName,
            @JsonArg(required = false, defaultValue = "") @Size(max = 100) String description,
            @JsonArg List<String> questionnaireHeaders,
            @JsonArg @NotEmpty @Valid List<List<QuestionnaireModuleDTO>> questionnaireBody
    ) {
        // 处理问卷头
        AtomicInteger questionCounter = new AtomicInteger(0);
        List<QuestionnaireQuestionAddDTO> headerList = questionnaireHeaders.stream().map(question ->
                new QuestionnaireQuestionAddDTO(
                        null,
                        0,
                        "",
                        questionCounter.getAndIncrement(),
                        question,
                        QuestionType.TEXT,
                        null
                )
        ).collect(Collectors.toList());

        // 问卷正文
        AtomicInteger pageCounter = new AtomicInteger(1);
        List<QuestionnaireQuestionAddDTO> bodyQuestionList = questionnaireBody.stream().flatMap(page -> {
            int pageIdx = pageCounter.getAndIncrement();
            return page.stream().flatMap(module -> {
                        String moduleName = module.moduleName;
                        return module.questions.stream().map(questionDTO ->
                                new QuestionnaireQuestionAddDTO(
                                        null,
                                        pageIdx,
                                        moduleName,
                                        questionCounter.getAndIncrement(),
                                        questionDTO.question,
                                        QuestionType.RADIO,
                                        questionDTO.options
                                )
                        );
                    }
            );
        }).collect(Collectors.toList());

        bodyQuestionList.addAll(headerList);
        return questionnaireService.create(questionnaireName, description, bodyQuestionList);
    }

    /**
     * showdoc
     *
     * @param questionnaireUuid 必填  String  问卷uuid
     * @return {"code":200,"msg":""}
     * @catalog row/问卷相关
     * @title 删除
     * @method delete
     * @url /questionnaires/:questionnaire-uuid
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/questionnaires/{questionnaire-uuid}")
    public void remove(@PathVariable("questionnaire-uuid") UUID questionnaireUuid) {
        questionnaireService.remove(questionnaireUuid);
    }

    /**
     * showdoc
     *
     * @param questionnaireUuid 必选  UUID    问卷uuid
     * @param stopOrOpen        必选  String  {"stop","open"}
     * @return {"code":200,"msg":""}
     * @catalog row/问卷相关
     * @title 停止|开启服务
     * @method put
     * @url /questionnaires/:questionnaire-uuid/:stop-or-open
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/questionnaires/{questionnaire-uuid}/{stop-or-open:stop|open}")
    public void stop(
            @PathVariable("questionnaire-uuid") UUID questionnaireUuid,
            @PathVariable("stop-or-open") String stopOrOpen
    ) {
        questionnaireService.change(questionnaireUuid, null, null, stopOrOpen.equals("open"));
    }

    /**
     * showdoc
     *
     * @param questionnaireUuid 必选  UUID    问卷uuid
     * @param questionnaireName 可选  String  问卷名
     * @param description       可选  String  问卷描述
     * @param update            可选  List<QuestionnaireQuestionUpdateDTO>    更新问腿
     * @param add               可选  List<QuestionnaireQuestionAddDTO>   新增问题
     * @param delete            可选  List<UUID>  删除问题
     * @return {"code":200,"msg":""}
     * @json_param {"questionnaireName":"测试修改问卷名","description":"测试修改问卷描述","update":[{"uuid":"000beb08-8dc4-4ad6-aceb-9cb09071d973","question":"问题2（测试修改问卷）","options":["选项1（测试修改问卷）","选项2（测试修改问卷）","选项3（测试修改问卷）"]},{"uuid":"84ef167b-a2c3-40e1-b395-8ca4a96839e2","question":"头2（测试修改问卷）"}],"add":[{"page":1,"module":"新增模块","questionNo":10,"question":"新增问题","questionType":"RADIO","options":["选项1（测试修改问卷）","选项2（测试修改问卷）","选项3（测试修改问卷）"]},{"page":0,"module":"","questionNo":10,"question":"新增头","questionType":"TEXT"}],"delete":["602e14f8-9bc9-4a9a-80a9-707d4f287adb"]}
     * @catalog row/问卷相关
     * @title 修改
     * @method put
     * @url /questionnaires/:questionnaire-uuid
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/questionnaires/{questionnaire-uuid}")
    public void renewal(
            @PathVariable("questionnaire-uuid") UUID questionnaireUuid,
            @JsonArg(required = false) @Size(max = 40) String questionnaireName,
            @JsonArg(required = false) @Size(max = 1000) String description,
            @JsonArg @Valid List<QuestionnaireQuestionUpdateDTO> update,
            @JsonArg @Valid List<QuestionnaireQuestionAddDTO> add,
            @JsonArg @Valid List<UUID> delete
    ) {
        // 检查并更新问卷元信息
        questionnaireService.getById(questionnaireUuid)
                .ifPresentOrElse(
                        (dontCare) -> questionnaireService.change(
                                questionnaireUuid,
                                questionnaireName,
                                description,
                                null),
                        () -> {
                            throw new CustomException(HttpStatus.BAD_REQUEST, "This questionnaire doesn't exists");
                        }
                );

        // 检查并更新问题
        questionnaireQuestionService.change(update.stream()
                .filter(questionnaireQuestionUpdateDTO ->
                        questionnaireQuestionService.getByQuestionnaireIdAndId(
                                questionnaireUuid,
                                questionnaireQuestionUpdateDTO.uuid
                        ).map(questionnaireQuestionDTO ->
                                questionnaireQuestionDTO.questionType == QuestionType.RADIO
                                        && questionnaireQuestionUpdateDTO.options != null
                                        && questionnaireQuestionUpdateDTO.options.size() > 0
                                        || questionnaireQuestionDTO.questionType == QuestionType.TEXT
                                        && questionnaireQuestionDTO.options == null
                        ).orElse(false)
                ).collect(Collectors.toList())
        );

        // 检查并新增问题
        add.forEach(questionnaireQuestionAddDTO -> questionnaireQuestionAddDTO.questionnaireUuid = questionnaireUuid);
        questionnaireQuestionService.create(add.stream()
                .filter(questionnaireQuestionAddDTO ->
                        questionnaireQuestionAddDTO.questionType == QuestionType.TEXT
                                && (questionnaireQuestionAddDTO.options == null || questionnaireQuestionAddDTO.options.size() == 0)
                                || (questionnaireQuestionAddDTO.questionType == QuestionType.RADIO
                                && questionnaireQuestionAddDTO.options != null && questionnaireQuestionAddDTO.options.size() > 1))
                .collect(Collectors.toList())
        );

        // 检查并删除问题
        questionnaireQuestionService.remove(delete.stream()
                .filter(uuid -> questionnaireQuestionService.getByQuestionnaireIdAndId(questionnaireUuid, uuid)
                        .isPresent())
                .collect(Collectors.toList())
        );
    }

    /**
     * showdoc
     *
     * @param servicing 可选  String  是否正在服务，null为所有
     * @return {"code":200,"msg":"","data":[{"uuid":"1757ec1f-a6da-4a53-b087-b3a8129a7beb","name":"大学生心理健康调察问卷","servicing":false,"createTime":"2021-04-17T14:59:44.270433Z","updateTime":"2021-04-17T14:59:44.270433Z"},{"uuid":"5b9de67a-34e8-4aa8-9946-f5ec10e4f519","name":"大学生心理健康调察问卷","servicing":false,"createTime":"2021-04-17T14:57:54.449246Z","updateTime":"2021-04-17T14:57:54.449246Z"}]}
     * @return_param data   List<QuestionnaireDTO>  问卷列表
     * @catalog row/问卷相关
     * @title 查询
     * @method get
     * @url /questionnaires?servicing=<boolean>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') OR true.equals(#servicing)")
    @GetMapping(value = "/questionnaires")
    public List<QuestionnaireDTO> search(@RequestParam(required = false) Boolean servicing) {
        return questionnaireService.get(servicing);
    }

    /**
     * showdoc
     *
     * @param questionnaireUuid 必选  UUID    问卷uuid
     * @return {"code":200,"msg":"","data":{"questionnaireUuid":"32aeb0d2-fa3f-4a46-b3a5-8cd6adf8152c","questionnaireName":"大学生心理健康调察问卷","description":"描述","pageNum":3,"questionnairePages":[{"page":0,"moduleNum":1,"questionnaireModules":[{"moduleName":"","questions":[{"questionUuid":"ee884c5e-071a-496d-ae39-cf34fbe98b63","question":"姓名","options":null},{"questionUuid":"fcdf3da2-dcaf-48cd-a33a-863cfc966ce2","question":"公司名","options":null}]}]},{"page":1,"moduleNum":2,"questionnaireModules":[{"moduleName":"模块1","questions":[{"questionUuid":"ebc6579a-1236-40ab-9aed-1ca2b0e7ff1c","question":"问题1","options":["选项1","选项2","选项3"]},{"questionUuid":"4b08c5f3-47be-4b0f-ac36-0388d4aa9104","question":"问题2","options":["选项1","选项2","选项3"]}]},{"moduleName":"模块2","questions":[{"questionUuid":"d4f9f5d0-15f3-450f-9848-d13a8ca5026f","question":"问题3","options":["选项1","选项2","选项3"]},{"questionUuid":"2aa1e943-6cbc-4d26-a3f9-08ca502ec2b6","question":"问题4","options":["选项1","选项2","选项3"]}]}]},{"page":2,"moduleNum":1,"questionnaireModules":[{"moduleName":"模块3","questions":[{"questionUuid":"7591b972-48e7-442d-866a-11e2e4900644","question":"问题7","options":["选项1","选项2","选项3"]}]}]}]}}
     * @catalog row/问卷相关
     * @title 查询（详情）
     * @method get
     * @url /questionnaires/:questionnaire-uuid
     */
    @GetMapping("/questionnaires/{questionnaire-uuid}")
    public QuestionnaireDetailsDTO searchByUuid(@PathVariable("questionnaire-uuid") UUID questionnaireUuid) {
        boolean existAndService = questionnaireService.getById(questionnaireUuid)
                .map(questionnaireDTO -> questionnaireDTO.servicing)
                .orElse(false);
        if (!existAndService)
            throw new CustomException(HttpStatus.BAD_REQUEST, "this questionnaire doesn't exist or doesn't servicing");

        return questionnaireService.getDetailsById(questionnaireUuid).orElse(null);
    }
}
