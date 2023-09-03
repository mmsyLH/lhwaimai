package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
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
        //存放每天日期的集合
        List<LocalDate> datalist=new ArrayList<>();

        datalist.add(begin);

        while (!begin.equals(end)){
            //日期计算，计算指定日期的后一天对应的日期
            begin = begin.plusDays(1);//一天一天的往后移
            datalist.add(begin);
        }
        String str = StringUtils.join(datalist, ",");


        List<Double> turnoverList = new ArrayList<>();//存放每天的营业额
        for (LocalDate date : datalist) {
            //营业额  状态为已完成的订单金额合计0.00-23.59
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN); //2023.9.1 0:00
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX); //2023.9.1 23:59


            //select sum(amount) from orders whrere date order_time >? and order_time <? and status =5
            Map map = new HashMap<>();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);//已完成
            Double turnover= orderMapper.sumByMap(map);
            if (turnover==null){
                turnover=0.0;
            }
            turnoverList.add(turnover);

        }

        //封装返回结果
        return TurnoverReportVO
                .builder()
                .dateList(str)//lang3的包
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }
}
