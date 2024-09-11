package com.xm.springbootinit.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xm.springbootinit.model.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 */
public interface UserService extends IService<User> {
    User getLoginUser(HttpServletRequest request);
}
