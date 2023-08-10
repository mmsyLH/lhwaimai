package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :罗汉
 * @date : 2023/8/5
 */
@Service
@Slf4j

public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 新增菜品和口味数据
     *
     * @param dishDTO 菜dto
     */
    @Transactional
    @Override
    public void saveWishFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();

        // 对象属性拷贝赋值
        BeanUtils.copyProperties(dishDTO, dish);
        // 向菜品表插入1数据
        dishMapper.insert(dish);

        //获取insert语句生成的主键值
        Long dishId = dish.getId();

        // 向口味表插入多个数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {// 说明口味数据不为空
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            // 向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO 菜页面查询dto
     * @return {@link PageResult}
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        //下一条sql进行分页，自动加入limit关键字分页
        Page<DishVO>  page=dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 菜品的批量删除功能
     *
     * @param ids id
     */
    @Override
    @Transactional
    public void delete(List<Long> ids) {
        //要删除三张表

        //1 判断当前菜品是否在起售中
        for (Long id : ids) {
           Dish dish= dishMapper.getById(id);
           if (dish.getStatus()== StatusConstant.ENABLE){
                //在起售状态 不能删除 抛个异常
               throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
           }
        }

        //2 如果当前菜品是否被某个套餐关联
        List<Long> setmealIds= setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds!=null && setmealIds.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //3 如果都能删除
        for (Long id : ids) {
            //3.1 删除菜品表中的数据
            dishMapper.deleteById(id);
            //3.2 删除菜品关联的口味表的数据
            dishFlavorMapper.deleteByDishIds(id);
        }


    }

    /**
     * 通过id获取菜品与味道
     *
     * @param id id
     * @return {@link DishVO}
     */
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        //1 根据id查询菜品
        Dish dish = dishMapper.getById(id);


        //2 根据菜品查询口味
        List<DishFlavor> dishFlavors= dishFlavorMapper.getByDishId(id);


        //3 封装到vo
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 起售与禁售菜品
     *
     * @param status 状态
     * @param id     id
     */
    @Override
    public void startOtStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                // .updateTime(LocalDateTime.now())
                // .updateUser(BaseContext.getCurrentId())
                .build();
        dishMapper.update(dish);

        if (status == StatusConstant.DISABLE) {
            // 如果是停售操作，还需要将包含当前菜品的套餐也停售
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            // select setmeal_id from setmeal_dish where dish_id in (?,?,?)
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(dishIds);
            if (setmealIds != null && setmealIds.size() > 0) {
                for (Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    setmealMapper.update(setmeal);
                }
            }
        }

    }

    /**
     * 更新菜品与味道
     *
     * @param dishDTO 菜dto
     */
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        // 先修改基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);

        //1 先把原先菜品口味删掉
        dishFlavorMapper.deleteByDishIds(dishDTO.getId());

        //2 再重新插入新的口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors!=null && flavors.size()>0){
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishDTO.getId());
            }
            //批量插入数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }
}
