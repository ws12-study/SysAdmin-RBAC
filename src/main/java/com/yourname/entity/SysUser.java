package com.yourname.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表 sys_user
 *
 * @author yourname
 */
@Data
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long id;
    /** 部门ID */
    private Long deptId;
    /** 登录账号 */
    private String userName;
    /** 用户昵称 */
    private String nickName;
    /** 用户类型（0系统用户 1普通会员） */
    private String userType;
    /** 登录密码 */
    private String password;
    /** 真实姓名 */
    private String realName;
    /** 邮箱 */
    private String email;
    /** 手机号码 */
    private String phonenumber;
    /** 用户性别（0男 1女 2未知） */
    private String sex;
    /** 头像路径 */
    private String avatar;
    /** 帐号状态（0正常 1停用） */
    private String status;
    /** 删除标志（0存在 1删除） */
    private String delFlag;
    /** 最后登录IP */
    private String loginIp;
    /** 最后登录时间 */
    private Date loginDate;
    /** 创建者 */
    private String createBy;
    /** 创建时间 */
    private Date createTime;
    /** 更新者 */
    private String updateBy;
    /** 更新时间 */
    private Date updateTime;
    /** 备注 */
    private String remark;

}
