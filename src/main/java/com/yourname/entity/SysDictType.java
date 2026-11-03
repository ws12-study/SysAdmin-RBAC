package com.yourname.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典类型表 sys_dict_type
 *
 * @author yourname
 */
@Data
public class SysDictType implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 字典主键 */
    private Long id;
    /** 字典名称 */
    private String dictName;
    /** 字典类型（唯一） */
    private String dictType;
    /** 状态（0正常 1停用） */
    private String status;
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
