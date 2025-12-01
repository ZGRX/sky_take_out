package com.sky.controller.admin;

import com.sky.entity.Orders;
import com.sky.result.Result;
import com.sky.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 管理端订单Controller
 */
@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "管理端-订单管理接口")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    /**
     * 查询所有订单
     */
    @GetMapping("/list")
    @ApiOperation("查询所有订单")
    public Result<List<Orders>> list() {
        log.info("查询所有订单");
        List<Orders> list = orderService.list();
        return Result.success(list);
    }
    
    /**
     * 接单
     */
    @PutMapping("/confirm/{id}")
    @ApiOperation("接单")
    public Result confirm(@PathVariable Long id) {
        log.info("接单：{}", id);
        orderService.confirm(id);
        return Result.success();
    }
    
    /**
     * 拒单
     */
    @PutMapping("/rejection/{id}")
    @ApiOperation("拒单")
    public Result rejection(@PathVariable Long id) {
        log.info("拒单：{}", id);
        orderService.rejection(id);
        return Result.success();
    }
    
    /**
     * 完成订单
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable Long id) {
        log.info("完成订单：{}", id);
        orderService.complete(id);
        return Result.success();
    }
    
}
