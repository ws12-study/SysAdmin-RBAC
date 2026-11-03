package com.yourname.service;

import com.yourname.common.PageResult;
import com.yourname.entity.SysNotice;

/**
 * 通知公告 Service
 *
 * @author yourname
 */
public interface SysNoticeService {

    PageResult<SysNotice> page(int pageNum, int pageSize, String title, String type, String status);

    /**
     * 公告总数
     */
    long count();

    /**
     * 最近发布的有效公告（首页展示）
     */
    java.util.List<SysNotice> latest(int limit);

    /**
     * 普通用户可见的公告分页（仅正常状态，支持按发布时间排序）
     */
    PageResult<SysNotice> viewPage(int pageNum, int pageSize, String title, String type, String order);

    SysNotice selectById(Long id);

    void save(SysNotice notice, String operator);

    void deleteById(Long id);

}
