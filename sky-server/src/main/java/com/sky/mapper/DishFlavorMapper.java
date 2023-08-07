package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

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
}
