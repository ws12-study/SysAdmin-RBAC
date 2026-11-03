package com.yourname.entity.query;

import lombok.Data;

import java.io.Serializable;

/**
 * 日志查询条件（操作日志 / 登录日志共用，未填写的字段不参与过滤）
 *
 * @author yourname
 */
@Data
public class LogQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 登录账号（登录日志） */
    private String loginName;
    /** 模块标题（操作日志） */
    private String title;
    /** 操作人员（操作日志） */
    private String operName;
    /** 状态：登录日志 0成功/1失败；操作日志 0正常/1异常 */
    private String status;
    /** 起始时间，yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss */
    private String beginTime;
    /** 结束时间，同上（只给日期时按当天 23:59:59 处理） */
    private String endTime;

}
