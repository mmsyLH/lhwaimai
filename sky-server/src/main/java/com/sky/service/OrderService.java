package com.sky.service;

import com.sky.dto.*;
import com.sky.entity.OrderDetail;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

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

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 历史订单分页查询
     *
     * @param page     页面
     * @param pageSize 页面大小
     * @param status   状态
     * @return {@link PageResult}
     */
    PageResult page(int page, int pageSize, Integer status);

    /**
     * 根据订单id查询订单详情
     *
     * @param id id
     * @return {@link OrderDetail}
     */
    OrderVO queryOrderDetailById(Long id);

    /**
     * 取消订单
     *
     * @param id id
     */
    void cancelOrder(Long id);

    /**
     * 再一次订单
     *
     * @param id id
     */
    void againOrder(Long id);

    /**
     * 订单搜索
     *
     * @param ordersPageQueryDTO 订单查询页面dto
     * @return {@link PageResult}
     */
    PageResult orderSearch(OrdersPageQueryDTO ordersPageQueryDTO);
    /**
     * 各个状态的订单数量统计
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * 接单
     *
     * @param ordersConfirmDTO 订单确认dto
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒绝接单
     *
     * @param ordersRejectionDTO 订单被拒绝dto
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 取消订单
     *
     * @param ordersCancelDTO 订单取消dto
     */
    void cancel(OrdersCancelDTO ordersCancelDTO);

    /**
     * 派送订单
     *
     * @param id id
     */
    void delivery(Long id);

    /**
     * 完成订单
     *
     * @param id id
     */
    void complete(Long id);

    /**
     * 客户催单
     *
     * @param id id
     */
    void reminder(long id);
}
