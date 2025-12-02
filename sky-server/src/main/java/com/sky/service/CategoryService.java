package com.sky.service;

import com.sky.entity.Category;

import java.util.List;

/**
 * 分类Service接口
 */
public interface CategoryService {
    
    /**
     * 查询分类列表
     * @param type
     * @return
     */
    List<Category> list(Integer type);
    
    /**
     * 新增分类
     * @param category
     */
    void save(Category category);
    
    /**
     * 修改分类
     * @param category
     */
    void update(Category category);
    
    /**
     * 删除分类
     * @param id
     */
    void delete(Long id);
    
    /**
     * 启用/停用分类
     * @param status
     * @param id
     */
    void updateStatus(Integer status, Long id);
}
