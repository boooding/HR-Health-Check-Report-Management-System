package top.chenzhimeng.hr_health_check.model.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import top.chenzhimeng.hr_health_check.model.interfaces.JsonAble;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author M
 * @date 2021/04/14
 **/

@Data
@NoArgsConstructor
@Entity
@Table(name = "result_data", indexes = {
        @Index(name = "uk_result_uuid_question_uuid", columnList = "result_uuid, question_uuid", unique = true)
})
public class ResultData implements JsonAble, Serializable {
    @Id
    @Column(length = 36)
    @Type(type = "uuid-char")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Type(type = "uuid-char")
    @Column(name = "result_uuid", length = 36, nullable = false)
    private UUID resultUuid;

    @Type(type = "uuid-char")
    @Column(name = "question_uuid", length = 36, nullable = false)
    private UUID questionUuid;

    @Column(length = 255, nullable = false)
    private String content;

    public ResultData(UUID resultUuid, UUID questionUuid, String content) {
        this.resultUuid = resultUuid;
        this.questionUuid = questionUuid;
        this.content = content;
    }
}
