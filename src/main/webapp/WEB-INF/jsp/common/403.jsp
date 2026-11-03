<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>无访问权限</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
</head>
<body>
<div class="app-container" style="max-width: 560px;">
    <div class="app-card text-center" style="padding: 60px 40px;">
        <div style="font-size: 72px; color: #f5576c; line-height: 1;"><i class="bi bi-shield-exclamation"></i></div>
        <h3 class="mt-3 mb-2" style="font-weight: 700;">403 · 没有访问权限</h3>
        <p class="text-muted mb-4">当前账号：<b><s:property value="#session.loginUser.userName"/></b>，权限不足。</p>
        <a class="btn btn-primary" href="<s:url action='index'/>"><i class="bi bi-house"></i> 返回首页</a>
        <a class="btn btn-outline-secondary ms-2" href="<s:url action='logout'/>">切换账号</a>
    </div>
</div>
</body>
</html>
