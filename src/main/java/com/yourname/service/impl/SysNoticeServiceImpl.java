package com.yourname.service.impl;

import com.yourname.common.PageResult;
import com.yourname.common.ServiceException;
import com.yourname.common.TimeRange;
import com.yourname.entity.SysNotice;
import com.yourname.mapper.SysNoticeMapper;
import com.yourname.service.SysNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 通知公告服务实现
 *
 * @author yourname
 */
@Service("sysNoticeServiceImpl")
public class SysNoticeServiceImpl implements SysNoticeService {

    @Autowired
    private SysNoticeMapper noticeMapper;

    @Override
    public PageResult<SysNotice> page(int pageNum, int pageSize, String title, String type, String status) {
        int num = PageResult.fixPageNum(pageNum);
        int size = PageResult.fixPageSize(pageSize);
        String t = TimeRange.trim(title);
        String ty = TimeRange.trim(type);
        String st = TimeRange.trim(status);
        List<SysNotice> rows = noticeMapper.selectPage(PageResult.offset(num, size), size, t, ty, st);
        long total = noticeMapper.countPage(t, ty, st);
        return PageResult.of(rows, total, num, size);
    }

    @Override
    public long count() {
        return noticeMapper.countPage(null, null, null);
    }

    @Override
    public List<SysNotice> latest(int limit) {
        return noticeMapper.selectLatest(limit > 0 ? limit : 5);
    }

    @Override
    public PageResult<SysNotice> viewPage(int pageNum, int pageSize, String title, String type, String order) {
        int num = PageResult.fixPageNum(pageNum);
        int size = PageResult.fixPageSize(pageSize);
        String t = TimeRange.trim(title);
        String ty = TimeRange.trim(type);
        String o = "asc".equalsIgnoreCase(TimeRange.trim(order)) ? "asc" : "desc";
        List<SysNotice> rows = noticeMapper.selectViewPage(PageResult.offset(num, size), size, t, ty, o);
        long total = noticeMapper.countViewPage(t, ty);
        return PageResult.of(rows, total, num, size);
    }

    @Override
    public SysNotice selectById(Long id) {
        return id == null ? null : noticeMapper.selectById(id);
    }

    @Override
    public void save(SysNotice notice, String operator) {
        if (notice == null) {
            throw new ServiceException("公告信息不能为空");
        }
        if (!StringUtils.hasText(notice.getNoticeTitle())) {
            throw new ServiceException("公告标题不能为空");
        }
        if (notice.getNoticeType() == null) {
            notice.setNoticeType("1");
        }
        if (notice.getStatus() == null) {
            notice.setStatus("0");
        }
        if (notice.getId() == null) {
            notice.setCreateBy(operator);
            noticeMapper.insert(notice);
        } else {
            if (noticeMapper.selectById(notice.getId()) == null) {
                throw new ServiceException("公告不存在");
            }
            notice.setUpdateBy(operator);
            noticeMapper.update(notice);
        }
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new ServiceException("公告ID不能为空");
        }
        if (noticeMapper.selectById(id) == null) {
            throw new ServiceException("公告不存在");
        }
        noticeMapper.deleteById(id);
    }

}
