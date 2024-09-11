package com.xm.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xm.springbootinit.common.ErrorCode;
import com.xm.springbootinit.constant.UserConstant;
import com.xm.springbootinit.exception.BusinessException;
import com.xm.springbootinit.mapper.UserMapper;
import com.xm.springbootinit.model.entity.User;
import com.xm.springbootinit.service.UserService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public User getLoginUser(HttpServletRequest request) {

        //判断是否登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User)userObj;
        if (user == null || user.getId() == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Long userId = user.getId();
        user = this.getById(userId);
        if (user == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return user;
    }
}
