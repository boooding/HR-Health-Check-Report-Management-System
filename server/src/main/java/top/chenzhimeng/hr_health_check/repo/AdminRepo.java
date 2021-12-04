package top.chenzhimeng.hr_health_check.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.chenzhimeng.hr_health_check.model.po.Admin;

import java.util.Optional;
import java.util.UUID;

/**
 * @author M
 * @date 2021/04/14
 **/
@Repository
public interface AdminRepo extends JpaRepository<Admin, UUID> {
    Optional<Admin> findByAccount(String account);
}
