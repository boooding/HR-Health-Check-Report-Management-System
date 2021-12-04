package top.chenzhimeng.hr_health_check.model.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import top.chenzhimeng.hr_health_check.model.interfaces.JsonAble;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * @author M
 * @date 2021/04/09
 **/
@Data
@NoArgsConstructor
@Entity
@Table(name = "questionnaire")
public class Questionnaire implements JsonAble, Serializable {
    @Id
    @Column(length = 36)
    @Type(type = "uuid-char")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    @Column(name = "questionnaire_name", length = 255, nullable = false)
    private String questionnaireName;

    @Column(length = 1023, nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean servicing;

    @Column(name = "create_time", nullable = false)
    @CreationTimestamp
    private Instant createTime;

    @Column(name = "update_time", nullable = false)
    @UpdateTimestamp
    private Instant updateTime;

    public Questionnaire(UUID uuid, String questionnaireName, String description, Boolean servicing) {
        this.uuid = uuid;
        this.questionnaireName = questionnaireName;
        this.description = description;
        this.servicing = servicing;
    }
}
