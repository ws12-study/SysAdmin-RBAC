<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>菜单管理</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
    <style>
        code { background: #f2f4f7; padding: 2px 6px; border-radius: 4px; color: #57606f; font-size: 12px; }
    </style>
</head>
<body>
<div class="top">
    <span class="brand">菜单管理</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
</div>

<div class="app-container">
    <%@ include file="/WEB-INF/jsp/common/flash.jsp" %>
    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>

    <div class="app-card">
        <div class="card-title">
            <span><i class="bi bi-diagram-3"></i>菜单列表</span>
            <a class="btn btn-primary btn-sm" href="<s:url action='menu_input'/>"><i class="bi bi-plus-lg"></i> 新增菜单</a>
        </div>
        <table class="table table-modern align-middle mb-0">
            <thead>
            <tr>
                <th style="width: 260px;">菜单名称</th>
                <th style="width: 60px;">排序</th>
                <th style="width: 140px;">路由地址</th>
                <th style="width: 170px;">权限标识</th>
                <th style="width: 70px;">显示</th>
                <th style="width: 70px;">状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <s:iterator value="menus" var="m">
                <tr>
                    <td>
                        <span style="padding-left:<s:property value='#m.depth * 22'/>px; display:inline-block;">
                            <s:if test="#m.depth > 0">└ </s:if>
                            <s:if test='#m.menuType == "M"'><span class="badge badge-soft-primary">目录</span></s:if>
                            <s:elseif test='#m.menuType == "C"'><span class="badge badge-soft-success">菜单</span></s:elseif>
                            <s:else><span class="badge badge-soft-gray">按钮</span></s:else>
                            <s:property value="#m.menuName"/>
                        </span>
                    </td>
                    <td><s:property value="#m.orderNum"/></td>
                    <td><code><s:property value="#m.path"/></code></td>
                    <td><code><s:property value="#m.perms"/></code></td>
                    <td>
                        <s:if test='#m.visible == "0"'><span class="badge badge-soft-success">显示</span></s:if>
                        <s:else><span class="badge badge-soft-gray">隐藏</span></s:else>
                    </td>
                    <td>
                        <s:if test='#m.status == "0"'><span class="badge badge-soft-success">正常</span></s:if>
                        <s:else><span class="badge badge-soft-gray">停用</span></s:else>
                    </td>
                    <td>
                        <a class="btn-op" href="<s:url action='menu_input'><s:param name='id' value='#m.id'/></s:url>"><i class="bi bi-pencil-square"></i> 编辑</a>
                        <a class="btn-op danger" href="<s:url action='menu_delete'><s:param name='id' value='#m.id'/></s:url>"
                           onclick="return confirm('确认删除该菜单？');"><i class="bi bi-trash"></i> 删除</a>
                    </td>
                </tr>
            </s:iterator>
            <s:if test="menus == null || menus.isEmpty()">
                <tr><td colspan="7" class="text-center text-muted py-4">暂无菜单</td></tr>
            </s:if>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
