package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void save(EmployeeDTO employeeDTO);

    /**
     * 分页查询查询方法
     *
     * @param employeePageQueryDTO 员工页面查询dto
     * @return {@link PageResult}
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用禁用员工账户
     *
     * @param status 状态
     * @param id     id
     */
    void startOtStop(Integer status, Long id);

    /**
     * 通过id查询员工
     *
     * @param id id
     * @return {@link Employee}
     */
    Employee getById(Long id);

    /**
     * 更新员工信息
     *
     * @param employeeDTO 员工dto
     */
    void update(EmployeeDTO employeeDTO);
}
