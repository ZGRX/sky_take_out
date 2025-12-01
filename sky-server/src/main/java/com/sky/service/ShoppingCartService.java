package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import java.util.List;

/**
 * 购物车Service接口
 */
public interface ShoppingCartService {
    
    /**
     * 添加购物车
     */
    void add(ShoppingCartDTO shoppingCartDTO);
    
    /**
     * 查看购物车
     */
    List<ShoppingCart> showShoppingCart();
    
    /**
     * 清空购物车
     */
    void clean();
    
    /**
     * 删除购物车中一个商品
     */
    void sub(ShoppingCartDTO shoppingCartDTO);
    
}
