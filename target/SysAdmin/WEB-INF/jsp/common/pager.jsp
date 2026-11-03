<%-- 分页条：页面需先 <s:set var="pagerAction" value="'xxx_list'"/> 再 include；
     搜索条件由 Action 的 getQueryString() 提供，翻页时自动带上 --%>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style>
    .pager { margin-top: 16px; text-align: right; font-size: 13px; color: #8896a6; }
    .pager a, .pager span.cur, .pager span.off {
        display: inline-block; padding: 5px 12px; margin-left: 6px;
        border-radius: 6px; transition: all .15s;
    }
    .pager a { border: 1px solid #e1e5eb; color: #333; text-decoration: none; background: #fff; }
    .pager a:hover { color: #1677ff; border-color: #1677ff; }
    .pager span.cur { border: none; background: linear-gradient(135deg, #4776E6 0%, #8E54E9 100%);
                      color: #fff; box-shadow: 0 3px 10px rgba(71, 118, 230, .3); }
    .pager span.off { border: 1px solid #eef1f5; color: #c0c6cf; background: #fafbfd; }
</style>
<s:if test="page != null && page.totalPages > 1">
    <div class="pager">
        第 <s:property value="page.startRow"/> - <s:property value="page.endRow"/> 条 / 共 <s:property value="page.total"/> 条
        <s:if test="page.hasPrev">
            <a href="<s:url action='%{#pagerAction}'/>?pageNum=1&amp;pageSize=<s:property value='page.pageSize'/><s:property value='queryString'/>">首页</a>
            <a href="<s:url action='%{#pagerAction}'/>?pageNum=<s:property value='page.pageNum - 1'/>&amp;pageSize=<s:property value='page.pageSize'/><s:property value='queryString'/>">上一页</a>
        </s:if>
        <s:else>
            <span class="off">首页</span><span class="off">上一页</span>
        </s:else>
        <span class="cur"><s:property value="page.pageNum"/> / <s:property value="page.totalPages"/></span>
        <s:if test="page.hasNext">
            <a href="<s:url action='%{#pagerAction}'/>?pageNum=<s:property value='page.pageNum + 1'/>&amp;pageSize=<s:property value='page.pageSize'/><s:property value='queryString'/>">下一页</a>
            <a href="<s:url action='%{#pagerAction}'/>?pageNum=<s:property value='page.totalPages'/>&amp;pageSize=<s:property value='page.pageSize'/><s:property value='queryString'/>">末页</a>
        </s:if>
        <s:else>
            <span class="off">下一页</span><span class="off">末页</span>
        </s:else>
    </div>
</s:if>
