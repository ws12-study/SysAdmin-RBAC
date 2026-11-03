package com.yourname.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 菜单权限 Mapper
 *
 * @author yourname
 */
public interface SysMenuMapper {

    /**
     * 查询用户通过角色拥有的全部权限标识
     */
    List<String> selectPermsByUserId(@Param("userId") Long userId);

    /**
     * 全部菜单（按父级与排序，用于权限树）
     */
    List<com.yourname.entity.SysMenu> selectAllMenus();

    /**
     * 用户通过角色可见的菜单（M 目录 + C 菜单，排除隐藏与停用）
     */
    List<com.yourname.entity.SysMenu> selectMenusByUserId(@Param("userId") Long userId);

    /**
     * 全部菜单（含停用与按钮，菜单管理页用）
     */
    List<com.yourname.entity.SysMenu> selectMenuList();

    /**
     * 按主键查询菜单
     */
    com.yourname.entity.SysMenu selectMenuById(@Param("id") Long id);

    /**
     * 新增菜单
     */
    int insertMenu(com.yourname.entity.SysMenu menu);

    /**
     * 更新菜单
     */
    int updateMenu(com.yourname.entity.SysMenu menu);

    /**
     * 物理删除菜单
     */
    int deleteMenuById(@Param("id") Long id);

    /**
     * 直接子菜单数量（删除保护）
     */
    int countChildren(@Param("parentId") Long parentId);

}
