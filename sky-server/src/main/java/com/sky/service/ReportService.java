package com.sky.service;

import com.sky.vo.TurnoverReportVO;
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
}
