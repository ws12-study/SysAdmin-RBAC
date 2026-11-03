package com.yourname.mapper;

import com.yourname.entity.SysNotice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通知公告 Mapper
 *
 * @author yourname
 */
public interface SysNoticeMapper {

    List<SysNotice> selectPage(@Param("offset") int offset, @Param("limit") int limit,
                               @Param("title") String title, @Param("type") String type,
                               @Param("status") String status);

    long countPage(@Param("title") String title, @Param("type") String type,
                   @Param("status") String status);

    SysNotice selectById(@Param("id") Long id);

    /** 最近发布的有效公告（首页展示用，所有登录用户可见） */
    List<SysNotice> selectLatest(@Param("limit") int limit);

    /** 普通用户可见的公告分页（仅正常状态，支持排序） */
    List<SysNotice> selectViewPage(@Param("offset") int offset, @Param("limit") int limit,
                                   @Param("title") String title, @Param("type") String type,
                                   @Param("order") String order);

    /** 普通用户可见的公告总数（仅正常状态） */
    long countViewPage(@Param("title") String title, @Param("type") String type);

    int insert(SysNotice notice);

    int update(SysNotice notice);

    int deleteById(@Param("id") Long id);

}
