package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜品Service实现类
 */
@Service
public class DishServiceImpl implements DishService {
    
    @Autowired
    private DishMapper dishMapper;
    
    /**
     * 根据分类id查询菜品
     */
    public List<Dish> getByCategoryId(Long categoryId) {
        return dishMapper.getByCategoryId(categoryId);
    }
    
    /**
     * 查询所有菜品
     */
    public List<Dish> list() {
        return dishMapper.list();
    }
    
    /**
     * 根据id查询菜品
     */
    public Dish getById(Long id) {
        return dishMapper.getById(id);
    }
    
    /**
     * 新增菜品
     */
    public void save(Dish dish) {
        dish.setCreateTime(LocalDateTime.now());
        dish.setUpdateTime(LocalDateTime.now());
        dish.setCreateUser(BaseContext.getCurrentId());
        dish.setUpdateUser(BaseContext.getCurrentId());
        dishMapper.insert(dish);
    }
    
    /**
     * 更新菜品
     */
    public void update(Dish dish) {
        dish.setUpdateTime(LocalDateTime.now());
        dish.setUpdateUser(BaseContext.getCurrentId());
        dishMapper.update(dish);
    }
    
    /**
     * 删除菜品
     */
    public void deleteById(Long id) {
        dishMapper.deleteById(id);
    }
    
}
