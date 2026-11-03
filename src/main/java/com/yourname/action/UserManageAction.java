package com.yourname.action;

import com.opensymphony.xwork2.ActionSupport;
import com.yourname.common.BusinessType;
import com.yourname.common.PageResult;
import com.yourname.common.QueryStringBuilder;
import com.yourname.common.ServiceException;
import com.yourname.common.annotation.OperLog;
import com.yourname.common.annotation.RequiresPermissions;
import com.yourname.entity.SysRole;
import com.yourname.entity.SysUser;
import com.yourname.entity.vo.SysUserVo;
import com.yourname.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理（列表 + 角色分配）
 *
 * @author yourname
 */
public class UserManageAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private SysUserService userService;

    @Autowired
    private com.yourname.service.DataScopeService dataScopeService;

    private List<SysUserVo> users;
    /** 分页结果（列表页改用分页后，users 仅为兼容保留） */
    private PageResult<SysUserVo> page;
    /** 页码，从 1 开始 */
    private Integer pageNum;
    private Integer pageSize;
    private SysUser targetUser;
    private List<SysRole> allRoles;
    private List<Long> checkedRoleIds;
    private Long userId;
    private String errorMsg;
    /** 当前登录用户的数据范围描述（页面提示） */
    private String scopeDesc;
    /** 搜索条件：账号 / 昵称关键字 */
    private String userName;
    /** 搜索条件：状态（0正常 1停用） */
    private String status;

    @RequiresPermissions("system:user:list")
    public String list() {
        SysUser current = currentUser();
        page = userService.selectUserPage(current, PageResult.fixPageNum(pageNum),
                PageResult.fixPageSize(pageSize), userName, status);
        users = page.getRows();
        scopeDesc = dataScopeService.describe(current);
        return SUCCESS;
    }

    @RequiresPermissions("system:user:edit")
    public String roleInput() {
        try {
            targetUser = userService.selectByIdInScope(userId, currentUser());
            if (targetUser == null) {
                // 不存在与越权统一提示，避免通过提示差异探测账号是否存在
                flashErr("用户不存在或不在你的数据范围内");
                return INPUT;
            }
            allRoles = userService.selectAllRoles();
            checkedRoleIds = userService.selectRoleIdsByUserId(userId);
            if (checkedRoleIds == null) {
                checkedRoleIds = new ArrayList<Long>();
            }
            return SUCCESS;
        } catch (ServiceException e) {
            errorMsg = e.getMessage();
            return INPUT;
        }
    }

    @RequiresPermissions("system:user:edit")
    @OperLog(title = "用户管理", type = BusinessType.GRANT)
    public String roleSave() {
        try {
            userService.assignRoles(userId, checkedRoleIds, currentUser());
            flashOk("角色分配成功");
            return SUCCESS;
        } catch (ServiceException e) {
            // 结果是 redirectAction，页内 errorMsg 会丢，改用一次性提示
            flashErr(e.getMessage());
            return INPUT;
        }
    }

    @RequiresPermissions("system:user:edit")
    @OperLog(title = "用户管理", type = BusinessType.UPDATE)
    public String changeStatus() {
        try {
            userService.changeStatus(userId, status, currentUser());
            flashOk("1".equals(status) ? "用户已停用，该账号将无法登录" : "用户已启用");
            return SUCCESS;
        } catch (ServiceException e) {
            flashErr(e.getMessage());
            return INPUT;
        }
    }

    public List<SysUserVo> getUsers() {
        return users;
    }

    public PageResult<SysUserVo> getPage() {
        return page;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public SysUser getTargetUser() {
        return targetUser;
    }

    public List<SysRole> getAllRoles() {
        return allRoles;
    }

    public List<Long> getCheckedRoleIds() {
        return checkedRoleIds;
    }

    public void setCheckedRoleIds(List<Long> checkedRoleIds) {
        this.checkedRoleIds = checkedRoleIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getScopeDesc() {
        return scopeDesc;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /** 供分页链接复用：翻页时保留搜索条件 */
    public String getQueryString() {
        StringBuilder sb = new StringBuilder();
        QueryStringBuilder.append(sb, "userName", userName);
        QueryStringBuilder.append(sb, "status", status);
        return sb.toString();
    }

}
