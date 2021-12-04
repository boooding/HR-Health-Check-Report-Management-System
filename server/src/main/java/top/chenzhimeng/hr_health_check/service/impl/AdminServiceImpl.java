package top.chenzhimeng.hr_health_check.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.chenzhimeng.hr_health_check.repo.AdminRepo;
import top.chenzhimeng.hr_health_check.model.po.Admin;
import top.chenzhimeng.hr_health_check.service.IAdminService;

import java.util.Optional;

/**
 * @author M
 * @date 2021/04/14
 **/
@Service
public class AdminServiceImpl implements IAdminService, UserDetailsService {
    private final AdminRepo adminRepo;

    public AdminServiceImpl(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<UserDetails> userDetails = getByUsername(s).map(user -> User.builder()
                .username(s)
                .password(user.getPassword())
                .roles("ADMIN")
                .build()
        );
        if (userDetails.isPresent()) return userDetails.get();
        throw new UsernameNotFoundException(String.format("username: %s not found", s));
    }

    @Override
    public Optional<Admin> getByUsername(String account) {
        return adminRepo.findByAccount(account);
    }
}
