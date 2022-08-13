package com.black.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.common.pojo.responseJson;
import com.black.user.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author BlackJun
 * @since 2022-06-08
 */
public interface UserService extends IService<User> {
    Page<User> listByPage(Map<String, Object> map);

    responseJson login(User user,boolean isBack);
}
