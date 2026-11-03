package com.yourname.mapper;

import com.yourname.entity.SysOperLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作日志 Mapper
 *
 * @author yourname
 */
public interface SysOperLogMapper {

    int insertOperLog(SysOperLog operLog);

    List<SysOperLog> selectLatest(@Param("limit") int limit);

    /** 分页查询（带筛选条件） */
    List<SysOperLog> selectByPage(@Param("offset") int offset, @Param("limit") int limit,
                                  @Param("query") com.yourname.entity.query.LogQuery query);

    /** 带筛选条件的操作日志总数 */
    long countByPage(@Param("query") com.yourname.entity.query.LogQuery query);

    long countOperLog();

}
