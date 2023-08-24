package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * @author :罗汉
 * @date : 2023/8/22
 */
public interface CartService {
    /**
     * 添加购物车
     *
     * @param shoppingCartDTO 购物车dto
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 显示购物车
     *
     * @return {@link List}<{@link ShoppingCart}>
     */
    List<ShoppingCart> showCart();

    /**
     * 清空购物车
     */
    void cleanCart();
}
