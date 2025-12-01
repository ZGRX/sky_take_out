package com.sky.service;

import com.sky.entity.Dish;
import java.util.List;

/**
 * 菜品Service接口
 */
public interface DishService {
    
    /**
     * 根据分类id查询菜品
     */
    List<Dish> getByCategoryId(Long categoryId);
    
    /**
     * 查询所有菜品
     */
    List<Dish> list();
    
    /**
     * 根据id查询菜品
     */
    Dish getById(Long id);
    
    /**
     * 新增菜品
     */
    void save(Dish dish);
    
    /**
     * 更新菜品
     */
    void update(Dish dish);
    
    /**
     * 删除菜品
     */
    void deleteById(Long id);
    
}
