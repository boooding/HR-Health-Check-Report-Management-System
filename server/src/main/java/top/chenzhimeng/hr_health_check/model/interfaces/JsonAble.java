package top.chenzhimeng.hr_health_check.model.interfaces;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * @author M
 * @date 2021/04/15
 **/
public interface JsonAble {
    default String toJsonString() {
        return JSON.toJSONString(this);
    }
}
