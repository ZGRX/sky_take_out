# 苍穹外卖后端开发任务

## 📋 需要实现的后端接口

### 1. 员工管理接口（EmployeeController 扩展）

需要在 `EmployeeController.java` 中添加以下方法：

```java
/**
 * 查询员工列表
 */
@GetMapping("/list")
@ApiOperation("查询员工列表")
public Result<List<Employee>> list() {
    log.info("查询员工列表");
    List<Employee> list = employeeService.list();
    return Result.success(list);
}

/**
 * 新增员工
 */
@PostMapping
@ApiOperation("新增员工")
public Result save(@RequestBody Employee employee) {
    log.info("新增员工：{}", employee);
    employeeService.save(employee);
    return Result.success();
}

/**
 * 修改员工
 */
@PutMapping
@ApiOperation("修改员工")
public Result update(@RequestBody Employee employee) {
    log.info("修改员工：{}", employee);
    employeeService.update(employee);
    return Result.success();
}

/**
 * 删除员工
 */
@DeleteMapping("/{id}")
@ApiOperation("删除员工")
public Result delete(@PathVariable Long id) {
    log.info("删除员工：{}", id);
    employeeService.deleteById(id);
    return Result.success();
}

/**
 * 启用/禁用员工账号
 */
@PutMapping("/status/{status}")
@ApiOperation("启用/禁用员工账号")
public Result updateStatus(@PathVariable Integer status, @RequestParam Long id) {
    log.info("启用/禁用员工账号：id={}, status={}", id, status);
    employeeService.updateStatus(id, status);
    return Result.success();
}
```

---

### 2. 分类管理接口（CategoryController - 新建）

创建 `CategoryController.java`：

```java
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
        categoryService.deleteById(id);
        return Result.success();
    }
    
    /**
     * 启用/停用分类
     */
    @PutMapping("/status/{status}")
    @ApiOperation("启用/停用分类")
    public Result updateStatus(@PathVariable Integer status, @RequestParam Long id) {
        log.info("启用/停用分类：id={}, status={}", id, status);
        categoryService.updateStatus(id, status);
        return Result.success();
    }
}
```

---

### 3. 套餐管理接口（SetmealController - 新建）

创建 `SetmealController.java`：

```java
package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 套餐管理Controller
 */
@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐管理接口")
public class SetmealController {
    
    /**
     * 查询套餐列表（暂时返回空列表，待实现）
     */
    @GetMapping("/list")
    @ApiOperation("查询套餐列表")
    public Result<List<Map<String, Object>>> list() {
        log.info("查询套餐列表");
        List<Map<String, Object>> list = new ArrayList<>();
        // 示例数据
        Map<String, Object> setmeal1 = new HashMap<>();
        setmeal1.put("id", 1);
        setmeal1.put("name", "商务套餐");
        setmeal1.put("categoryId", 1);
        setmeal1.put("price", 8800);
        setmeal1.put("status", 1);
        setmeal1.put("description", "适合商务聚餐");
        list.add(setmeal1);
        
        return Result.success(list);
    }
    
    /**
     * 新增套餐
     */
    @PostMapping
    @ApiOperation("新增套餐")
    public Result save(@RequestBody Map<String, Object> setmeal) {
        log.info("新增套餐：{}", setmeal);
        // TODO: 实现新增逻辑
        return Result.success();
    }
    
    /**
     * 修改套餐
     */
    @PutMapping
    @ApiOperation("修改套餐")
    public Result update(@RequestBody Map<String, Object> setmeal) {
        log.info("修改套餐：{}", setmeal);
        // TODO: 实现修改逻辑
        return Result.success();
    }
    
    /**
     * 删除套餐
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除套餐")
    public Result delete(@PathVariable Long id) {
        log.info("删除套餐：{}", id);
        // TODO: 实现删除逻辑
        return Result.success();
    }
    
    /**
     * 起售/停售套餐
     */
    @PutMapping("/status/{status}")
    @ApiOperation("起售/停售套餐")
    public Result updateStatus(@PathVariable Integer status, @RequestParam Long id) {
        log.info("起售/停售套餐：id={}, status={}", id, status);
        // TODO: 实现状态更新逻辑
        return Result.success();
    }
}
```

---

### 4. 数据统计接口（StatisticsController - 新建）

创建 `StatisticsController.java`：

