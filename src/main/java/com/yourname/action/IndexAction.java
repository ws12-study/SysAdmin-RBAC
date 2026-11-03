package com.yourname.action;

import com.yourname.entity.SysLoginLog;
import com.yourname.entity.SysRole;
import com.yourname.entity.SysUser;
import com.yourname.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 首页 Action（导航按角色菜单动态渲染）
 *
 * @author yourname
 */
public class IndexAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private SysUserService userService;

    @Autowired
    private com.yourname.service.SysNoticeService noticeService;

    private SysUser currentUser;
    private List<SysRole> roles;
    private boolean admin;
    private long userCount;
    private long loginCount;
    private long noticeCount;
    private List<com.yourname.entity.SysNotice> latestNotices;
    private List<SysLoginLog> latestLogs;

    @Override
    public String execute() {
        currentUser = currentUser();
        if (currentUser == null) {
            return LOGIN;
        }
        Long userId = currentUser.getId();

        roles = permissionService.getRoles(userId);
        admin = permissionService.isAdmin(userId);
        // 菜单由 BaseAction.getMenuTree() 按角色动态渲染

        userCount = userService.countUser();
        // 管理员看全量登录次数，普通用户只看自己的（避免泄露全系统活动量）
        loginCount = admin ? userService.countLoginLog()
                : userService.countLoginLogByUser(currentUser.getUserName());
        noticeCount = noticeService.count();
        // 最新公告对所有登录用户可见（无 system:notice:list 权限也能看）
        latestNotices = noticeService.latest(5);
        latestLogs = userService.latestLoginLogs(currentUser.getUserName(), 5);
        return SUCCESS;
    }

    public SysUser getCurrentUser() {
        return currentUser;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public boolean getAdmin() {
        return admin;
    }

    public boolean getIsAdmin() {
        return admin;
    }

    public long getUserCount() {
        return userCount;
    }

    public long getLoginCount() {
        return loginCount;
    }

    public long getNoticeCount() {
        return noticeCount;
    }

    public List<com.yourname.entity.SysNotice> getLatestNotices() {
        return latestNotices;
    }

    public List<SysLoginLog> getLatestLogs() {
        return latestLogs;
    }

}
