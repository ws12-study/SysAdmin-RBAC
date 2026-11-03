<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>用户管理</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
</head>
<body>
<div class="top">
    <span class="brand">用户管理</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
</div>

<div class="app-container">
    <%@ include file="/WEB-INF/jsp/common/flash.jsp" %>

    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>

    <!-- 筛选卡片 -->
    <div class="app-card">
        <div class="card-title"><span><i class="bi bi-funnel"></i>筛选条件</span></div>
        <form method="get" action="<s:url action='user_list'/>" class="row g-3 align-items-end">
            <input type="hidden" name="pageSize" value="<s:property value='page.pageSize'/>"/>
            <div class="col-md-4">
                <label class="form-label small text-muted">账号 / 昵称</label>
                <input type="text" name="userName" class="form-control" value="<s:property value='userName'/>"/>
            </div>
            <div class="col-md-3">
                <label class="form-label small text-muted">状态</label>
                <select name="status" class="form-select">
                    <option value="">全部</option>
                    <option value="0" <s:if test='status == "0"'>selected</s:if>>正常</option>
                    <option value="1" <s:if test='status == "1"'>selected</s:if>>停用</option>
                </select>
            </div>
            <div class="col-auto">
                <button type="submit" class="btn btn-primary"><i class="bi bi-search"></i> 搜索</button>
                <a class="btn btn-outline-secondary" href="<s:url action='user_list'/>">重置</a>
            </div>
        </form>
    </div>

    <!-- 用户列表卡片 -->
    <div class="app-card">
        <div class="card-title">
            <span><i class="bi bi-people"></i>用户列表</span>
            <span class="text-muted small">当前数据范围：<b><s:property value="scopeDesc"/></b> · 共 <s:property value="page.total"/> 条</span>
        </div>

        <table class="table table-modern align-middle mb-0">
            <thead>
            <tr>
                <th style="width: 60px;">ID</th>
                <th style="width: 150px;">账号</th>
                <th style="width: 120px;">昵称</th>
                <th style="width: 130px;">部门</th>
                <th>角色</th>
                <th style="width: 90px;">状态</th>
                <th style="width: 190px;">操作</th>
            </tr>
            </thead>
            <tbody>
            <s:iterator value="page.rows" var="u">
                <tr>
                    <td><s:property value="#u.id"/></td>
                    <td><s:property value="#u.userName"/></td>
                    <td><s:property value="#u.nickName"/></td>
                    <td><s:property value="#u.deptName"/></td>
                    <td>
                        <s:if test="#u.roleNames == null || #u.roleNames == ''">
                            <span class="badge badge-soft-gray">未分配</span>
                        </s:if>
                        <s:else>
                            <span class="badge badge-soft-primary"><s:property value="#u.roleNames"/></span>
                        </s:else>
                    </td>
                    <td>
                        <s:if test='#u.status == "0"'><span class="badge badge-soft-success">正常</span></s:if>
                        <s:else><span class="badge badge-soft-gray">停用</span></s:else>
                    </td>
                    <td>
                        <a class="btn-op" href="<s:url action='user_role_input'><s:param name='userId' value='#u.id'/></s:url>">
                            <i class="bi bi-person-gear"></i> 分配角色
                        </a>
                        <s:if test='#u.status == "0"'>
                            <a class="btn-op danger"
                               href="<s:url action='user_status_save'><s:param name='userId' value='#u.id'/><s:param name='status' value='1'/></s:url>"
                               onclick="return confirm('停用后该账号将无法登录，确认停用？');">
                                <i class="bi bi-pause-circle"></i> 停用
                            </a>
                        </s:if>
                        <s:else>
                            <a class="btn-op success"
                               href="<s:url action='user_status_save'><s:param name='userId' value='#u.id'/><s:param name='status' value='0'/></s:url>">
                                <i class="bi bi-play-circle"></i> 启用
                            </a>
                        </s:else>
                    </td>
                </tr>
            </s:iterator>
            </tbody>
        </table>

        <s:if test="page.rows.isEmpty()">
            <div class="text-center text-muted py-4">当前数据范围内没有用户。</div>
        </s:if>

        <s:set var="pagerAction" value="'user_list'"/>
        <%@ include file="/WEB-INF/jsp/common/pager.jsp" %>
    </div>
</div>
</body>
</html>
