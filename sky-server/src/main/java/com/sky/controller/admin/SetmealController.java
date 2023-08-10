package com.sky.controller.admin;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO 套餐分页查询dto
     * @return {@link Result}<{@link PageResult}>
     */
    @GetMapping("/page")
    @ApiOperation(value = "套餐分页查询")//
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){//springMVC会自动封装好
        log.info("套餐分页查询参数：{}",setmealPageQueryDTO );

        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return  Result.success(pageResult);
    }
    @PostMapping("status/{status}")
    @ApiOperation(value = "起售停售套餐")//
    public Result startOrStop(@PathVariable Integer status,Long id){
        // public Result startOtStop(@PathVariable("status") Integer status,Long id){//变量同名可以省略
        log.info("起售停售套餐:{},{}",status,id);
        setmealService.startOrStop(status,id);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除套餐")
    public Result delete(@RequestParam List<Long> ids){
        log.info("批量删除套餐：{}",ids);
        setmealService.delete(ids);
        return Result.success();
    }
}
