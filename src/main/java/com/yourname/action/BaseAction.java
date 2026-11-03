package com.yourname.action;

import com.opensymphony.xwork2.ActionSupport;
import com.yourname.common.FlashMessage;
import com.yourname.common.MenuTreeBuilder;
import com.yourname.common.SessionKeys;
import com.yourname.entity.SysUser;
import com.yourname.entity.vo.MenuVo;
import com.yourname.service.SysPermissionService;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 登录后访问的 Action 基类：统一提供当前登录用户与顶栏菜单
 * <p>
 * 菜单取自 SysPermissionService（按用户缓存），角色授权变更后立即反映到导航，无需重新登录。
 *
 * @author yourname
 */
public abstract class BaseAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 1L;

    @Autowired
    protected SysPermissionService permissionService;

    protected Map<String, Object> session;

    /** 当前登录用户，未登录返回 null */
    protected SysUser currentUser() {
        Object user = session == null ? null : session.get(SessionKeys.LOGIN_USER);
        return user instanceof SysUser ? (SysUser) user : null;
    }

    /** 供 JSP 直接访问当前登录用户（如顶栏显示昵称） */
    public SysUser getCurrentUser() {
        return currentUser();
    }

    /** 当前登录账号名，用于 create_by / update_by 填充 */
    protected String currentUserName() {
        SysUser user = currentUser();
        return user == null ? "system" : user.getUserName();
    }

    /** 顶栏导航：只渲染当前用户有权限的菜单 */
    public List<MenuVo> getMenuTree() {
        SysUser user = currentUser();
        if (user == null || user.getId() == null) {
            return Collections.emptyList();
        }
        return MenuTreeBuilder.build(permissionService.getMenus(user.getId()));
    }

    /**
     * 存入一次性提示，跳转后的列表页读取一次即清除
     */
    protected void flash(FlashMessage message) {
        if (session != null && message != null) {
            session.put(SessionKeys.FLASH_MESSAGE, message);
        }
    }

    protected void flashOk(String text) {
        flash(FlashMessage.ok(text));
    }

    protected void flashErr(String text) {
        flash(FlashMessage.err(text));
    }

    /** 同一次请求内缓存已读取的提示，避免 JSP 里多次取 flash 导致二次 getFlash() 返回 null */
    private transient FlashMessage flashView;

    /** 读取并清除一次性提示（页面用，读不到返回 null） */
    public FlashMessage getFlash() {
        if (flashView != null) {
            return flashView;
        }
        if (session == null) {
            return null;
        }
        Object value = session.remove(SessionKeys.FLASH_MESSAGE);
        flashView = value instanceof FlashMessage ? (FlashMessage) value : null;
        return flashView;
    }

    public boolean getIsAdmin() {
        SysUser user = currentUser();
        return user != null && user.getId() != null && permissionService.isAdmin(user.getId());
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

}
