package top.chenzhimeng.hr_health_check.model.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import top.chenzhimeng.hr_health_check.model.interfaces.JsonAble;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * @author M
 * @date 2021/04/14
 **/
@Data
@NoArgsConstructor
@Entity
@Table(name = "questionnaire_result", indexes = {
        @Index(name = "uk_questionnaire_uuid_ip_addr", columnList = "questionnaire_uuid, ip_addr", unique = true),
        @Index(name = "ix_create_time", columnList = "create_time")
})
public class QuestionnaireResult implements JsonAble, Serializable {
    @Id
    @Column(length = 36)
    @Type(type = "uuid-char")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Type(type = "uuid-char")
    @Column(name = "questionnaire_uuid", length = 36, nullable = false)
    private UUID questionnaireUuid;

    @Column(name = "company_name", length = 255, nullable = false)
    private String companyName;

    @Column(name = "contact_name", length = 255, nullable = false)
    private String contactName;

    @Column(name = "ip_addr", length = 15, nullable = false)
    private String ipAddr;

    @Column(name = "create_time", nullable = false)
    @CreationTimestamp
    private Instant createTime;

    public QuestionnaireResult(
            UUID questionnaireUuid,
            String companyName,
            String contactName,
            String ipAddr
    ) {
        this.questionnaireUuid = questionnaireUuid;
        this.companyName = companyName;
        this.contactName = contactName;
        this.ipAddr = ipAddr;
    }
}
