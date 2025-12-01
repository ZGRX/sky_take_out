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
     * 查询所有分类
     */
    @Select("select * from category where status = 1 order by sort")
    List<Category> list();
    
    /**
     * 根据id查询分类
     */
    @Select("select * from category where id = #{id}")
    Category getById(Long id);
    
}
