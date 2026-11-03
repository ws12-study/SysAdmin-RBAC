package com.yourname.common;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页结果（不引入分页插件，手写 LIMIT 即可）
 *
 * @author yourname
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 默认每页条数 */
    public static final int DEFAULT_PAGE_SIZE = 10;
    /** 每页条数上限，防止 pageSize=99999 拖垮数据库 */
    public static final int MAX_PAGE_SIZE = 200;

    /** 当前页，从 1 开始 */
    private int pageNum;
    private int pageSize;
    /** 总记录数 */
    private long total;
    /** 当前页数据 */
    private List<T> rows;

    public static <T> PageResult<T> of(List<T> rows, long total, int pageNum, int pageSize) {
        PageResult<T> page = new PageResult<T>();
        page.rows = rows == null ? new ArrayList<T>() : rows;
        page.total = total < 0 ? 0 : total;
        page.pageNum = fixPageNum(pageNum);
        page.pageSize = fixPageSize(pageSize);
        return page;
    }

    public static int fixPageNum(Integer pageNum) {
        return pageNum == null || pageNum < 1 ? 1 : pageNum;
    }

    public static int fixPageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(pageSize, MAX_PAGE_SIZE);
    }

    public static int offset(int pageNum, int pageSize) {
        return (fixPageNum(pageNum) - 1) * fixPageSize(pageSize);
    }

    public long getTotalPages() {
        if (pageSize <= 0) {
            return 0;
        }
        return (total + pageSize - 1) / pageSize;
    }

    public boolean getHasPrev() {
        return pageNum > 1;
    }

    public boolean getHasNext() {
        return pageNum < getTotalPages();
    }

    /** 当前页起始序号（用于"第 x - y 条"展示） */
    public long getStartRow() {
        return total == 0 ? 0 : (pageNum - 1) * pageSize + 1;
    }

    public long getEndRow() {
        return Math.min(total, (long) pageNum * pageSize);
    }

}
