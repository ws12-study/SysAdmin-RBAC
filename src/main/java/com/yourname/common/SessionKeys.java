package com.yourname.common;

/**
 * 会话 Key
 *
 * @author yourname
 */
public interface SessionKeys {

    /** 当前登录用户（会话只存身份，角色/权限/菜单走 SysPermissionService 缓存） */
    String LOGIN_USER = "loginUser";

    /** 一次性提示消息（redirect 后仍能显示） */
    String FLASH_MESSAGE = "flashMessage";

    /** 超级管理员角色标识，与 sys_role.role_key 对应 */
    String ROLE_KEY_ADMIN = "admin";

}
