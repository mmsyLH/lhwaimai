package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 订单明细表
 *
 * @author 罗汉
 * @date 2023/08/24
 */
@Mapper
public interface OrderDetailMapper {
    /**
     * 批量插入订单明细数据
     *
     * @param orderDetails 订单细节
     */
    void insertBatch(List<OrderDetail> orderDetails);
    /**
     * 根据订单id查询订单明细
     * @param orderId
     * @return
     */
    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> getByOrderId(Long orderId);
}
