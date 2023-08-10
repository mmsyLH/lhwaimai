package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.vo.SetmealVO;
import org.springframework.stereotype.Service;

/**
 * @author :罗汉
 * @date : 2023/8/10
 */

public interface SetmealService {
    /**
     * 添加套餐
     *
     * @param SetmealDTO setmeal签证官
     */
    void save(SetmealDTO setmealDTO);
}
