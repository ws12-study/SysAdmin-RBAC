package com.yourname.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.yourname.common.SessionKeys;
import com.yourname.common.annotation.RequiresPermissions;
import com.yourname.entity.SysUser;
import com.yourname.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 认证 + 授权拦截器
 * <p>
 * 1. 未登录 -> 跳转登录页<br>
 * 2. 方法上有 {@link RequiresPermissions} -> 校验权限标识；超级管理员放行
 *
 * @author yourname
 */
public class AuthInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        Map<String, Object> session = invocation.getInvocationContext().getSession();

        Object loginUser = session.get(SessionKeys.LOGIN_USER);
        if (!(loginUser instanceof SysUser)) {
            return "login";
        }
        Long userId = ((SysUser) loginUser).getId();

        String requiredPerms = resolveRequiredPerms(invocation);
        if (requiredPerms == null) {
            return invocation.invoke();
        }

        // 权限取自可失效的服务端缓存，授权变更后无需重新登录
        if (permissionService.isAdmin(userId)) {
            return invocation.invoke();
        }
        if (permissionService.getPerms(userId).contains(requiredPerms)) {
            return invocation.invoke();
        }
        return "unauthorized";
    }

    /** 优先取方法上的注解，其次取类上的注解 */
    private String resolveRequiredPerms(ActionInvocation invocation) {
        Object action = invocation.getAction();
        String methodName = invocation.getProxy().getMethod();
        if (methodName == null || methodName.length() == 0) {
            methodName = "execute";
        }
        try {
            Method method = action.getClass().getMethod(methodName);
            RequiresPermissions onMethod = method.getAnnotation(RequiresPermissions.class);
            if (onMethod != null) {
                return onMethod.value();
            }
            RequiresPermissions onClass = action.getClass().getAnnotation(RequiresPermissions.class);
            if (onClass != null) {
                return onClass.value();
            }
        } catch (NoSuchMethodException e) {
            // 方法名带参数或不存在时忽略，交由类级注解兜底
        }
        return null;
    }

    /** 保留：ActionProxy 未解析出方法名时使用 */
    @SuppressWarnings("unused")
    private String getMethodName(ActionProxy proxy) {
        return proxy.getMethod();
    }

}
