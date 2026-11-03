package com.yourname.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志表 sys_oper_log
 *
 * @author yourname
 */
@Data
public class SysOperLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 日志主键 */
    private Long id;
    /** 模块标题 */
    private String title;
    /** 业务类型（见 BusinessType） */
    private Integer businessType;
    /** 方法名称 */
    private String method;
    /** 请求方式 */
    private String requestMethod;
    /** 操作类别（0其它 1后台用户 2手机端用户） */
    private Integer operatorType;
    /** 操作人员 */
    private String operName;
    /** 请求URL */
    private String operUrl;
    /** 主机地址 */
    private String operIp;
    /** 操作地点 */
    private String operLocation;
    /** 请求参数 */
    private String operParam;
    /** 返回结果 */
    private String jsonResult;
    /** 操作状态（0正常 1异常） */
    private Integer status;
    /** 错误消息 */
    private String errorMsg;
    /** 操作时间 */
    private Date operTime;
    /** 创建者 */
    private String createBy;
    /** 创建时间 */
    private Date createTime;
    /** 更新者 */
    private String updateBy;
    /** 更新时间 */
    private Date updateTime;

}
