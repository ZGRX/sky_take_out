package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单Service实现类
 */
@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    
    /**
     * 用户下单
     */
    @Transactional
    public OrderVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        Long userId = BaseContext.getCurrentId();
        
        // 查询当前用户的购物车数据
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.getByUserId(userId);
        
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new RuntimeException("购物车为空，无法下单");
        }
        
        // 构造订单数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setNumber(String.valueOf(System.currentTimeMillis())); // 订单号
        orders.setStatus(Orders.PENDING_PAYMENT); // 待付款
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        
        // 计算总金额
        BigDecimal totalAmount = new BigDecimal(0);
        for (ShoppingCart cart : shoppingCartList) {
            totalAmount = totalAmount.add(cart.getAmount().multiply(new BigDecimal(cart.getNumber())));
        }
        orders.setAmount(totalAmount);
        
        // 插入订单数据
        orderMapper.insert(orders);
        
        // 构造订单明细数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        
        // 批量插入订单明细数据
        orderDetailMapper.insertBatch(orderDetailList);
        
        // 清空购物车
        shoppingCartMapper.deleteByUserId(userId);
        
        // 封装返回结果
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        
        return orderVO;
    }
    
    /**
     * 查询用户订单
     */
    public List<Orders> getByUserId() {
        Long userId = BaseContext.getCurrentId();
        return orderMapper.getByUserId(userId);
    }
    
    /**
     * 查询所有订单（管理端）
     */
    public List<Orders> list() {
        return orderMapper.list();
    }
    
    /**
     * 接单
     */
    public void confirm(Long id) {
        orderMapper.updateStatus(id, Orders.CONFIRMED);
    }
    
    /**
     * 拒单
     */
    public void rejection(Long id) {
        orderMapper.updateStatus(id, Orders.CANCELLED);
    }
    
    /**
     * 完成订单
     */
    public void complete(Long id) {
        orderMapper.updateStatus(id, Orders.COMPLETED);
    }
    
}
