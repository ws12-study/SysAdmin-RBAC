package com.yourname.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yourname.common.BusinessType;
import com.yourname.common.ServiceException;
import com.yourname.common.annotation.OperLog;
import com.yourname.common.annotation.RequiresPermissions;
import com.yourname.entity.SysDept;
import com.yourname.entity.SysMenu;
import com.yourname.entity.SysRole;
import com.yourname.entity.SysUser;
import com.yourname.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色管理（列表 / 编辑 / 菜单授权 / 数据范围 / 删除）
 *
 * @author yourname
 */
public class RoleManageAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private SysRoleService roleService;

    private List<SysRole> roles;
    private SysRole role;
    private List<SysMenu> menuList;
    private List<Long> checkedMenuIds;
    private List<SysDept> deptList;
    private List<Long> checkedDeptIds;
    private Long roleId;
    private String errorMsg;

    @RequiresPermissions("system:role:list")
    public String list() {
        roles = roleService.selectRoleList();
        return SUCCESS;
    }

    @RequiresPermissions("system:role:edit")
    public String input() {
        menuList = roleService.selectAllMenus();
        deptList = roleService.selectAllDepts();
        if (roleId == null) {
            role = new SysRole();
            role.setDataScope("1");
            checkedMenuIds = new ArrayList<Long>();
            checkedDeptIds = new ArrayList<Long>();
        } else {
            role = roleService.selectRoleById(roleId);
            if (role == null) {
                errorMsg = "角色不存在";
                return INPUT;
            }
            checkedMenuIds = roleService.selectMenuIdsByRoleId(roleId);
            checkedDeptIds = roleService.selectDeptIdsByRoleId(roleId);
        }
        return SUCCESS;
    }

    @RequiresPermissions("system:role:edit")
    @OperLog(title = "角色管理", type = BusinessType.UPDATE)
    public String save() {
        try {
            roleService.saveRole(role, checkedMenuIds, checkedDeptIds, currentUserName());
            flashOk("角色保存成功");
            return SUCCESS;
        } catch (ServiceException e) {
            errorMsg = e.getMessage();
            menuList = roleService.selectAllMenus();
            deptList = roleService.selectAllDepts();
            if (checkedMenuIds == null) {
                checkedMenuIds = new ArrayList<Long>();
            }
            if (checkedDeptIds == null) {
                checkedDeptIds = new ArrayList<Long>();
            }
            return INPUT;
        }
    }

    @RequiresPermissions("system:role:remove")
    @OperLog(title = "角色管理", type = BusinessType.DELETE)
    public String delete() {
        try {
            roleService.deleteRoleById(roleId);
            flashOk("角色已删除");
            return SUCCESS;
        } catch (ServiceException e) {
            errorMsg = e.getMessage();
            roles = roleService.selectRoleList();
            return INPUT;
        }
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public SysRole getRole() {
        return role;
    }

    public void setRole(SysRole role) {
        this.role = role;
    }

    public List<SysMenu> getMenuList() {
        return menuList;
    }

    public List<Long> getCheckedMenuIds() {
        return checkedMenuIds;
    }

    public void setCheckedMenuIds(List<Long> checkedMenuIds) {
        this.checkedMenuIds = checkedMenuIds;
    }

    public List<SysDept> getDeptList() {
        return deptList;
    }

    public List<Long> getCheckedDeptIds() {
        return checkedDeptIds;
    }

    public void setCheckedDeptIds(List<Long> checkedDeptIds) {
        this.checkedDeptIds = checkedDeptIds;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
