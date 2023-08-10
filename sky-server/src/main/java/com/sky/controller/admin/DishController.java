package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.EmployeeDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author :罗汉
 * @date : 2023/8/5
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}",dishDTO);
        dishService.saveWishFlavor(dishDTO);

        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询:{}",dishPageQueryDTO);
        PageResult pageResult= dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     *  菜品批量删除
     *  在这个方法中，@RequestParam 注解用于绑定 HTTP 请求中的参数 "ids"。
     *  由于参数类型为 List<Long>，因此 Spring 会自动将请求中的参数值转换为一个 Long 类型的列表，并将其赋值给方法参数 ids。例如，如果请求 URL 为 "/delete?ids=1&ids=2&ids=3"，那么 Spring 会将参数值 [1, 2, 3] 转换为一个 Long 类型的列表，并将其赋值给方法参数 ids。
     * 该方法的作用是删除一个或多个指定 ID 的记录，并返回一个成功的结果。具体的实现逻辑需要根据业务需求来确定。
     * @param ids id
     * @return {@link Result}
     */
    @DeleteMapping
    @ApiOperation("菜品批量删除")
    public Result delete(@RequestParam List<Long> ids){
        log.info("菜品批量删除：{}",ids);
        dishService.delete(ids);
        return Result.success();
    }

    /**
     * 通过id查询菜品
     *
     * @param id id
     * @return {@link Result}<{@link Employee}>
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询菜品")//用在方法上的描述注解
    public Result<DishVO> getById(@PathVariable Long  id) {//@PathVariable 注解用于从 URL 中获取参数值。
        log.info("根据id查询菜品:{}",id);
        DishVO dishVO=dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation(value = "修改菜品")//用在方法上的描述注解
    public  Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品信息:{}",dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success();

    }

    @PostMapping("status/{status}")
    @ApiOperation(value = "起售停售菜品")//
    public Result startOtStop(@PathVariable Integer status,Long id){
        // public Result startOtStop(@PathVariable("status") Integer status,Long id){//变量同名可以省略
        log.info("起售停售菜品:{},{}",status,id);
        dishService.startOtStop(status,id);
        return Result.success();
    }

}
