package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.config.JwtProperties;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理Controller
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private JwtProperties jwtProperties;
    
    /**
     * 登录
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);
        
        Employee employee = employeeService.login(employeeLoginDTO);
        
        // 登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);
        
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .username(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
        
        return Result.success(employeeLoginVO);
    }
    
    /**
     * 退出
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result<String> logout() {
        return Result.success();
    }
    
    /**
     * 查询员工列表
     */
    @GetMapping("/list")
    @ApiOperation("查询员工列表")
    public Result<List<Employee>> list(@RequestParam(required = false) String name) {
        log.info("查询员工列表，name={}", name);
        // 暂时返回空列表，等待 Service 实现
        List<Employee> list = new ArrayList<>();
        return Result.success(list);
    }
    
    /**
     * 新增员工
     */
    @PostMapping
    @ApiOperation("新增员工")
    public Result save(@RequestBody Employee employee) {
        log.info("新增员工：{}", employee);
        // TODO: 调用 Service
        return Result.success();
    }
    
    /**
     * 根据ID查询员工
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询员工")
    public Result<Employee> getById(@PathVariable Long id) {
        log.info("根据ID查询员工：{}", id);
        // TODO: 调用 Service
        return Result.success(null);
    }
    
    /**
     * 修改员工
     */
    @PutMapping
    @ApiOperation("修改员工")
    public Result update(@RequestBody Employee employee) {
        log.info("修改员工：{}", employee);
        // TODO: 调用 Service
        return Result.success();
    }
    
    /**
     * 启用/停用员工账号
     */
    @PutMapping("/status/{status}")
    @ApiOperation("启用/停用员工账号")
    public Result updateStatus(@PathVariable Integer status, @RequestParam Long id) {
        log.info("启用/停用员工账号：id={}, status={}", id, status);
        // TODO: 调用 Service
        return Result.success();
    }
    
}
