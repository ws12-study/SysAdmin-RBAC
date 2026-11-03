<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>部门管理</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
</head>
<body>
<div class="top">
    <span class="brand">部门管理</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
</div>

<div class="app-container">
    <%@ include file="/WEB-INF/jsp/common/flash.jsp" %>
    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>

    <div class="app-card">
        <div class="card-title">
            <span><i class="bi bi-diagram-2"></i>部门列表</span>
            <a class="btn btn-primary btn-sm" href="<s:url action='dept_input'/>"><i class="bi bi-plus-lg"></i> 新增部门</a>
        </div>
        <table class="table table-modern align-middle mb-0">
            <thead>
            <tr>
                <th style="width: 300px;">部门名称</th>
                <th style="width: 70px;">排序</th>
                <th style="width: 100px;">负责人</th>
                <th style="width: 140px;">联系电话</th>
                <th style="width: 80px;">状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <s:iterator value="depts" var="d">
                <tr>
                    <td>
                        <span style="padding-left:<s:property value='#d.depth * 22'/>px; display:inline-block;">
                            <s:if test="#d.depth > 0">└ </s:if><s:property value="#d.deptName"/>
                        </span>
                    </td>
                    <td><s:property value="#d.orderNum"/></td>
                    <td><s:property value="#d.leader"/></td>
                    <td><s:property value="#d.phone"/></td>
                    <td>
                        <s:if test='#d.status == "0"'><span class="badge badge-soft-success">正常</span></s:if>
                        <s:else><span class="badge badge-soft-gray">停用</span></s:else>
                    </td>
                    <td>
                        <a class="btn-op" href="<s:url action='dept_input'><s:param name='id' value='#d.id'/></s:url>"><i class="bi bi-pencil-square"></i> 编辑</a>
                        <a class="btn-op danger" href="<s:url action='dept_delete'><s:param name='id' value='#d.id'/></s:url>"
                           onclick="return confirm('确认删除该部门？');"><i class="bi bi-trash"></i> 删除</a>
                    </td>
                </tr>
            </s:iterator>
            <s:if test="depts == null || depts.isEmpty()">
                <tr><td colspan="6" class="text-center text-muted py-4">暂无部门</td></tr>
            </s:if>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
