package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车控制器
 *
 * @author 罗汉
 * @date 2023/08/22
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "C端-购物车接口")
public class CartController {
    @Autowired
    private CartService cartService;
    /**
     * 添加购物车
     *
     * @param shoppingCartDTO 购物车dto
     * @return {@link Result}
     */
    @ApiOperation(value = "添加购物车")
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车商品信息为,{}",shoppingCartDTO);
        cartService.add(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     *
     * @return {@link Result}<{@link ShoppingCart}>
     */
    @GetMapping("/list")
    @ApiOperation(value = "查看购物车")
    public Result<List<ShoppingCart>> list(){
        List<ShoppingCart> shoppingCartList=cartService.showCart();
        return Result.success(shoppingCartList);
    }

    /**
     * 清空购物车
     *
     * @return {@link Result}
     */
    @DeleteMapping("/clean")
    @ApiOperation(value = "清空购物车")
    public Result clean(){
        cartService.cleanCart();
        return Result.success();
    }
}
