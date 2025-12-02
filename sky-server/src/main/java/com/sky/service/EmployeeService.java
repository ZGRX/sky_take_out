package com.sky.service;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;

import java.util.List;

/**
 * 员工Service接口
 */
public interface EmployeeService {
    
    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);
    
    /**
     * 查询员工列表
     * @param name
     * @return
     */
    List<Employee> list(String name);
    
    /**
     * 新增员工
     * @param employee
     */
    void save(Employee employee);
    
    /**
     * 根据ID查询员工
     * @param id
     * @return
     */
    Employee getById(Long id);
    
    /**
     * 修改员工
     * @param employee
     */
    void update(Employee employee);
    
    /**
     * 启用/停用员工账号
     * @param status
     * @param id
     */
    void updateStatus(Integer status, Long id);
    
}
