package com.tibame.peterparker.service;

import com.tibame.peterparker.dao.UserRepository;
import com.tibame.peterparker.entity.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


public class AdminUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired  // 注入 UserRepository 和 PasswordEncoder
    public AdminUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 根據用戶名和密碼進行用戶驗證。
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVO userVO = userRepository.findByUserAccountAndUserPassword(username, "password");
        if (userVO == null) {
            throw new UsernameNotFoundException("User not found.");
        }

        return User.withUsername(userVO.getUserAccount())
                .password(passwordEncoder.encode(userVO.getUserPassword()))
                .roles("USER") // 根據需求設置角色，可以從數據庫中擷取角色信息
                .build();
    }
}