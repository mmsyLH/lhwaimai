package com.sky.mapper;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderSubmitVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author :罗汉
 * @date : 2023/8/24
 */
@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     *
     * @param orders 订单
     */
    void insert(Orders orders);
}
