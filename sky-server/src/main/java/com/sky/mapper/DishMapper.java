package com.sky.mapper;

import com.sky.entity.Dish;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 菜品Mapper
 */
@Mapper
public interface DishMapper {
    
    /**
     * 根据分类id查询菜品
     */
    @Select("select * from dish where category_id = #{categoryId} and status = 1 order by create_time desc")
    List<Dish> getByCategoryId(Long categoryId);
    
    /**
     * 查询所有菜品
     */
    @Select("select * from dish where status = 1 order by create_time desc")
    List<Dish> list();
    
    /**
     * 根据id查询菜品
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);
    
    /**
     * 插入菜品数据
     */
    @Insert("insert into dish (name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) " +
            "values (#{name}, #{categoryId}, #{price}, #{image}, #{description}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Dish dish);
    
    /**
     * 更新菜品
     */
    @Update("update dish set name=#{name}, category_id=#{categoryId}, price=#{price}, image=#{image}, " +
            "description=#{description}, status=#{status}, update_time=#{updateTime}, update_user=#{updateUser} where id=#{id}")
    void update(Dish dish);
    
    /**
     * 删除菜品
     */
    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);
    
}
