package com.xm.springbootinit.aop;


import com.xm.springbootinit.annotation.AuthCheck;
import com.xm.springbootinit.common.ErrorCode;
import com.xm.springbootinit.exception.BusinessException;
import com.xm.springbootinit.model.entity.User;
import com.xm.springbootinit.model.enums.UserRoleEnum;
import com.xm.springbootinit.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限校验AOP
 *
 */

@Aspect
@Component
public class AuthInterceptor {


    @Resource
    UserService userService;


    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        //1.获取当前用户
        User user = userService.getLoginUser(request);
        //2.获取当前权限
        String mustRole = authCheck.mustRole();
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        //3.不需要权限，放行
        if (mustRoleEnum == null){
            return joinPoint.proceed();
        }
        //4.必须有该权限才能通过
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(user.getUserRole());
        if (userRoleEnum == null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //5.如果被封号，直接拒绝
        if (UserRoleEnum.BAN.equals(userRoleEnum)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //6.必须有管理员权限
        if (UserRoleEnum.ADMIN.equals(mustRoleEnum)){
            if (!UserRoleEnum.ADMIN.equals(userRoleEnum)){
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        //7.放行
        return joinPoint.proceed();
    }
}
