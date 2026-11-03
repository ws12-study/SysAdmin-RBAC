package com.yourname.service.impl;

import com.yourname.common.ServiceException;
import com.yourname.entity.SysDept;
import com.yourname.entity.SysMenu;
import com.yourname.entity.SysRole;
import com.yourname.mapper.SysDeptMapper;
import com.yourname.mapper.SysMenuMapper;
import com.yourname.mapper.SysRoleMapper;
import com.yourname.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * 角色服务实现
 *
 * @author yourname
 */
@Service("sysRoleServiceImpl")
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private com.yourname.service.SysPermissionService permissionService;

    @Override
    public List<SysRole> selectRoleList() {
        List<SysRole> roles = roleMapper.selectAllRoles();
        return roles == null ? Collections.<SysRole>emptyList() : roles;
    }

    @Override
    public SysRole selectRoleById(Long id) {
        return id == null ? null : roleMapper.selectRoleById(id);
    }

    @Override
    public void saveRole(SysRole role, List<Long> menuIds, List<Long> deptIds, String operator) {
        if (role == null) {
            throw new ServiceException("角色信息不能为空");
        }
        if (!StringUtils.hasText(role.getRoleName()) || !StringUtils.hasText(role.getRoleKey())) {
            throw new ServiceException("角色名称与权限字符不能为空");
        }
        role.setCreateBy(operator);

        // 权限字符唯一性校验（更新时排除自身）
        SysRole exists = roleMapper.selectRoleByKey(role.getRoleKey());
        if (exists != null && (role.getId() == null || !exists.getId().equals(role.getId()))) {
            throw new ServiceException("权限字符已被占用：" + role.getRoleKey());
        }

        if (role.getId() == null) {
            role.setStatus(role.getStatus() == null ? "0" : role.getStatus());
            role.setDataScope(role.getDataScope() == null ? "1" : role.getDataScope());
            roleMapper.insertRole(role);
        } else {
            if (roleMapper.selectRoleById(role.getId()) == null) {
                throw new ServiceException("角色不存在");
            }
            roleMapper.updateRole(role);
        }

        // 重新授权菜单
        roleMapper.deleteRoleMenuByRoleId(role.getId());
        if (menuIds != null) {
            for (Long menuId : menuIds) {
                if (menuId != null) {
                    roleMapper.insertRoleMenu(role.getId(), menuId);
                }
            }
        }

        // 仅在「自定义」数据范围时保存部门
        roleMapper.deleteRoleDeptByRoleId(role.getId());
        if ("2".equals(role.getDataScope()) && deptIds != null) {
            for (Long deptId : deptIds) {
                if (deptId != null) {
                    roleMapper.insertRoleDept(role.getId(), deptId);
                }
            }
        }
        // 菜单/数据范围变化会影响该角色下所有用户，一并失效其权限缓存
        permissionService.evictByRole(role.getId());
    }

    @Override
    public void deleteRoleById(Long id) {
        if (id == null) {
            throw new ServiceException("角色ID不能为空");
        }
        if (roleMapper.selectRoleById(id) == null) {
            throw new ServiceException("角色不存在");
        }
        if (roleMapper.countUserByRoleId(id) > 0) {
            throw new ServiceException("该角色下仍有用户，请先解除用户关联");
        }
        // 先失效该角色下用户的权限缓存，再删除授权关系
        permissionService.evictByRole(id);
        roleMapper.deleteRoleMenuByRoleId(id);
        roleMapper.deleteRoleDeptByRoleId(id);
        roleMapper.deleteRoleById(id);
    }

    @Override
    public List<SysMenu> selectAllMenus() {
        List<SysMenu> menus = menuMapper.selectAllMenus();
        return menus == null ? Collections.<SysMenu>emptyList() : menus;
    }

    @Override
    public List<Long> selectMenuIdsByRoleId(Long roleId) {
        List<Long> ids = roleId == null ? null : roleMapper.selectMenuIdsByRoleId(roleId);
        return ids == null ? Collections.<Long>emptyList() : ids;
    }

    @Override
    public List<SysDept> selectAllDepts() {
        List<SysDept> depts = deptMapper.selectAllDepts();
        return depts == null ? Collections.<SysDept>emptyList() : depts;
    }

    @Override
    public List<Long> selectDeptIdsByRoleId(Long roleId) {
        List<Long> ids = roleId == null ? null : roleMapper.selectDeptIdsByRoleId(roleId);
        return ids == null ? Collections.<Long>emptyList() : ids;
    }

}
