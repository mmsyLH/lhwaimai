package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 我任务
 *
 * @author 罗汉
 * @date 2023/08/27
 */
@Component
@Slf4j
public class MyTask {

    // /**
    //  * 定时任务，每5秒触发一次
    //  */
    // @Scheduled(cron = "0/5 * * * * ?")
    // public void text1(){
    //     log.info("人物执行,{}",new Date());
    // }
}
