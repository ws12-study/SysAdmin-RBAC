package com.yourname.action;

import com.opensymphony.xwork2.ActionSupport;
import com.yourname.common.ServiceException;
import com.yourname.entity.SysUser;
import com.yourname.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 注册 Action
 *
 * @author yourname
 */
public class RegisterAction extends ActionSupport {

    private static final long serialVersionUID = 1L;

    private String userName;
    private String password;
    private String confirmPassword;
    private String nickName;
    private String phonenumber;
    private String email;
    private String errorMsg;

    @Autowired
    private SysUserService userService;

    /** 进入注册页 */
    public String input() {
        return INPUT;
    }

    /** 提交注册 */
    public String register() {
        if (password == null || !password.equals(confirmPassword)) {
            errorMsg = "两次输入的密码不一致";
            return INPUT;
        }
        try {
            SysUser user = new SysUser();
            user.setUserName(userName);
            user.setNickName(nickName);
            user.setPhonenumber(phonenumber);
            user.setEmail(email);
            userService.register(user, password);
            return SUCCESS;
        } catch (ServiceException e) {
            errorMsg = e.getMessage();
            return INPUT;
        }
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
