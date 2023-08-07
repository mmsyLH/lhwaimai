package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

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

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO 菜页面查询dto
     * @return {@link PageResult}
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品的批量删除功能
     *
     * @param ids id
     */
    void delete(List<Long> ids);
}
