package com.yourname.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import com.yourname.common.BusinessType;
import com.yourname.common.ServiceException;
import com.yourname.common.annotation.OperLog;
import com.yourname.entity.SysUser;
import com.yourname.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

/**
 * 修改当前登录用户的密码
 *
 * @author yourname
 */
public class PasswordAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private String errorMsg;

    @Autowired
    private SysUserService userService;

    /** 进入修改密码页 */
    public String input() {
        return INPUT;
    }

    /** 提交修改 */
    @OperLog(title = "修改密码", type = BusinessType.UPDATE, saveParams = false)
    public String save() {
        SysUser user = currentUser();
        if (user == null) {
            return LOGIN;
        }
        if (newPassword == null || !newPassword.equals(confirmPassword)) {
            errorMsg = "两次输入的新密码不一致";
            return INPUT;
        }
        try {
            userService.changePassword(user.getId(), oldPassword, newPassword);
            // 修改密码后强制重新登录，旧会话立即失效
            ActionContext.getContext().getSession().clear();
            HttpSession httpSession = ServletActionContext.getRequest().getSession(false);
            if (httpSession != null) {
                httpSession.invalidate();
            }
            return LOGIN;
        } catch (ServiceException e) {
            errorMsg = e.getMessage();
            return INPUT;
        }
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
