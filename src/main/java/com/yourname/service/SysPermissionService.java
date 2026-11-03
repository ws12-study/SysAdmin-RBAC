package com.yourname.service;

import com.yourname.entity.SysMenu;
import com.yourname.entity.SysRole;

import java.util.List;
import java.util.Set;

/**
 * 权限服务：缓存用户的角色 / 权限标识 / 菜单，授权变更时可精确失效
 *
 * @author yourname
 */
public interface SysPermissionService {

    /**
     * 用户权限标识集合
     */
    Set<String> getPerms(Long userId);

    /**
     * 用户角色列表
     */
    List<SysRole> getRoles(Long userId);

    /**
     * 用户可见菜单（M 目录 + C 菜单）
     */
    List<SysMenu> getMenus(Long userId);

    /**
     * 是否超级管理员
     */
    boolean isAdmin(Long userId);

    /**
     * 失效指定用户的权限缓存
     */
    void evict(Long userId);

    /**
     * 失效某个角色下全部用户的权限缓存
     */
    void evictByRole(Long roleId);

    /**
     * 清空全部权限缓存
     */
    void evictAll();

}
