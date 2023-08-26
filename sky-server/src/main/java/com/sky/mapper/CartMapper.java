package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :罗汉
 * @date : 2023/8/22
 */
@Mapper
public interface CartMapper {
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 更新数量通过id
     *
     * @param shoppingCart 购物车
     */
    @Update("update shopping_cart set number=#{number} where id =#{id}")
    void updateNumberById(ShoppingCart shoppingCart);


    /**
     * 插入购物车数据
     *
     * @param shoppingCart 购物车
     */
    @Insert("insert into shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) " +
            "values (#{name}, #{image},#{userId}, #{dishId},#{setmealId}, #{dishFlavor},#{number}, #{amount}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 按用户id删除购物车数据
     *
     * @param userId 用户id
     */
    @Delete("delete  from shopping_cart where user_id=#{userId}")
    void deleteByUserId(Long userId);

    /**
     * 通过用户id得到购物车
     *
     * @param userId 用户id
     * @return {@link ShoppingCart}
     */
    @Select("select * from shopping_cart where user_id=#{userId}")
    ShoppingCart getCartByUserId(Long userId);
    /**
     * 批量插入购物车数据
     *
     * @param shoppingCartList
     */
    void insertBatch(List<ShoppingCart> shoppingCartList);
}
