<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>通知公告</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
</head>
<body>
<div class="top">
    <span class="brand">通知公告</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
</div>

<div class="app-container">
    <div class="app-card">
        <div class="card-title"><span><i class="bi bi-funnel"></i>筛选条件</span></div>
        <form method="get" action="<s:url action='notice_view'/>" class="row g-3 align-items-end">
            <input type="hidden" name="pageSize" value="<s:property value='page.pageSize'/>"/>
            <div class="col-md-4">
                <label class="form-label small text-muted">标题</label>
                <input type="text" name="title" class="form-control" value="<s:property value='title'/>"/>
            </div>
            <div class="col-md-3">
                <label class="form-label small text-muted">类型</label>
                <select name="type" class="form-select">
                    <option value="">全部</option>
                    <option value="1" <s:if test='type == "1"'>selected</s:if>>通知</option>
                    <option value="2" <s:if test='type == "2"'>selected</s:if>>公告</option>
                </select>
            </div>
            <div class="col-md-3">
                <label class="form-label small text-muted">排序</label>
                <select name="order" class="form-select">
                    <option value="desc" <s:if test='order != "asc"'>selected</s:if>>最新优先</option>
                    <option value="asc" <s:if test='order == "asc"'>selected</s:if>>最早优先</option>
                </select>
            </div>
            <div class="col-auto">
                <button type="submit" class="btn btn-primary"><i class="bi bi-search"></i> 搜索</button>
                <a class="btn btn-outline-secondary" href="<s:url action='notice_view'/>">重置</a>
            </div>
        </form>
    </div>

    <div class="app-card">
        <div class="card-title"><span><i class="bi bi-megaphone"></i>公告列表</span></div>
        <table class="table table-modern align-middle mb-0">
            <thead>
            <tr>
                <th style="width: 80px;">类型</th>
                <th>标题</th>
                <th style="width: 110px;">发布人</th>
                <th style="width: 160px;">发布时间</th>
            </tr>
            </thead>
            <tbody>
            <s:iterator value="page.rows" var="n">
                <tr>
                    <td>
                        <s:if test='#n.noticeType == "2"'><span class="badge badge-soft-success">公告</span></s:if>
                        <s:else><span class="badge badge-soft-primary">通知</span></s:else>
                    </td>
                    <td>
                        <a class="text-decoration-none" style="color: #1677ff;"
                           href="<s:url action='notice_detail'><s:param name='id' value='#n.id'/></s:url>">
                            <s:property value="#n.noticeTitle"/>
                        </a>
                    </td>
                    <td><s:property value="#n.createBy"/></td>
                    <td class="text-muted"><s:date name="#n.createTime" format="yyyy-MM-dd HH:mm:ss"/></td>
                </tr>
            </s:iterator>
            <s:if test="page == null || page.rows.isEmpty()">
                <tr><td colspan="4" class="text-center text-muted py-4">暂无公告</td></tr>
            </s:if>
            </tbody>
        </table>

        <s:set var="pagerAction" value="'notice_view'"/>
        <%@ include file="/WEB-INF/jsp/common/pager.jsp" %>
    </div>
</div>
</body>
</html>
