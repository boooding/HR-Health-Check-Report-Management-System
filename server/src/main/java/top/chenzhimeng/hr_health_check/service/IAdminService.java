package top.chenzhimeng.hr_health_check.service;

import top.chenzhimeng.hr_health_check.model.po.Admin;

import java.util.Optional;

/**
 * @author M
 * @date 2021/04/14
 **/
public interface IAdminService {
    /**
     * 根据账号获取管理员账号密码
     *
     * @param account 账号
     * @return 管理员
     */
    Optional<Admin> getByUsername(String account);
}
