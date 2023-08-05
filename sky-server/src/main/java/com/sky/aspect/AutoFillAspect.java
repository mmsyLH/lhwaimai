package com.sky.aspect;

import com.sky.annotaction.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面类
 * 实现自动填充逻辑
 *
 * @author :罗汉
 * @date : 2023/8/4
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 指定切入点
     * execution(* com.sky.mapper.*.*(..)) 里面的所有类所有方法所有参数都拦截
     * 满足2个条件
     * 1 在这个包下的全部方法
     * 2 还要满足这个注解
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotaction.AutoFill)")
    public void autoFillPointCut() {
    }

    /**
     * 需要前置通知：因为在执行方法之前 需要给字段赋值
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段的填充...");

        // 1 获取被拦截注解的操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();// 方法签名对象
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);// 获得方法上的注解对象
        OperationType operationType = annotation.value();// 获得数据库操作类型   update或者insert

        // 2 获取被拦截的实体对象的参数
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        Object entiy = args[0];

        // 3 准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();


        // 4 通过反射赋值
        if (operationType == OperationType.INSERT) {
            // 为4个公共字段赋值
            try {
                Method setCreateTime = entiy.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entiy.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entiy.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entiy.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                // 反射赋值
                setCreateTime.invoke(entiy, now);
                setCreateUser.invoke(entiy, currentId);
                setUpdateTime.invoke(entiy, now);
                setUpdateUser.invoke(entiy, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (operationType == OperationType.UPDATE) {
            // 为2个公共字段赋值
            try {
                Method  setUpdateTime = entiy.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entiy.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                // 反射赋值
                setUpdateTime.invoke(entiy, now);
                setUpdateUser.invoke(entiy, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
