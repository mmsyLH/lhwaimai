package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.format.annotation.DateTimeFormat;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * @author :罗汉
 * @date : 2023/8/30
 */
public interface ReportService {
    /**
     * 获得指定时间范围营业额统计
     *
     * @return {@link TurnoverReportVO}
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 获得指定时间范围用户统计
     *
     * @param begin
     * @param end
     * @return {@link UserReportVO}
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 获得指定时间范围的订单统计
     * @param begin
     * @param end
     * @return {@link OrderReportVO}
     */
    OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end);

    /**
     * 商品销量排行前十10统计
     * @param begin
     * @param end
     * @return {@link SalesTop10ReportVO}
     */
    SalesTop10ReportVO getDishStatistics(LocalDate begin, LocalDate end);

    /**
     * 导出运营数据报表
     * @param httpServletResponse
     */
    void export(HttpServletResponse httpServletResponse);
}
