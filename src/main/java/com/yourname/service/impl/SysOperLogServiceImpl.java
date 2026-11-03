package com.yourname.service.impl;

import com.yourname.entity.SysOperLog;
import com.yourname.common.PageResult;
import com.yourname.common.TimeRange;
import com.yourname.entity.query.LogQuery;
import com.yourname.mapper.SysOperLogMapper;
import com.yourname.service.SysOperLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 操作日志服务实现
 *
 * @author yourname
 */
@Service("sysOperLogServiceImpl")
public class SysOperLogServiceImpl implements SysOperLogService {

    private static final Logger log = LoggerFactory.getLogger(SysOperLogServiceImpl.class);

    @Autowired
    private SysOperLogMapper operLogMapper;

    @Override
    public void record(SysOperLog operLog) {
        if (operLog == null) {
            return;
        }
        try {
            operLogMapper.insertOperLog(operLog);
        } catch (Exception e) {
            // 审计日志写失败不应影响业务请求
            log.warn("写入操作日志失败：{}", e.getMessage());
        }
    }

    @Override
    public List<SysOperLog> latest(int limit) {
        List<SysOperLog> list = operLogMapper.selectLatest(limit > 0 ? limit : 100);
        return list == null ? Collections.<SysOperLog>emptyList() : list;
    }

    @Override
    public long count() {
        return operLogMapper.countOperLog();
    }

    @Override
    public PageResult<SysOperLog> page(int pageNum, int pageSize, LogQuery query) {
        int num = PageResult.fixPageNum(pageNum);
        int size = PageResult.fixPageSize(pageSize);
        LogQuery q = query == null ? new LogQuery() : query;
        // 只给日期时补齐时分秒，保证"当天"查得到
        q.setBeginTime(TimeRange.begin(q.getBeginTime()));
        q.setEndTime(TimeRange.end(q.getEndTime()));
        List<SysOperLog> rows = operLogMapper.selectByPage(PageResult.offset(num, size), size, q);
        return PageResult.of(rows, operLogMapper.countByPage(q), num, size);
    }

}
