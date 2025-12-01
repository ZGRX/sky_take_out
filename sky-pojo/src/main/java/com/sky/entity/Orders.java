package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // 订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
    public static final Integer PENDING_PAYMENT = 1;
    public static final Integer TO_BE_CONFIRMED = 2;
    public static final Integer CONFIRMED = 3;
    public static final Integer DELIVERY_IN_PROGRESS = 4;
    public static final Integer COMPLETED = 5;
    public static final Integer CANCELLED = 6;
    
    private Long id;
    
    // 订单号
    private String number;
    
    // 订单状态
    private Integer status;
    
    // 用户id
    private Long userId;
    
    // 地址id
    private Long addressBookId;
    
    // 下单时间
    private LocalDateTime orderTime;
    
    // 结账时间
    private LocalDateTime checkoutTime;
    
    // 支付方式 1微信支付 2支付宝
    private Integer payMethod;
    
    // 实收金额
    private BigDecimal amount;
    
    // 备注
    private String remark;
    
    // 手机号
    private String phone;
    
    // 地址
    private String address;
    
    // 收货人
    private String consignee;
    
}
