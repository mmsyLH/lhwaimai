package com.sky.annotaction;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解 用于标识某个方法需要进行功能字段自动填充处理
 * @author :罗汉
 * @date : 2023/8/4
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //通过枚举指定类型  数据库操作类型
    OperationType value();
}
