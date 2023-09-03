package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @author :罗汉
 * @date : 2023/8/15
 */
@Mapper
public interface UserMapper {
    /**
     * 通过openid查询用户
     *
     * @param openid openid
     * @return {@link User}
     */
    @Select("select * from user where openid=#{openid}")
    User getByOpenid(String openid);

    /**
     * 插入数据
     *
     * @param user 用户
     */
    void insert(User user);
    @Select("select * from user where id=#{userId}")
    User getById(Long userId);

    /**
     * 根据动态条件来统计数量
     * @param map
     * @return {@link Integer}
     */
    Integer countByMap(Map map);
}
