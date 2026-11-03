package com.yourname.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色表 sys_role
 *
 * @author yourname
 */
@Data
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 角色ID */
    private Long id;
    /** 角色名称 */
    private String roleName;
    /** 角色权限字符串 */
    private String roleKey;
    /** 显示顺序 */
    private Integer roleSort;
    /** 数据权限范围（1全部 2自定义 3本部门 4本部门及以下 5仅本人） */
    private String dataScope;
    /** 角色状态（0正常 1停用） */
    private String status;
    /** 删除标志（0存在 1删除） */
    private String delFlag;
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
