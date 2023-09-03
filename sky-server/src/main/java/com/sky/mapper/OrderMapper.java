package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author :罗汉
 * @date : 2023/8/24
 */
@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     *
     * @param orders 订单
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);
    /**
     * 分页条件查询并按下单时间排序
     * @param ordersPageQueryDTO
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 取消订单
     *
     * @param orders 订单
     */
    @Update("update orders set status = #{status} where id=#{id}")
    void cancelOrder(Orders orders);

    /**
     * 根据状态统计订单数量
     * @param status
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * 通过订单状态和下单时间查询订单
     *
     * @param status    状态
     * @param orderTime 订单时间
     * @return {@link List}<{@link Orders}>
     */// select * from orders where status=? and order_time<(当前时间-15分钟)
    @Select("select * from orders where status=#{status} and order_time<#{orderTime}")
    List<Orders> getByStatusAndOrderTime(int status, LocalDateTime orderTime);

    /**
     * 根据动态条件来统计营业额数据
     * @param map
     * @return {@link Double}
     */
    Double sumByMap(Map map);

    /**
     * 根据Map动态统计订单数量
     * @param map
     * @return {@link Integer}
     */
    Integer countByMap(Map map);

    /**
     * 统计指定时间内的销量排名
     *
     * @param begin
     * @param end
     * @return {@link List}<{@link GoodsSalesDTO}>
     */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin,LocalDateTime end);
}
