package com.yourname.service;

import com.yourname.entity.SysOperLog;

import java.util.List;

/**
 * 操作日志服务
 *
 * @author yourname
 */
public interface SysOperLogService {

    /**
     * 记录一条操作日志；写库失败只记 warn，不影响业务请求
     */
    void record(SysOperLog operLog);

    /**
     * 最近的操作日志
     */
    List<SysOperLog> latest(int limit);

    /**
     * 日志总数
     */
    long count();

    /**
     * 分页查询操作日志
     *
     * @param query 筛选条件（模块 / 操作人 / 状态 / 时间范围），null 表示不筛选
     */
    com.yourname.common.PageResult<SysOperLog> page(int pageNum, int pageSize,
                                                    com.yourname.entity.query.LogQuery query);

}
