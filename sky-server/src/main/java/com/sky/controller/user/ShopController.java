package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author :罗汉
 * @date : 2023/8/13
 */
@RestController("userShopController")//@RestController 是一个 Spring MVC 注解，它的作用是将一个类标记为 RESTful Web 服务的控制器。
// 与传统的 Spring MVC控制器不同，@RestController 控制器的每个方法都会将返回值
// 直接转换为 JSON 或 XML 格式的响应体，而无需使用视图解析器进行视图渲染。
@RequestMapping("/user/shop")
@Slf4j
@Api(tags = "店铺相关接口")//描述当前类的作用
public class ShopController {
    private static final String KEY="SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取店铺营业状态
     *
     * @return {@link Result}<{@link Integer}>
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result<Integer> getStatus() {
        Integer shop_status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取店铺营业状态为：{}",shop_status==1?"营业中":"打样中");
        return Result.success(shop_status);
    }
}
