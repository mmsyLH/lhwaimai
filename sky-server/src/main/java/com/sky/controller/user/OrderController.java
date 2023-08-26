package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.OrderDetail;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author :罗汉
 * @date : 2023/8/24
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "C端-订单相关接口")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> sumbit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单的参数为,{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO=orderService.sumbitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }
    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * 页面
     *
     * @param page     页面
     * @param pageSize 页面大小
     * @param status   订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
     * @return {@link Result}<{@link PageResult}>
     */
    @GetMapping("/historyOrders")
    @ApiOperation("分页查询历史订单")
    public Result<PageResult> page(int page,int pageSize,Integer status)   {
        log.info("分页查询历史订单信息,{}",page,pageSize,status);
        PageResult pageResult= orderService.page(page,pageSize,status);
        return Result.success(pageResult);
    }

    @GetMapping("orderDetail/{id}")
    @ApiOperation("根据订单id查询订单详情")
    public Result<OrderVO> queryOrderDetailById(@PathVariable Long id){
        log.info("根据订单id查询订单详情打的id为,{}",id);
        OrderVO orderVO=   orderService.queryOrderDetailById(id);
        return Result.success(orderVO);
    }


}
