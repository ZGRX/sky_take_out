package com.sky.controller.admin;

import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 菜品管理Controller
 */
@RestController("adminDishController")
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品管理接口")
public class DishController {
    
    @Autowired
    private DishService dishService;
    
    /**
     * 查询所有菜品
     */
    @GetMapping("/list")
    @ApiOperation("查询所有菜品")
    public Result<List<Dish>> list() {
        log.info("查询所有菜品");
        List<Dish> list = dishService.list();
        return Result.success(list);
    }
    
    /**
     * 根据分类id查询菜品
     */
    @GetMapping("/category/{categoryId}")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> getByCategoryId(@PathVariable Long categoryId) {
        log.info("根据分类id查询菜品：{}", categoryId);
        List<Dish> list = dishService.getByCategoryId(categoryId);
        return Result.success(list);
    }
    
    /**
     * 根据id查询菜品
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<Dish> getById(@PathVariable Long id) {
        log.info("根据id查询菜品：{}", id);
        Dish dish = dishService.getById(id);
        return Result.success(dish);
    }
    
    /**
     * 新增菜品
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody Dish dish) {
        log.info("新增菜品：{}", dish);
        dishService.save(dish);
        return Result.success();
    }
    
    /**
     * 修改菜品
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody Dish dish) {
        log.info("修改菜品：{}", dish);
        dishService.update(dish);
        return Result.success();
    }
    
    /**
     * 删除菜品
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除菜品")
    public Result delete(@PathVariable Long id) {
        log.info("删除菜品：{}", id);
        dishService.deleteById(id);
        return Result.success();
    }
    
}
