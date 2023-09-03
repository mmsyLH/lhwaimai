package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotaction.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 盘映射器
 *
 * @author 罗汉
 * @date 2023/08/04
 */
@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品数据
     *
     * @param dish 菜
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品页面分页查询
     *
     * @param dishPageQueryDTO 菜页面查询dto
     * @return {@link Page}<{@link DishVO}>
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 通过id查询菜品
     *
     * @param id id
     * @return {@link Dish}
     */
    @Select("select *from dish where id=#{id}")
    Dish getById(Long id);

    /**
     * 删除通过id
     *
     * @param id id
     */
    @Delete("delete from dish where id=#{id}")
    void deleteById(Long id);

    /**
     * 根据id来动态修改菜品数据
     *
     * @param dish 菜
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);
    /**
     * 动态条件查询菜品
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
