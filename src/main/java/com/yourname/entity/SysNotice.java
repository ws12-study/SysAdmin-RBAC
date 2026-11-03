package com.yourname.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 通知公告表 sys_notice
 *
 * @author yourname
 */
@Data
public class SysNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /** 公告标题 */
    private String noticeTitle;
    /** 公告类型（1通知 2公告） */
    private String noticeType;
    /** 公告内容 */
    private String noticeContent;
    /** 状态（0正常 1关闭） */
    private String status;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String remark;

}
