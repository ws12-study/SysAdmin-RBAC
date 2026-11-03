package com.yourname.service;

import com.yourname.entity.SysMenu;

import java.util.List;

/**
 * 菜单 Service
 *
 * @author yourname
 */
public interface SysMenuService {

    /**
     * 菜单树（深度优先平铺，带缩进层级，含目录/菜单/按钮）
     */
    List<SysMenu> selectMenuTree();

    /**
     * 上级菜单下拉（M 目录 + C 菜单）
     */
    List<SysMenu> selectParentOptions();

    /**
     * 按主键查询
     */
    SysMenu selectMenuById(Long id);

    /**
     * 新增 / 修改菜单
     */
    void saveMenu(SysMenu menu, String operator);

    /**
     * 删除菜单（存在子菜单时拒绝，并清理角色授权关联）
     */
    void deleteMenuById(Long id);

}
