package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author :罗汉
 * @date : 2023/8/5
 */
@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口味数据
     *
     * @param flavors 口味
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品id删除对应的口味数据
     *
     * @param dishId id
     */
    @Delete("delete from dish_flavor where dish_id=#{dishId}")
    void deleteByDishIds(Long dishId);

    /**
     * 通过菜品id查询口味
     *
     * @param dishId id
     * @return {@link List}<{@link DishFlavor}>
     */
    @Select("select  * from dish_flavor where dish_id=#{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
