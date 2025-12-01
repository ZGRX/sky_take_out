package com.sky.service.impl;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 用户登录（简化版：手机号直接登录，如果不存在则自动注册）
     */
    public User login(UserLoginDTO userLoginDTO) {
        String phone = userLoginDTO.getPhone();
        
        // 查询用户是否存在
        User user = userMapper.getByPhone(phone);
        
        // 如果不存在，自动注册
        if (user == null) {
            user = User.builder()
                    .phone(phone)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        
        return user;
    }
    
}
