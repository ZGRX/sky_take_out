package com.sky.controller.user;

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
 * 用户端菜品Controller
 */
@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "用户端-菜品浏览接口")
public class DishController {
    
    @Autowired
    private DishService dishService;
    
    /**
     * 查询所有菜品
     */
    @GetMapping("/list")
    @ApiOperation("查询所有菜品")
    public Result<List<Dish>> list() {
        log.info("用户查询所有菜品");
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
    
}
