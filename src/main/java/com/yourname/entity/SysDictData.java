package com.yourname.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典数据表 sys_dict_data
 *
 * @author yourname
 */
@Data
public class SysDictData implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 字典编码 */
    private Long id;
    /** 字典排序 */
    private Integer dictSort;
    /** 字典标签 */
    private String dictLabel;
    /** 字典键值 */
    private String dictValue;
    /** 字典类型 */
    private String dictType;
    /** 表格回显样式 */
    private String listClass;
    /** 是否默认（Y是 N否） */
    private String isDefault;
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
