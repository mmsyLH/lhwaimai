package com.sky.service;

import com.sky.dto.DishDTO;

/**
 * 盘服务
 *
 * @author 罗汉
 * @date 2023/08/05
 */
public interface DishService {
    /**
     * 新增菜品和口味数据
     *
     * @param dishDTO 菜dto
     */
    public void  saveWishFlavor(DishDTO dishDTO);
}
