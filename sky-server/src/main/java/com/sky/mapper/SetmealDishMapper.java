package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author :罗汉
 * @date : 2023/8/8
 */
@Mapper
public interface SetmealDishMapper {
    /**
     * 据菜品Id查询菜单id
     *
     * @param dishIds 菜id
     * @return {@link List}<{@link Long}>
     */
    //select setmeal id from setmeal dis where dish_id in(1,2,3,4,5)
    List<Long> getSetmealIdsByDishIds(List<Long>dishIds);

    /**
     * 批量保存套餐和菜品的关联关系
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);
}