```java
package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

/**
 * 数据统计Controller
 */
@RestController
@RequestMapping("/admin/statistics")
@Slf4j
@Api(tags = "数据统计接口")
public class StatisticsController {
    
    /**
     * 营业额统计
     */
    @GetMapping("/turnover")
    @ApiOperation("营业额统计")
    public Result<Map<String, Object>> getTurnoverStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("营业额统计：{} - {}", begin, end);
        
        // 模拟数据
        Map<String, Object> data = new HashMap<>();
        List<String> dateList = new ArrayList<>();
        List<Double> amountList = new ArrayList<>();
        
        LocalDate current = begin;
        Random random = new Random();
        while (!current.isAfter(end)) {
            dateList.add(current.toString());
            amountList.add(random.nextDouble() * 10000 + 5000);
            current = current.plusDays(1);
        }
        
        data.put("dateList", dateList);
        data.put("amountList", amountList);
        
        return Result.success(data);
    }
    
    /**
     * 订单统计
     */
    @GetMapping("/orders")
    @ApiOperation("订单统计")
    public Result<Map<String, Object>> getOrderStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("订单统计：{} - {}", begin, end);
        
        // 模拟数据
        Map<String, Object> data = new HashMap<>();
        List<String> dateList = new ArrayList<>();
        List<Integer> orderCountList = new ArrayList<>();
        
        LocalDate current = begin;
        Random random = new Random();
        while (!current.isAfter(end)) {
            dateList.add(current.toString());
            orderCountList.add(random.nextInt(50) + 10);
            current = current.plusDays(1);
        }
        
        data.put("dateList", dateList);
        data.put("orderCountList", orderCountList);
        
        return Result.success(data);
    }
    
    /**
     * 销量排行Top10
     */
    @GetMapping("/top10")
    @ApiOperation("销量排行Top10")
    public Result<Map<String, Object>> getTop10(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("销量排行Top10：{} - {}", begin, end);
        
        // 模拟数据
        Map<String, Object> data = new HashMap<>();
        data.put("nameList", Arrays.asList(
            "宫保鸡丁", "鱼香肉丝", "麻婆豆腐", "回锅肉", "糖醋里脊",
            "青椒肉丝", "酸辣土豆丝", "西红柿炒蛋", "地三鲜", "红烧肉"
        ));
        data.put("numberList", Arrays.asList(156, 142, 138, 125, 118, 102, 95, 87, 76, 65));
        
        return Result.success(data);
    }
}
```

---

## 📝 Service 层实现示例

### EmployeeService.java（需要扩展）

```java
/**
 * 查询员工列表
 */
List<Employee> list();

/**
 * 新增员工
 */
void save(Employee employee);

/**
 * 修改员工
 */
void update(Employee employee);

/**
 * 删除员工
 */
void deleteById(Long id);

/**
 * 启用/禁用员工
 */
void updateStatus(Long id, Integer status);
```

### CategoryService.java（需要新建）

```java
package com.sky.service;

import com.sky.entity.Category;
import java.util.List;

public interface CategoryService {
    
    /**
     * 查询分类列表
     */
    List<Category> list(Integer type);
    
    /**
     * 新增分类
     */
    void save(Category category);
    
    /**
     * 修改分类
     */
    void update(Category category);
    
    /**
     * 删除分类
     */
    void deleteById(Long id);
    
    /**
     * 启用/停用分类
     */
    void updateStatus(Long id, Integer status);
}
```

---

## 🗄️ Mapper 层实现示例

### EmployeeMapper.xml（需要添加）

```xml
<!-- 查询所有员工 -->
<select id="list" resultType="com.sky.entity.Employee">
    select * from employee order by create_time desc
</select>

<!-- 新增员工 -->
<insert id="save">
    insert into employee (name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user)
    values (#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})
</insert>

<!-- 修改员工 -->
<update id="update">
    update employee set
        name = #{name},
        phone = #{phone},
        sex = #{sex},
        id_number = #{idNumber},
        update_time = #{updateTime},
        update_user = #{updateUser}
    where id = #{id}
</update>

<!-- 删除员工 -->
<delete id="deleteById">
    delete from employee where id = #{id}
</delete>

<!-- 更新员工状态 -->
<update id="updateStatus">
    update employee set status = #{status}, update_time = now() where id = #{id}
</update>
```

### CategoryMapper.xml（需要新建）

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.sky.mapper.CategoryMapper">
    
    <!-- 查询分类列表 -->
    <select id="list" resultType="com.sky.entity.Category">
        select * from category
        <where>
            <if test="type != null">
                and type = #{type}
            </if>
        </where>
        order by sort asc, create_time desc
    </select>
    
    <!-- 新增分类 -->
    <insert id="save">
        insert into category (name, type, sort, status, create_time, update_time, create_user, update_user)
        values (#{name}, #{type}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})
    </insert>
    
    <!-- 修改分类 -->
    <update id="update">
        update category set
            name = #{name},
            type = #{type},
            sort = #{sort},
            update_time = #{updateTime},
            update_user = #{updateUser}
        where id = #{id}
    </update>
    
    <!-- 删除分类 -->
    <delete id="deleteById">
        delete from category where id = #{id}
    </delete>
    
    <!-- 更新分类状态 -->
    <update id="updateStatus">
        update category set status = #{status}, update_time = now() where id = #{id}
    </update>
    
</mapper>
```

---

## 🚀 实现步骤

### 优先级1：员工管理
1. 在 `EmployeeController.java` 添加新方法
2. 在 `EmployeeService.java` 添加接口定义
3. 在 `EmployeeServiceImpl.java` 实现业务逻辑
4. 在 `EmployeeMapper.java` 添加方法签名
5. 在 `EmployeeMapper.xml` 添加 SQL

### 优先级2：分类管理
1. 创建 `CategoryController.java`
2. 创建 `CategoryService.java` 接口
3. 创建 `CategoryServiceImpl.java` 实现类
4. 创建 `CategoryMapper.java` 接口
5. 创建 `CategoryMapper.xml` SQL 配置

### 优先级3：套餐管理
1. 创建 `SetmealController.java`（暂时返回模拟数据）
2. 后续实现完整的 Service 和 Mapper

### 优先级4：数据统计
1. 创建 `StatisticsController.java`（暂时返回模拟数据）
2. 后续接入真实数据库统计

---

## ✅ 完成后测试

启动后端，访问：
- http://localhost:8080/admin/employee/list
- http://localhost:8080/admin/category/list
- http://localhost:8080/admin/setmeal/list
- http://localhost:8080/admin/statistics/turnover?begin=2025-01-01&end=2025-01-31

---

这样前端就可以正常调用这些接口了！
