package com.yourname.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门表 sys_dept
 *
 * @author yourname
 */
@Data
public class SysDept implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 部门ID */
    private Long id;
    /** 父部门ID（顶级为0） */
    private Long parentId;
    /** 祖级路径（如 0,100,101） */
    private String ancestors;
    /** 部门名称 */
    private String deptName;
    /** 显示顺序 */
    private Integer orderNum;
    /** 负责人 */
    private String leader;
    /** 联系电话 */
    private String phone;
    /** 邮箱 */
    private String email;
    /** 部门状态（0正常 1停用） */
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

    /** 树形展示层级（非表字段，仅用于前端缩进） */
    private transient Integer depth;

}
