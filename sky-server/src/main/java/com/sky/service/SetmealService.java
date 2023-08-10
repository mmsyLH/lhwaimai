package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author :罗汉
 * @date : 2023/8/10
 */

public interface SetmealService {
    /**
     * 添加套餐
     *
     * @param setmealDTO setmeal签证官
     */
    void save(SetmealDTO setmealDTO);

    /**
     * 页面查询
     * 分页查询
     *
     * @param setmealPageQueryDTO setmeal页面查询dto
     * @return {@link PageResult}
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 启动或停止套餐
     *
     * @param status 状态
     * @param id     id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 批量删除套餐
     *
     * @param ids id
     */
    void delete(List<Long> ids);

    /**
     * 通过id查询套餐
     *
     * @param id id
     * @return {@link SetmealVO}
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * 更新套餐
     *
     * @param setmealDTO setmeal dto
     */
    void update(SetmealDTO setmealDTO);
}
