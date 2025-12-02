package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.mapper.EmployeeMapper;
import com.sky.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 员工Service实现类
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    
    @Autowired
    private EmployeeMapper employeeMapper;
    
    /**
     * 员工登录
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        
        // 1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);
        
        // 2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            // 账号不存在
            throw new RuntimeException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        
        // 密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            // 密码错误
            throw new RuntimeException(MessageConstant.PASSWORD_ERROR);
        }
        
        if (employee.getStatus().equals(StatusConstant.DISABLE)) {
            // 账号被锁定
            throw new RuntimeException(MessageConstant.ACCOUNT_LOCKED);
        }
        
        // 3、返回实体对象
        return employee;
    }
    
    /**
     * 查询员工列表
     */
    @Override
    public List<Employee> list(String name) {
        return employeeMapper.list(name);
    }
    
    /**
     * 新增员工
     */
    @Override
    public void save(Employee employee) {
        // 设置默认密码，MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        
        // 设置状态，默认启用
        employee.setStatus(StatusConstant.ENABLE);
        
        // 设置创建时间和更新时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        
        // 设置创建人和修改人
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());
        
        employeeMapper.insert(employee);
    }
    
    /**
     * 根据ID查询员工
     */
    @Override
    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }
    
    /**
     * 修改员工
     */
    @Override
    public void update(Employee employee) {
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);
    }
    
    /**
     * 启用/停用员工账号
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        employeeMapper.updateStatus(status, id);
    }
    
}
