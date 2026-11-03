package com.yourname.service;

import com.yourname.entity.SysDept;
import com.yourname.entity.SysMenu;
import com.yourname.entity.SysRole;

import java.util.List;

/**
 * 角色服务
 *
 * @author yourname
 */
public interface SysRoleService {

    /**
     * 角色列表
     */
    List<SysRole> selectRoleList();

    /**
     * 按主键查询角色
     */
    SysRole selectRoleById(Long id);

    /**
     * 保存角色及其菜单授权、部门数据范围（事务内执行）
     *
     * @param role     角色信息，id 为空即新增
     * @param menuIds  授权的菜单 ID 集合
     * @param deptIds  数据权限自定义时的部门 ID 集合
     * @param operator 操作人账号
     */
    void saveRole(SysRole role, List<Long> menuIds, List<Long> deptIds, String operator);

    /**
     * 删除角色（已分配用户时拒绝）
     */
    void deleteRoleById(Long id);

    /**
     * 全部菜单（权限树）
     */
    List<SysMenu> selectAllMenus();

    /**
     * 角色已授权的菜单 ID
     */
    List<Long> selectMenuIdsByRoleId(Long roleId);

    /**
     * 全部部门
     */
    List<SysDept> selectAllDepts();

    /**
     * 角色已授权的部门 ID
     */
    List<Long> selectDeptIdsByRoleId(Long roleId);

}
