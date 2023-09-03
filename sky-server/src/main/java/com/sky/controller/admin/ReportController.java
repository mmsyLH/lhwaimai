package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * 数据统计相关接口
 *
 * @author 罗汉
 * @date 2023/08/30
 */
@RestController()
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "数据统计相关接口")
public class ReportController {
    @Autowired
    private ReportService reportService;

    /**
     * 获得营业额报告
     * 通过注解描述日期格式
     * @return {@link Result}<{@link TurnoverReportVO}>
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> getTurnoverReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
        // log.info("营业额统计的时间范围为开始：{}",begin);
        // log.info("营业额统计的时间范围结束为：{}",end);
        TurnoverReportVO turnoverStatistics = reportService.getTurnoverStatistics(begin, end);
        return Result.success(turnoverStatistics);
    }
    @GetMapping("/userStatistics")
    @ApiOperation("用户额统计")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
        log.info("营业额统计的时间范围为开始：{},{}",begin,end);
        UserReportVO userReportVO = reportService.getUserStatistics(begin, end);
        return Result.success(userReportVO);
    }
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计")
    public Result<OrderReportVO> ordersStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
        log.info("订单统计的时间范围为开始：{},{}",begin,end);
        OrderReportVO orderReportVO = reportService.getOrdersStatistics(begin, end);
        return Result.success(orderReportVO);
    }

    /**
     * 商品销量前10查询
     * @param begin
     * @param end
     * @return {@link Result}<{@link OrderReportVO}>
     */
    @GetMapping("/top10")
    @ApiOperation("销量排名前10统计")
    public Result<SalesTop10ReportVO> salesTop10(
            @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
        log.info("销量排名前10统计时间范围为：{},{}",begin,end);
        SalesTop10ReportVO salesTop10ReportVO = reportService.getDishStatistics(begin, end);
        return Result.success(salesTop10ReportVO);
    }

    /**
     * 导出运营数据报表
     * @return {@link Result}
     */
    @GetMapping("/export")
    @ApiOperation("导出运营数据报表")
    public void export(HttpServletResponse httpServletResponse){
        reportService.export(httpServletResponse);
    }
}
