package com.yourname.mapper;

import com.yourname.entity.SysLoginLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 登录日志 Mapper
 *
 * @author yourname
 */
public interface SysLoginLogMapper {

    int insertLoginLog(SysLoginLog loginLog);

    List<SysLoginLog> selectLatestByLoginName(@Param("loginName") String loginName,
                                              @Param("limit") int limit);

    long countLoginLog();

    /** 最近的登录日志（全部账号，登录日志页用） */
    List<SysLoginLog> selectLatest(@Param("limit") int limit);

    /** 分页查询登录日志（带筛选条件） */
    List<SysLoginLog> selectByPage(@Param("offset") int offset, @Param("limit") int limit,
                                   @Param("query") com.yourname.entity.query.LogQuery query);

    /** 带筛选条件的登录日志总数 */
    long countByPage(@Param("query") com.yourname.entity.query.LogQuery query);

    /** 指定账号的登录次数（首页非管理员统计用） */
    long countByLoginName(@Param("loginName") String loginName);

    /**
     * 最近 minutes 分钟内、晚于最后一次成功登录的失败次数（登录失败锁定用）
     */
    long countRecentFailures(@Param("loginName") String loginName, @Param("minutes") int minutes);

}
