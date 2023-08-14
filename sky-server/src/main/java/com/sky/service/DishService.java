package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

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

    /**
     * 通过id获取菜品与味道
     *
     * @param id id
     * @return {@link DishVO}
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 起售与禁售菜品
     *
     * @param status 状态
     * @param id     id
     */
    void startOtStop(Integer status, Long id);

    /**
     * 更新菜品与味道
     *
     * @param dishDTO 菜dto
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);


    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
