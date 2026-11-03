package com.yourname.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志表 sys_login_log
 *
 * @author yourname
 */
@Data
public class SysLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 访问ID */
    private Long id;
    /** 登录账号 */
    private String loginName;
    /** 登录IP地址 */
    private String ipAddress;
    /** 登录地点 */
    private String loginLocation;
    /** 浏览器类型 */
    private String browser;
    /** 操作系统 */
    private String os;
    /** 登录状态（0成功 1失败） */
    private String status;
    /** 提示消息 */
    private String msg;
    /** 登录时间 */
    private Date loginTime;
    /** 创建者 */
    private String createBy;
    /** 创建时间 */
    private Date createTime;
    /** 更新者 */
    private String updateBy;
    /** 更新时间 */
    private Date updateTime;

}
