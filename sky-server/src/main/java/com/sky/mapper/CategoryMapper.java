package com.sky.mapper;

import com.sky.entity.Category;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 分类Mapper
 */
@Mapper
public interface CategoryMapper {
    
    /**
     * 根据类型查询分类
     */
    @Select("select * from category where type = #{type} and status = 1 order by sort")
    List<Category> getByType(Integer type);
    
    /**
     * 查询分类列表（支持类型筛选）
     */
    List<Category> list(Integer type);
    
    /**
     * 根据id查询分类
     */
    @Select("select * from category where id = #{id}")
    Category getById(Long id);
    
    /**
     * 新增分类
     */
    @Insert("insert into category (type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "values (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Category category);
    
    /**
     * 修改分类
     */
    void update(Category category);
    
    /**
     * 删除分类
     */
    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);
    
    /**
     * 启用/停用分类
     */
    @Update("update category set status = #{status} where id = #{id}")
    void updateStatus(Integer status, Long id);
    
}
