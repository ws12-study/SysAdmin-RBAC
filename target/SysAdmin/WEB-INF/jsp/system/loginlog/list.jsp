<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>SysAdmin · 登录日志</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
</head>
<body>
<div class="top">
    <span class="brand">登录日志</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
</div>

<div class="app-container">
    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>

    <div class="app-card">
        <div class="card-title"><span><i class="bi bi-funnel"></i>筛选条件</span></div>
        <form method="get" action="<s:url action='loginlog_list'/>" class="row g-3 align-items-end">
            <input type="hidden" name="pageSize" value="<s:property value='page.pageSize'/>"/>
            <div class="col-md-3">
                <label class="form-label small text-muted">账号</label>
                <input type="text" name="query.loginName" class="form-control" value="<s:property value='query.loginName'/>"/>
            </div>
            <div class="col-md-2">
                <label class="form-label small text-muted">状态</label>
                <select name="query.status" class="form-select">
                    <option value="">全部</option>
                    <option value="0" <s:if test='query.status == "0"'>selected</s:if>>成功</option>
                    <option value="1" <s:if test='query.status == "1"'>selected</s:if>>失败</option>
                </select>
            </div>
            <div class="col-md-3">
                <label class="form-label small text-muted">开始时间</label>
                <input type="text" name="query.beginTime" class="form-control" value="<s:property value='query.beginTime'/>" placeholder="2026-01-01"/>
            </div>
            <div class="col-md-3">
                <label class="form-label small text-muted">结束时间</label>
                <input type="text" name="query.endTime" class="form-control" value="<s:property value='query.endTime'/>"/>
            </div>
            <div class="col-auto">
                <button type="submit" class="btn btn-primary"><i class="bi bi-search"></i> 搜索</button>
                <a class="btn btn-outline-secondary" href="<s:url action='loginlog_list'/>">重置</a>
            </div>
        </form>
    </div>

    <div class="app-card">
        <div class="card-title">
            <span><i class="bi bi-clock-history"></i>登录日志</span>
            <span class="text-muted small">共 <s:property value="page.total"/> 条</span>
        </div>
        <s:if test="page == null || page.rows.isEmpty()">
            <div class="text-center text-muted py-4">暂无登录记录。</div>
        </s:if>
        <s:else>
            <table class="table table-modern align-middle mb-0">
                <thead>
                <tr>
                    <th style="width: 150px;">登录时间</th>
                    <th style="width: 110px;">登录账号</th>
                    <th style="width: 70px;">状态</th>
                    <th style="width: 240px;">提示消息</th>
                    <th style="width: 120px;">IP</th>
                    <th>浏览器 / 系统</th>
                </tr>
                </thead>
                <tbody>
                <s:iterator value="page.rows" var="l">
                    <tr>
                        <td class="text-muted"><s:date name="#l.loginTime" format="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><s:property value="#l.loginName"/></td>
                        <td>
                            <s:if test='#l.status == "1"'><span class="badge badge-soft-danger">失败</span></s:if>
                            <s:else><span class="badge badge-soft-success">成功</span></s:else>
                        </td>
                        <td class="text-muted"><s:property value="#l.msg"/></td>
                        <td><s:property value="#l.ipAddress"/></td>
                        <td class="text-muted"><s:property value="#l.browser"/> / <s:property value="#l.os"/></td>
                    </tr>
                </s:iterator>
                </tbody>
            </table>
        </s:else>

        <s:set var="pagerAction" value="'loginlog_list'"/>
        <%@ include file="/WEB-INF/jsp/common/pager.jsp" %>
    </div>
</div>
</body>
</html>
