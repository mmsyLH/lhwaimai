package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @author :罗汉
 * @date : 2023/8/15
 */
public interface UserService {

    /**
     * wx登录
     *
     * @param userLoginDTO 用户登录dto
     * @return {@link User}
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
