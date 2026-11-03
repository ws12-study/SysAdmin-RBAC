<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>角色管理</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
</head>
<body>
<div class="top">
    <span class="brand">角色管理</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
</div>

<div class="app-container">
    <%@ include file="/WEB-INF/jsp/common/flash.jsp" %>
    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>

    <div class="app-card">
        <div class="card-title">
            <span><i class="bi bi-person-badge"></i>角色列表</span>
            <a class="btn btn-primary btn-sm" href="<s:url action='role_input'/>"><i class="bi bi-plus-lg"></i> 新增角色</a>
        </div>
        <table class="table table-modern align-middle mb-0">
            <thead>
            <tr>
                <th style="width: 60px;">ID</th>
                <th>角色名称</th>
                <th>权限字符</th>
                <th style="width: 70px;">排序</th>
                <th>数据范围</th>
                <th style="width: 80px;">状态</th>
                <th style="width: 180px;">操作</th>
            </tr>
            </thead>
            <tbody>
            <s:iterator value="roles" var="r">
                <tr>
                    <td><s:property value="#r.id"/></td>
                    <td><s:property value="#r.roleName"/></td>
                    <td><span class="badge badge-soft-gray"><s:property value="#r.roleKey"/></span></td>
                    <td><s:property value="#r.roleSort"/></td>
                    <td>
                        <s:if test='#r.dataScope == "1"'>全部数据</s:if>
                        <s:elseif test='#r.dataScope == "2"'>自定义</s:elseif>
                        <s:elseif test='#r.dataScope == "3"'>本部门</s:elseif>
                        <s:elseif test='#r.dataScope == "4"'>本部门及以下</s:elseif>
                        <s:else>仅本人</s:else>
                    </td>
                    <td>
                        <s:if test='#r.status == "0"'><span class="badge badge-soft-success">正常</span></s:if>
                        <s:else><span class="badge badge-soft-gray">停用</span></s:else>
                    </td>
                    <td>
                        <a class="btn-op" href="<s:url action='role_input'><s:param name='roleId' value='#r.id'/></s:url>"><i class="bi bi-pencil-square"></i> 编辑/授权</a>
                        <a class="btn-op danger" href="<s:url action='role_delete'><s:param name='roleId' value='#r.id'/></s:url>"
                           onclick="return confirm('确认删除该角色？')"><i class="bi bi-trash"></i> 删除</a>
                    </td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
