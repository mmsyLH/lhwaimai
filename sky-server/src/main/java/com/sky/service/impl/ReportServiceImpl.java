package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private WorkspaceService workspaceService;

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
        return TurnoverReportVO.builder().dateList(str)// lang3的包
                .turnoverList(StringUtils.join(turnoverList, ",")).build();
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
        UserReportVO buildVO = UserReportVO.builder().dateList(daTeStr).newUserList(newUserStr).totalUserList(totalUserStr).build();
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
        Double res = 0.0;
        if (totalOrderCount != 0) {
            res = validOrderCount.doubleValue() / totalOrderCount;
        }

        // 4 封装结果返回
        String daTeStr = StringUtils.join(datalist, ",");
        String orderCountListStr = StringUtils.join(orderCountList, ",");
        String validOrderCountListStr = StringUtils.join(validOrderCountList, ",");
        OrderReportVO build = OrderReportVO.builder().dateList(daTeStr).orderCountList(orderCountListStr).validOrderCountList(validOrderCountListStr).validOrderCount(validOrderCount).totalOrderCount(totalOrderCount).orderCompletionRate(res).build();
        return build;
    }

    /**
     * 商品销量排行前十10统计
     *
     * @param begin
     * @param end
     * @return {@link SalesTop10ReportVO}
     */
    @Override
    public SalesTop10ReportVO getDishStatistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN); // 2023.9.1 0:00
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX); // 2023.9.1 23:59
        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);
        // 格式处理  list转vo
        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        String nameStr = StringUtils.join(names, ",");
        String numbersStr = StringUtils.join(numbers, ",");
        return SalesTop10ReportVO.builder().nameList(nameStr).numberList(numbersStr).build();

    }


    /**
     * 导出运营数据报表
     *
     * @param httpServletResponse
     */
    @Override
    public void export(HttpServletResponse httpServletResponse) {
        // 1 查询数据库获取营业数据 30天
        // 1.1 查询最近30天的数据
        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate dateEnd = LocalDate.now().minusDays(1);

        // 查询概览数据
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(dateEnd, LocalTime.MAX));

        //2. 通过POI将数据写入到Excel文件中
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");


        try {
            // 基于模版文件创建一个新的Excel文件
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);

            // 2.1 填充数据------时间
            // 获取表格文件的sheet1标签页
            XSSFSheet sheet1 = excel.getSheet("Sheet1");
            // 获取行 第二行
            sheet1.getRow(1).getCell(1).setCellValue("时间:" + dateBegin + "至" + dateEnd);

            // 2.2 填充-----概览数据

            // 获得第四行
            XSSFRow row = sheet1.getRow(3);
            row.getCell(2).setCellValue(businessDataVO.getTurnover());   // 设置营业额
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());  // 填充订单完成率
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());   // 新增用户数
            // 获得第5行
            row = sheet1.getRow(4);
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());// 有效订单
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());// 平均客单价

            // 2.3 填充明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = dateBegin.plusDays(i);
                // 查询某一天的营业数据
                BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                // 获得某一行
                row = sheet1.getRow(7 + i);// 第一次遍历就是第八行 下表为7
                row.getCell(1).setCellValue(date.toString());//第2个单元格
                row.getCell(2).setCellValue(businessData.getTurnover());//第3个单元格
                row.getCell(3).setCellValue(businessData.getValidOrderCount());//第3个单元格
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());//第3个单元格
                row.getCell(5).setCellValue(businessData.getUnitPrice());//第3个单元格
                row.getCell(6).setCellValue(businessData.getNewUsers());//第3个单元格
            }


            // 3 通过输出流将excel文件下载到客户端浏览器
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            excel.write(outputStream);

            // 4 关闭资源
            outputStream.close();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 根据条件统计订单数量
     *
     * @param begin
     * @param end
     * @param status
     * @return {@link Integer}
     */
    private Integer getOrderCount(LocalDateTime begin, LocalDateTime end, Integer status) {
        Map map = new HashMap();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", status);
        return orderMapper.countByMap(map);
    }
}
