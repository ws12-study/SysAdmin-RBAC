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
    <%@ include file="/WEB-INF/jsp/common/flash.jsp" %>
    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>

    <div class="app-card">
        <div class="card-title">
            <span><i class="bi bi-funnel"></i>筛选条件</span>
            <a class="btn btn-primary btn-sm" href="<s:url action='notice_input'/>"><i class="bi bi-plus-lg"></i> 新增公告</a>
        </div>
        <form method="get" action="<s:url action='notice_list'/>" class="row g-3 align-items-end">
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
                <label class="form-label small text-muted">状态</label>
                <select name="status" class="form-select">
                    <option value="">全部</option>
                    <option value="0" <s:if test='status == "0"'>selected</s:if>>正常</option>
                    <option value="1" <s:if test='status == "1"'>selected</s:if>>关闭</option>
                </select>
            </div>
            <div class="col-auto">
                <button type="submit" class="btn btn-primary"><i class="bi bi-search"></i> 搜索</button>
                <a class="btn btn-outline-secondary" href="<s:url action='notice_list'/>">重置</a>
            </div>
        </form>
    </div>

    <div class="app-card">
        <div class="card-title"><span><i class="bi bi-megaphone"></i>公告列表</span></div>
        <table class="table table-modern align-middle mb-0">
            <thead>
            <tr>
                <th style="width: 60px;">ID</th>
                <th>标题</th>
                <th style="width: 80px;">类型</th>
                <th style="width: 80px;">状态</th>
                <th style="width: 110px;">创建者</th>
                <th style="width: 160px;">创建时间</th>
                <th style="width: 150px;">操作</th>
            </tr>
            </thead>
            <tbody>
            <s:iterator value="page.rows" var="n">
                <tr>
                    <td><s:property value="#n.id"/></td>
                    <td><s:property value="#n.noticeTitle"/></td>
                    <td>
                        <s:if test='#n.noticeType == "2"'><span class="badge badge-soft-success">公告</span></s:if>
                        <s:else><span class="badge badge-soft-primary">通知</span></s:else>
                    </td>
                    <td>
                        <s:if test='#n.status == "0"'><span class="badge badge-soft-success">正常</span></s:if>
                        <s:else><span class="badge badge-soft-gray">关闭</span></s:else>
                    </td>
                    <td><s:property value="#n.createBy"/></td>
                    <td class="text-muted"><s:date name="#n.createTime" format="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>
                        <a class="btn-op" href="<s:url action='notice_detail'><s:param name='id' value='#n.id'/></s:url>"><i class="bi bi-eye"></i> 查看</a>
                        <a class="btn-op" href="<s:url action='notice_input'><s:param name='id' value='#n.id'/></s:url>"><i class="bi bi-pencil-square"></i> 编辑</a>
                        <a class="btn-op danger" href="<s:url action='notice_delete'><s:param name='id' value='#n.id'/></s:url>"
                           onclick="return confirm('确认删除该公告？');"><i class="bi bi-trash"></i> 删除</a>
                    </td>
                </tr>
            </s:iterator>
            <s:if test="page == null || page.rows.isEmpty()">
                <tr><td colspan="7" class="text-center text-muted py-4">暂无公告</td></tr>
            </s:if>
            </tbody>
        </table>

        <s:set var="pagerAction" value="'notice_list'"/>
        <%@ include file="/WEB-INF/jsp/common/pager.jsp" %>
    </div>
</div>
</body>
</html>
