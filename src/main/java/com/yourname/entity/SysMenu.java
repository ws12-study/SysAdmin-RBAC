package com.yourname.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单/权限表 sys_menu
 *
 * @author yourname
 */
@Data
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 菜单ID */
    private Long id;
    /** 菜单名称 */
    private String menuName;
    /** 父菜单ID（顶级为0） */
    private Long parentId;
    /** 显示顺序 */
    private Integer orderNum;
    /** 路由地址 */
    private String path;
    /** 组件路径 */
    private String component;
    /** 路由参数 */
    private String query;
    /** 是否外链（0是 1否） */
    private Integer isFrame;
    /** 是否缓存（0缓存 1不缓存） */
    private Integer isCache;
    /** 菜单类型（M目录 C菜单 F按钮） */
    private String menuType;
    /** 显示状态（0显示 1隐藏） */
    private String visible;
    /** 菜单状态（0正常 1停用） */
    private String status;
    /** 权限标识 */
    private String perms;
    /** 菜单图标 */
    private String icon;
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
