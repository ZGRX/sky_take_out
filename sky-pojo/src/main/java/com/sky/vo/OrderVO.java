package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO implements Serializable {
    
    private Long id;
    
    private String number;
    
    private Integer status;
    
    private Long userId;
    
    private LocalDateTime orderTime;
    
    private LocalDateTime checkoutTime;
    
    private Integer payMethod;
    
    private BigDecimal amount;
    
    private String remark;
    
    private String phone;
    
    private String address;
    
    private String consignee;
    
}
