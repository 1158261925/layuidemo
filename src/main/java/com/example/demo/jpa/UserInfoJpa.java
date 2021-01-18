package com.example.demo.jpa;

import com.example.demo.pojo.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserInfoJpa extends JpaRepository<UserInfo,Long> {
    UserInfo findByUsername(String username);
    UserInfo findByUsernameAndPasswd(String username,String passwd);
}
