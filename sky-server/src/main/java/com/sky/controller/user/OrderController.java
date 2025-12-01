package com.sky.controller.user;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 用户端订单Controller
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "用户端-订单接口")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    /**
     * 用户下单
     */
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单：{}", ordersSubmitDTO);
        OrderVO orderVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderVO);
    }
    
    /**
     * 查询用户订单
     */
    @GetMapping("/list")
    @ApiOperation("查询用户订单")
    public Result<List<Orders>> list() {
        log.info("查询用户订单");
        List<Orders> list = orderService.getByUserId();
        return Result.success(list);
    }
    
}
