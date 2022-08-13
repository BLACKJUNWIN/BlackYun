package com.black.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.common.pojo.responseCode;
import com.black.common.pojo.responseJson;
import com.black.common.utils.commonUtils;
import com.black.common.utils.tokenUtils;
import com.black.user.pojo.User;
import com.black.user.mapper.UserMapper;
import com.black.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author BlackJun
 * @since 2022-06-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public Page<User> listByPage(Map<String, Object> map) {
        int page=1;
        if(!commonUtils.isNull(map.get("page"))){
            page= (int) map.get("page");
        }
        Page<User> pageParam = new Page<>(page,10);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if(!commonUtils.isNull(map.get("name"))){
            wrapper.like("name",map.get("name"));
        }
        if(!commonUtils.isNull(map.get("id"))){
            wrapper.eq("id",map.get("id"));
        }
        userMapper.selectPage(pageParam,wrapper);
        return pageParam;
    }

    @Override
    public responseJson login(User user,boolean isBack) {
        User oldUser = userMapper.selectOne(new QueryWrapper<User>().eq("name", user.getName()));
        if(oldUser==null){
            return new responseJson(responseCode.LONGIN_ERROR);
        }
        if(!oldUser.getPassword().equals(user.getPassword())){
            return new responseJson(responseCode.LONGIN_ERROR);
        }
        if(!isBack){
            Map<String,Object> map=new HashMap<>();
            map.put("token",tokenUtils.getToken(oldUser.getId()+""));
            return new responseJson(map);
        }
        if(!oldUser.getRole().equals("admin")){
            return new responseJson(responseCode.USER_NO_AUTHORITY);
        }
        Map<String,Object> map=new HashMap<>();
        map.put("token",tokenUtils.getToken(oldUser.getId()+""));
        return new responseJson(map);
    }
}
