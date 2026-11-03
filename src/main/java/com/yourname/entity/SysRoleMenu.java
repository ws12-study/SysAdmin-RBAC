package com.yourname.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色菜单关联表 sys_role_menu
 *
 * @author yourname
 */
@Data
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;
    /** 角色ID */
    private Long roleId;
    /** 菜单ID */
    private Long menuId;
    /** 创建者 */
    private String createBy;
    /** 创建时间 */
    private Date createTime;
    /** 更新者 */
    private String updateBy;
    /** 更新时间 */
    private Date updateTime;

}
