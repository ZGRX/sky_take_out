package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * 用户Service接口
 */
public interface UserService {
    
    /**
     * 用户登录（简化版：手机号直接登录，如果不存在则自动注册）
     */
    User login(UserLoginDTO userLoginDTO);
    
}
