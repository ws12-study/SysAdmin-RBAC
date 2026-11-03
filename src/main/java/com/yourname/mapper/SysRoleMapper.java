package com.yourname.mapper;

import com.yourname.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色表 Mapper
 *
 * @author yourname
 */
public interface SysRoleMapper {

    /**
     * 查询指定用户拥有的角色（未删除、已启用）
     */
    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 全部可用角色
     */
    List<SysRole> selectAllRoles();

    /**
     * 查询用户已拥有的角色 ID
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 清空用户的角色关联
     */
    int deleteUserRolesByUserId(@Param("userId") Long userId);

    /**
     * 按主键查询角色
     */
    SysRole selectRoleById(@Param("id") Long id);

    /**
     * 按权限字符查询角色（校验唯一性用）
     */
    SysRole selectRoleByKey(@Param("roleKey") String roleKey);

    /**
     * 新增角色
     */
    int insertRole(SysRole role);

    /**
     * 更新角色
     */
    int updateRole(SysRole role);

    /**
     * 逻辑删除角色
     */
    int deleteRoleById(@Param("id") Long id);

    /**
     * 统计角色下已分配的用户数（删除前置校验）
     */
    long countUserByRoleId(@Param("roleId") Long roleId);

    /**
     * 角色下已分配的用户 ID（权限缓存失效用）
     */
    List<Long> selectUserIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 角色已授权的菜单 ID
     */
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 清空角色的菜单授权
     */
    int deleteRoleMenuByRoleId(@Param("roleId") Long roleId);

    /**
     * 给角色授权菜单
     */
    int insertRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    /**
     * 删除指定菜单的所有角色授权（删除菜单时清理关联）
     */
    int deleteRoleMenuByMenuId(@Param("menuId") Long menuId);

    /**
     * 角色已授权的部门 ID（数据权限自定义时使用）
     */
    List<Long> selectDeptIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 清空角色的部门授权
     */
    int deleteRoleDeptByRoleId(@Param("roleId") Long roleId);

    /**
     * 给角色授权部门
     */
    int insertRoleDept(@Param("roleId") Long roleId, @Param("deptId") Long deptId);

}
