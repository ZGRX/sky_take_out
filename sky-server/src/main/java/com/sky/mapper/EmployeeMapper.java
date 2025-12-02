package com.sky.mapper;

import com.sky.entity.Employee;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 员工Mapper
 */
@Mapper
public interface EmployeeMapper {
    
    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);
    
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
    @Insert("insert into employee (name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "values (#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Employee employee);
    
    /**
     * 根据ID查询员工
     * @param id
     * @return
     */
    @Select("select * from employee where id = #{id}")
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
    @Update("update employee set status = #{status} where id = #{id}")
    void updateStatus(Integer status, Long id);
    
}
