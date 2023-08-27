package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.OrderDetail;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
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
@RestController("AdminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "客户端-订单相关接口")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 订单搜索
     *
     * @param ordersPageQueryDTO 订单查询页面dto
     * @return {@link Result}<{@link PageResult}>
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> orderSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        PageResult pageResult = orderService.orderSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 通过Id查询订单详情
     *
     * @param id id
     * @return {@link Result}<{@link OrderVO}>
     */
    @GetMapping("details/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> orderSearchDetail(@PathVariable Long id){
        OrderVO orderVO= orderService.queryOrderDetailById(id);
        return Result.success(orderVO);
    }
}
