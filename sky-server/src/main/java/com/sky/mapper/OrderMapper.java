package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 订单Mapper
 */
@Mapper
public interface OrderMapper {
    
    /**
     * 插入订单数据
     */
    @Insert("insert into orders (number, status, user_id, address_book_id, order_time, checkout_time, pay_method, " +
            "amount, remark, phone, address, consignee) " +
            "values (#{number}, #{status}, #{userId}, #{addressBookId}, #{orderTime}, #{checkoutTime}, #{payMethod}, " +
            "#{amount}, #{remark}, #{phone}, #{address}, #{consignee})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Orders orders);
    
    /**
     * 根据用户id查询订单
     */
    @Select("select * from orders where user_id = #{userId} order by order_time desc")
    List<Orders> getByUserId(Long userId);
    
    /**
     * 根据id查询订单
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);
    
    /**
     * 查询所有订单
     */
    @Select("select * from orders order by order_time desc")
    List<Orders> list();
    
    /**
     * 更新订单状态
     */
    @Update("update orders set status = #{status} where id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
}
