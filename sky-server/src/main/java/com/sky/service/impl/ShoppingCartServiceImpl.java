package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车Service实现类
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    
    @Autowired
    private DishMapper dishMapper;
    
    /**
     * 添加购物车
     */
    public void add(ShoppingCartDTO shoppingCartDTO) {
        // 判断当前加入购物车的商品是否已经存在
        ShoppingCart shoppingCart = new ShoppingCart();
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        
        Long dishId = shoppingCartDTO.getDishId();
        
        // 查询购物车中是否已有该商品
        ShoppingCart cart = shoppingCartMapper.getByUserIdAndDishId(userId, dishId);
        
        if (cart != null) {
            // 如果已存在，数量加1
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumber(cart);
        } else {
            // 如果不存在，需要插入一条购物车数据
            // 查询菜品信息
            Dish dish = dishMapper.getById(dishId);
            
            shoppingCart.setName(dish.getName());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setDishId(dishId);
            shoppingCart.setNumber(1);
            shoppingCart.setAmount(dish.getPrice());
            shoppingCart.setCreateTime(LocalDateTime.now());
            
            shoppingCartMapper.insert(shoppingCart);
        }
    }
    
    /**
     * 查看购物车
     */
    public List<ShoppingCart> showShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        return shoppingCartMapper.getByUserId(userId);
    }
    
    /**
     * 清空购物车
     */
    public void clean() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }
    
    /**
     * 删除购物车中一个商品
     */
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();
        Long dishId = shoppingCartDTO.getDishId();
        
        // 查询购物车中的商品
        ShoppingCart cart = shoppingCartMapper.getByUserIdAndDishId(userId, dishId);
        
        if (cart != null) {
            Integer number = cart.getNumber();
            if (number == 1) {
                // 如果数量为1，直接删除
                shoppingCartMapper.deleteById(cart.getId());
            } else {
                // 如果数量大于1，数量减1
                cart.setNumber(number - 1);
                shoppingCartMapper.updateNumber(cart);
            }
        }
    }
    
}
