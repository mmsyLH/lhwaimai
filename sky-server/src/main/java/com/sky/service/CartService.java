package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

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
}
