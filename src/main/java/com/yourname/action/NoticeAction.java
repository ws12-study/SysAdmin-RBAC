package com.yourname.action;

import com.yourname.common.BusinessType;
import com.yourname.common.PageResult;
import com.yourname.common.ServiceException;
import com.yourname.common.annotation.OperLog;
import com.yourname.common.annotation.RequiresPermissions;
import com.yourname.entity.SysNotice;
import com.yourname.entity.SysUser;
import com.yourname.service.SysNoticeService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 通知公告
 *
 * @author yourname
 */
public class NoticeAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private SysNoticeService noticeService;

    private PageResult<SysNotice> page;
    private SysNotice notice;
    private Long id;
    private Integer pageNum;
    private Integer pageSize;
    private String title;
    private String type;
    private String status;
    private String order;
    private String errorMsg;

    @RequiresPermissions("system:notice:list")
    public String list() {
        page = noticeService.page(PageResult.fixPageNum(pageNum), PageResult.fixPageSize(pageSize),
                title, type, status);
        return SUCCESS;
    }

    /**
     * 普通用户可见的公告列表：只展示"正常"状态的公告，无需 system:notice:list 权限。
     */
    public String view() {
        page = noticeService.viewPage(PageResult.fixPageNum(pageNum), PageResult.fixPageSize(pageSize),
                title, type, order);
        return SUCCESS;
    }

    /**
     * 公告详情：所有登录用户可见（不做 system:notice:list 校验）。
     * 普通用户只能查看"正常"状态的公告，已下线的公告对普通用户隐藏。
     */
    public String detail() {
        notice = noticeService.selectById(id);
        if (notice == null) {
            errorMsg = "公告不存在";
            return INPUT;
        }
        SysUser user = currentUser();
        boolean isAdmin = user != null && user.getId() != null
                && permissionService.isAdmin(user.getId());
        if (!isAdmin && !"0".equals(notice.getStatus())) {
            notice = null;
            errorMsg = "公告不存在或已下线";
            return INPUT;
        }
        return SUCCESS;
    }

    @RequiresPermissions("system:notice:list")
    public String input() {
        if (id != null) {
            notice = noticeService.selectById(id);
            if (notice == null) {
                errorMsg = "公告不存在";
                return INPUT;
            }
        } else {
            notice = new SysNotice();
            notice.setNoticeType("1");
            notice.setStatus("0");
        }
        return SUCCESS;
    }

    @RequiresPermissions("system:notice:list")
    @OperLog(title = "通知公告", type = BusinessType.UPDATE)
    public String save() {
        try {
            noticeService.save(notice, currentUserName());
            flashOk("公告保存成功");
            return SUCCESS;
        } catch (ServiceException e) {
            errorMsg = e.getMessage();
            return INPUT;
        }
    }

    @RequiresPermissions("system:notice:list")
    @OperLog(title = "通知公告", type = BusinessType.DELETE)
    public String delete() {
        try {
            noticeService.deleteById(id);
            flashOk("公告已删除");
            return SUCCESS;
        } catch (ServiceException e) {
            flashErr(e.getMessage());
            return INPUT;
        }
    }

    public PageResult<SysNotice> getPage() {
        return page;
    }

    public SysNotice getNotice() {
        return notice;
    }

    public void setNotice(SysNotice notice) {
        this.notice = notice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    /** 供分页链接复用：翻页时保留搜索条件 */
    public String getQueryString() {
        StringBuilder sb = new StringBuilder();
        com.yourname.common.QueryStringBuilder.append(sb, "title", title);
        com.yourname.common.QueryStringBuilder.append(sb, "type", type);
        com.yourname.common.QueryStringBuilder.append(sb, "status", status);
        com.yourname.common.QueryStringBuilder.append(sb, "order", order);
        return sb.toString();
    }

}
