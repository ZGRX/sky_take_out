package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户登录DTO
 */
@Data
public class UserLoginDTO implements Serializable {
    
    private String phone;
    
    private String password;
    
}
