package com.sky.controller.admin;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 套餐管理相关接口
 *
 * @author 罗汉
 * @date 2023/08/10
 */
@RestController//@RestController 是一个 Spring MVC 注解，它的作用是将一个类标记为 RESTful Web 服务的控制器。
// 与传统的 Spring MVC控制器不同，@RestController 控制器的每个方法都会将返回值
// 直接转换为 JSON 或 XML 格式的响应体，而无需使用视图解析器进行视图渲染。
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")//描述当前类的作用
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     *
     * @return {@link Result}<{@link String}>
     */
    @PostMapping("")
    @ApiOperation(value = "新增套餐")//用在方法上的描述注解
    public Result<String> save(@RequestBody SetmealDTO setmealDTO) {

        System.out.println("当前线程id："+Thread.currentThread().getId());

        log.info("新增套餐：{}",setmealDTO );
        setmealService.save(setmealDTO);
        return Result.success();
    }
}
