package com.yourname.action;

import com.yourname.common.PageResult;
import com.yourname.common.QueryStringBuilder;
import com.yourname.common.ServiceException;
import com.yourname.common.annotation.RequiresPermissions;
import com.yourname.entity.SysLoginLog;
import com.yourname.entity.query.LogQuery;
import com.yourname.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 登录日志查询
 *
 * @author yourname
 */
public class LoginLogAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private SysUserService userService;

    private PageResult<SysLoginLog> page;
    /** 页码，从 1 开始 */
    private Integer pageNum;
    private Integer pageSize;
    /** 筛选条件 */
    private LogQuery query;
    private String errorMsg;

    @RequiresPermissions("system:loginlog:list")
    public String list() {
        try {
            if (query == null) {
                query = new LogQuery();
            }
            page = userService.loginLogPage(PageResult.fixPageNum(pageNum),
                    PageResult.fixPageSize(pageSize), query);
            return SUCCESS;
        } catch (ServiceException e) {
            // 时间格式等参数错误：提示并回退到无条件查询
            errorMsg = e.getMessage();
            query = new LogQuery();
            page = userService.loginLogPage(PageResult.fixPageNum(pageNum), PageResult.fixPageSize(pageSize), query);
            return SUCCESS;
        }
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public PageResult<SysLoginLog> getPage() {
        return page;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public LogQuery getQuery() {
        return query;
    }

    public void setQuery(LogQuery query) {
        this.query = query;
    }

    /** 供分页链接复用：翻页时保留搜索条件 */
    public String getQueryString() {
        StringBuilder sb = new StringBuilder();
        if (query != null) {
            QueryStringBuilder.append(sb, "query.loginName", query.getLoginName());
            QueryStringBuilder.append(sb, "query.status", query.getStatus());
            QueryStringBuilder.append(sb, "query.beginTime", query.getBeginTime());
            QueryStringBuilder.append(sb, "query.endTime", query.getEndTime());
        }
        return sb.toString();
    }

}
