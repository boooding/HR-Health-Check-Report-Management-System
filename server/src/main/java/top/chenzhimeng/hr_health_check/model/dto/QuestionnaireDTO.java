package top.chenzhimeng.hr_health_check.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.chenzhimeng.hr_health_check.annotation.SensitiveClass;
import top.chenzhimeng.hr_health_check.annotation.SensitiveInfo;
import top.chenzhimeng.hr_health_check.model.interfaces.JsonAble;

import java.time.Instant;
import java.util.UUID;

/**
 * @author M
 * @date 2021/04/15
 **/
@AllArgsConstructor
@SensitiveClass
public class QuestionnaireDTO implements JsonAble {
    public UUID uuid;
    public String name;
    public String description;
    @SensitiveInfo
    public Boolean servicing;
    @SensitiveInfo
    public Instant createTime;
    @SensitiveInfo
    public Instant updateTime;
}
