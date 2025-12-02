package com.sky.controller.admin;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理Controller
 */
@RestController("adminCategoryController")
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类管理接口")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * 查询所有分类
     */
    @GetMapping("/list")
    @ApiOperation("查询所有分类")
    public Result<List<Category>> list(@RequestParam(required = false) Integer type) {
        log.info("查询所有分类，type={}", type);
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
    
    /**
     * 新增分类
     */
    @PostMapping
    @ApiOperation("新增分类")
    public Result save(@RequestBody Category category) {
        log.info("新增分类：{}", category);
        categoryService.save(category);
        return Result.success();
    }
    
    /**
     * 修改分类
     */
    @PutMapping
    @ApiOperation("修改分类")
    public Result update(@RequestBody Category category) {
        log.info("修改分类：{}", category);
        categoryService.update(category);
        return Result.success();
    }
    
    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除分类")
    public Result delete(@PathVariable Long id) {
        log.info("删除分类：{}", id);
        categoryService.delete(id);
        return Result.success();
    }
    
    /**
     * 启用/停用分类
     */
    @PutMapping("/status/{status}")
    @ApiOperation("启用/停用分类")
    public Result updateStatus(@PathVariable Integer status, @RequestParam Long id) {
        log.info("启用/停用分类：id={}, status={}", id, status);
        categoryService.updateStatus(status, id);
        return Result.success();
    }
}
