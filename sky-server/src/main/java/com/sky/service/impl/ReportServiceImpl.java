package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :罗汉
 * @date : 2023/8/30
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 获得指定时间范围营业额统计
     *
     * @param begin
     * @param end
     * @return {@link TurnoverReportVO}
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {

        // 1 日期的计算
        // 存放每天日期的集合
        List<LocalDate> datalist = new ArrayList<>();

        datalist.add(begin);

        while (!begin.equals(end)) {
            // 日期计算，计算指定日期的后一天对应的日期
            begin = begin.plusDays(1);// 一天一天的往后移
            datalist.add(begin);
        }
        String str = StringUtils.join(datalist, ",");


        List<Double> turnoverList = new ArrayList<>();// 存放每天的营业额
        for (LocalDate date : datalist) {
            // 营业额  状态为已完成的订单金额合计0.00-23.59
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN); // 2023.9.1 0:00
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX); // 2023.9.1 23:59


            // select sum(amount) from orders whrere date order_time >? and order_time <? and status =5
            Map map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);// 已完成
            Double turnover = orderMapper.sumByMap(map);
            if (turnover == null) {
                turnover = 0.0;
            }
            turnoverList.add(turnover);

        }

        // 封装返回结果
        return TurnoverReportVO
                .builder()
                .dateList(str)// lang3的包
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 获得指定时间范围用户统计
     *
     * @param begin
     * @param end
     * @return {@link UserReportVO}
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {

        // 1 日期的计算
        // 存放每天日期的集合
        List<LocalDate> datalist = new ArrayList<>();

        datalist.add(begin);

        while (!begin.equals(end)) {
            // 日期计算，计算指定日期的后一天对应的日期
            begin = begin.plusDays(1);// 一天一天的往后移
            datalist.add(begin);
        }


        // 2 用户的统计
        List<Integer> newUserList = new ArrayList<>();// 存放每天的新增用户数量
        List<Integer> totalUserList = new ArrayList<>();// 存放每天的总增用户数量 select count(id) from user where create_time< ?
        for (LocalDate date : datalist) {
            // 营业额  状态为已完成的订单金额合计0.00-23.59
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN); // 2023.9.1 0:00
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX); // 2023.9.1 23:59
            // 建议先查用户总数量
            Map map = new HashMap<>();
            Integer totalUser = userMapper.countByMap(map);

            // 新增用户数量
            map.put("begin", beginTime);
            Integer newUser = userMapper.countByMap(map);

            totalUserList.add(totalUser);
            newUserList.add(newUser);
        }
        // 3 封装结果返回
        String daTeStr = StringUtils.join(datalist, ",");
        String totalUserStr = StringUtils.join(totalUserList, ",");
        String newUserStr = StringUtils.join(newUserList, ",");
        UserReportVO buildVO = UserReportVO.builder()
                .dateList(daTeStr)
                .newUserList(newUserStr)
                .totalUserList(totalUserStr)
                .build();
        return buildVO;
    }

    /**
     * 获得指定时间范围的订单统计
     *
     * @param begin
     * @param end
     * @return {@link OrderReportVO}
     */
    @Override
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {

        // 1 日期的计算
        // 存放每天日期的集合
        List<LocalDate> datalist = new ArrayList<>();

        datalist.add(begin);

        while (!begin.equals(end)) {
            // 日期计算，计算指定日期的后一天对应的日期
            begin = begin.plusDays(1);// 一天一天的往后移
            datalist.add(begin);
        }

        List<Integer> orderCountList = new ArrayList<>();// 存放每天的总订单数量
        List<Integer> validOrderCountList = new ArrayList<>();// 存放每天的有效订单数量
        // 2 遍历查询每天的有效订单数和订单总数
        for (LocalDate date : datalist) {

            // 营业额  状态为已完成的订单金额合计0.00-23.59
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN); // 2023.9.1 0:00
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX); // 2023.9.1 23:59

            // 查询每天的订单总数 select count from orders where order_time > ? and order_time<?
            Integer orderCount = getOrderCount(beginTime, endTime, null);
            orderCountList.add(orderCount);

            // 查询每天的有效订单数 select count from orders where order_time > ? and order_time<? and status = 5
            Integer validOrderCount = getOrderCount(beginTime, endTime, Orders.COMPLETED);
            validOrderCountList.add(validOrderCount);

        }
        // 3 计算时间区间内的总数量
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();

        // 3 计算时间区间内的有效订单总数
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();

        // 计算订单完成率
        Double res=0.0;
        if (totalOrderCount!=0){
            res= validOrderCount.doubleValue()/totalOrderCount;
        }

        // 4 封装结果返回
        String daTeStr = StringUtils.join(datalist, ",");
        String orderCountListStr = StringUtils.join(orderCountList, ",");
        String validOrderCountListStr = StringUtils.join(validOrderCountList, ",");
        OrderReportVO build = OrderReportVO.builder()
                .dateList(daTeStr)
                .orderCountList(orderCountListStr)
                .validOrderCountList(validOrderCountListStr)
                .validOrderCount(validOrderCount)
                .totalOrderCount(totalOrderCount)
                .orderCompletionRate(res)
                .build();
        return build;
    }

    /**
     * 根据条件统计订单数量
     * @param begin
     * @param end
     * @param status
     * @return {@link Integer}
     */
    private Integer getOrderCount(LocalDateTime begin,LocalDateTime end,Integer status) {
        Map map=new HashMap();
        map.put("begin",begin);
        map.put("end",end);
        map.put("status",status);
       return orderMapper.countByMap(map);
    }
}
