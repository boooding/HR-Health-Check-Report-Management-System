package top.chenzhimeng.hr_health_check.model.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.chenzhimeng.hr_health_check.model.interfaces.JsonAble;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author M
 * @date 2021/04/14
 **/
@Data
@NoArgsConstructor
@Entity
@Table(name = "admin", indexes = {
        @Index(name = "uk_account", columnList = "account", unique = true)
})
public class Admin implements JsonAble, Serializable {
    @Id
    @Column(length = 255)
    private String account;

    @Column(length = 255, nullable = false)
    private String password;
}
