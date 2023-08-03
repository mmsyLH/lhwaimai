package com.sky.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页查询结果
 *
 * @author 罗汉
 * @date 2023/08/03
 */
@Data//自动生成getter、setter、equals、hashCode、toString等方法。
@AllArgsConstructor//生成一个包含所有成员变量的构造方法。
@NoArgsConstructor//生成一个无参构造方法。
public class PageResult implements Serializable {

    private long total; //总记录数

    private List records; //当前页数据集合

}
