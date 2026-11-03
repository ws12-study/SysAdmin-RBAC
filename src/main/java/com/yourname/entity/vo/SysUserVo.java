package com.yourname.entity.vo;

import com.yourname.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户列表 VO：实体字段 + 部门名 + 角色名展示
 *
 * @author yourname
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserVo extends SysUser {

    private static final long serialVersionUID = 1L;

    /** 部门名称 */
    private String deptName;

    /** 角色名称，逗号分隔 */
    private String roleNames;

}
