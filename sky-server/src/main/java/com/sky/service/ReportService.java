package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.format.annotation.DateTimeFormat;

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
    TurnoverReportVO getTurnoverStatistics(LocalDate begin,LocalDate end);

    /**
     * 获得指定时间范围用户统计
     * @param begin
     * @param end
     * @return {@link UserReportVO}
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);
}
