package top.chenzhimeng.hr_health_check.model.po;

import com.alibaba.fastjson.JSON;
import lombok.*;
import org.hibernate.annotations.Type;
import top.chenzhimeng.hr_health_check.model.dto.QuestionnaireQuestionDTO;
import top.chenzhimeng.hr_health_check.model.interfaces.JsonAble;
import top.chenzhimeng.hr_health_check.model.enums.QuestionType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author M
 * @date 2021/04/14
 **/
@Data
@Entity
@Table(name = "questionnaire_question", indexes = {
        @Index(name = "uk_questionnaire_uuid_question", columnList = "questionnaire_uuid, question", unique = true)
})
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionnaireQuestion implements JsonAble, Serializable {
    @Id
    @Column(length = 36)
    @Type(type = "uuid-char")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Type(type = "uuid-char")
    @Column(name = "questionnaire_uuid", length = 36, nullable = false)
    private UUID questionnaireUuid;

    @Column(columnDefinition = "tinyint(1) not null")
    private Integer page;

    @Column(length = 255, nullable = false)
    private String module;

    @Column(columnDefinition = "tinyint(2) not null")
    private Integer questionNo;

    @Column(length = 255, nullable = false)
    private String question;

    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;

    @Column(length = 1023, nullable = true)
    private String options;

    @Column(nullable = false)
    private Boolean deleted;

    public QuestionnaireQuestionDTO toDTO() {
        return new QuestionnaireQuestionDTO(
                uuid,
                questionnaireUuid,
                page,
                module,
                questionNo,
                question,
                questionType,
                options,
                deleted
        );
    }

    public static String options2MapString(List<String> options) {
        if (options == null) return null;

        Map<Integer, String> indexOptionMap = new LinkedHashMap<>();
        for (int i = 0; i < options.size(); i++) {
            indexOptionMap.put(i, options.get(i));
        }
        return JSON.toJSONString(indexOptionMap);
    }

    public static List<String> options2ListString(String options) {
        if (options == null) return null;

        return JSON.parseObject(options).entrySet()
                .stream()
                .sorted(Comparator.comparingInt(entry -> Integer.parseInt(entry.getKey())))
                .map(entry -> (String) entry.getValue())
                .collect(Collectors.toList());
    }
}
