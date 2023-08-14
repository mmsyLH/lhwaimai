package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
