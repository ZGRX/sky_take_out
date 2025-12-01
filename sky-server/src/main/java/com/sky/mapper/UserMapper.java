package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.*;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据手机号查询用户
     */
    @Select("select * from user where phone = #{phone}")
    User getByPhone(String phone);
    
    /**
     * 插入用户数据
     */
    @Insert("insert into user (openid, name, phone, sex, id_number, avatar, create_time) " +
            "values (#{openid}, #{name}, #{phone}, #{sex}, #{idNumber}, #{avatar}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);
    
    /**
     * 根据id查询用户
     */
    @Select("select * from user where id = #{id}")
    User getById(Long id);
    
}
