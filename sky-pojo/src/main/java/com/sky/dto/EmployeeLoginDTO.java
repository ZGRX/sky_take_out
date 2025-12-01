package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 员工登录DTO
 */
@Data
public class EmployeeLoginDTO implements Serializable {
    
    private String username;
    
    private String password;
    
}
