<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>系统出错了</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
</head>
<body>
<div class="app-container" style="max-width: 560px;">
    <div class="app-card text-center" style="padding: 60px 40px;">
        <div style="font-size: 72px; color: #f5576c; line-height: 1;"><i class="bi bi-bug"></i></div>
        <h3 class="mt-3 mb-2" style="font-weight: 700;">500 · 系统出错了</h3>
        <p class="text-muted mb-4"><s:property value="exception.message"/></p>
        <a class="btn btn-primary" href="<s:url action='index'/>"><i class="bi bi-house"></i> 返回首页</a>
    </div>
</div>
</body>
</html>
