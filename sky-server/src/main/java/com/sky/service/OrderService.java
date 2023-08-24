package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;

/**
 * 订单服务
 *
 * @author 罗汉
 * @date 2023/08/24
 */
public interface OrderService {

    /**
     * 提交订单
     *
     * @param ordersSubmitDTO 订单提交dto
     * @return {@link OrderSubmitVO}
     */
    OrderSubmitVO sumbitOrder(OrdersSubmitDTO ordersSubmitDTO);
}
