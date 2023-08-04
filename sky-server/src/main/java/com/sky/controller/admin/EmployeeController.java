package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工控制器
 * 员工管理
 *
 * @author 罗汉
 * @date 2023/08/03
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")//描述当前类的作用
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     *
     * @return {@link Result}<{@link String}>
     */
    @PostMapping("")
    @ApiOperation(value = "新增员工")//用在方法上的描述注解
    public Result<String> save(@RequestBody EmployeeDTO employeeDTO) {

        System.out.println("当前线程id："+Thread.currentThread().getId());

        log.info("新增员工：{}",employeeDTO );
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO 员工页面查询dto
     * @return {@link Result}<{@link PageResult}>
     */
    @GetMapping("/page")
    @ApiOperation(value = "员工分页查询")//
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){//springMVC会自动封装好
        log.info("员工分页查询参数：{}",employeePageQueryDTO );

       PageResult pageResult=employeeService.pageQuery(employeePageQueryDTO);
        return  Result.success(pageResult);
    }
    @PostMapping("status/{status}")
    @ApiOperation(value = "启用禁用员工账号")//
    public Result startOtStop(@PathVariable Integer status,Long id){
    // public Result startOtStop(@PathVariable("status") Integer status,Long id){//变量同名可以省略
        log.info("启用禁用员工账号:{},{}",status,id);
        employeeService.startOtStop(status,id);
        return Result.success();
    }
    /**
     * 新增员工
     *
     * @return {@link Result}<{@link String}>
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询员工")//用在方法上的描述注解
    public Result<Employee> getById(@PathVariable Long  id) {
        Employee employee=employeeService.getById(id);
        return Result.success(employee);
    }
    @PutMapping
    @ApiOperation(value = "编辑更新员工信息")//用在方法上的描述注解
    public  Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("编辑员工信息:{}",employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();

    }
}
