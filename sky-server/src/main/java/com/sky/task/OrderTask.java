package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单任务
 * 处理订单超时
 *
 * @author 罗汉
 * @date 2023/08/27
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;
    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 * * * * ?")//每分钟触发
    private void processTimeOutOrder(){
        log.info("定时处理订单：{}", LocalDateTime.now());
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-15);
        //select * from orders where status=? and order_time<(当前时间-15分钟)
        //查询
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, localDateTime);

        if (ordersList!=null && ordersList.size()>0){
            for (Orders orders : ordersList) {
                  orders.setStatus(Orders.CANCELLED);
                  orders.setCancelReason("订单超时,自动取消");//取消原因
                  orders.setCancelTime(LocalDateTime.now());
                  orderMapper.update(orders);
            }
        }

    }

    /**
     * 处理一直处于派送中的订单
     */
    @Scheduled(cron="0 0 1 * * ?")//每天凌晨1点触发一次
    private void processDeliveryOrder( ){

    }
}
