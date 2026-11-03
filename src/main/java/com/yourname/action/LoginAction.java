package com.yourname.action;

import com.opensymphony.xwork2.ActionSupport;
import com.yourname.common.ServiceException;
import com.yourname.common.SessionKeys;
import com.yourname.entity.SysUser;
import com.yourname.service.SysUserService;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 登录 Action
 * <p>
 * Action 不放入 Spring 容器：由 struts-spring-plugin 每次请求新建实例并交给 Spring 自动注入依赖，
 * 因此天然线程安全，不需要 prototype 声明。
 *
 * @author yourname
 */
public class LoginAction extends ActionSupport implements org.apache.struts2.interceptor.SessionAware {

    private static final long serialVersionUID = 1L;

    private String userName;
    private String password;
    private String errorMsg;
    /** 重定向带来的提示，如 changed = 密码已修改，请重新登录 */
    private String msg;

    @Autowired
    private SysUserService userService;

    private Map<String, Object> session;

    /** 进入登录页 */
    public String input() {
        if (session.get(SessionKeys.LOGIN_USER) != null) {
            return SUCCESS; // 已登录 -> 跳首页
        }
        return INPUT;
    }

    /** 提交登录 */
    public String login() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String ip = getRemoteIp(request);
        try {
            SysUser user = userService.login(userName, password);
            // 会话只保留身份；角色 / 权限 / 菜单走 SysPermissionService 缓存，授权变更后可即时失效
            session.put(SessionKeys.LOGIN_USER, user);
            userService.recordLoginInfo(user, ip, true, "登录成功",
                    clip(request.getHeader("User-Agent"), 50), clip(System.getProperty("os.name"), 50));
            return SUCCESS;
        } catch (ServiceException e) {
            errorMsg = e.getMessage();
            // 失败日志要带上尝试的账号名，登录失败锁定按 login_name 统计
            SysUser attempted = new SysUser();
            attempted.setUserName(userName == null ? "" : userName.trim());
            userService.recordLoginInfo(attempted, ip, false, errorMsg,
                    clip(request.getHeader("User-Agent"), 50), clip(System.getProperty("os.name"), 50));
            return INPUT;
        }
    }

    /** 退出登录 */
    public String logout() {
        session.remove(SessionKeys.LOGIN_USER);
        HttpSession httpSession = ServletActionContext.getRequest().getSession(false);
        if (httpSession != null) {
            httpSession.invalidate();
        }
        return SUCCESS;
    }

    private String getRemoteIp(HttpServletRequest request) {
        String xip = request.getHeader("X-Forwarded-For");
        if (xip != null && xip.length() > 0) {
            return xip.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    /** 截断到字段长度，避免 Data truncation */
    private String clip(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
