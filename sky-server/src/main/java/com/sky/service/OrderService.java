package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import java.util.List;

/**
 * 订单Service接口
 */
public interface OrderService {
    
    /**
     * 用户下单
     */
    OrderVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
    
    /**
     * 查询用户订单
     */
    List<Orders> getByUserId();
    
    /**
     * 查询所有订单（管理端）
     */
    List<Orders> list();
    
    /**
     * 接单
     */
    void confirm(Long id);
    
    /**
     * 拒单
     */
    void rejection(Long id);
    
    /**
     * 完成订单
     */
    void complete(Long id);
    
}
