package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 订单提交DTO
 */
@Data
public class OrdersSubmitDTO implements Serializable {
    
    // 收货人
    private String consignee;
    
    // 手机号
    private String phone;
    
    // 地址
    private String address;
    
    // 备注
    private String remark;
    
    // 支付方式
    private Integer payMethod;
    
}
