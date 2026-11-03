<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>分配角色</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
</head>
<body>
<div class="top">
    <span class="brand">分配角色</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
    <a href="<s:url action='user_list'/>">返回列表</a>
</div>

<div class="app-container" style="max-width: 640px;">
    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>

    <div class="app-card">
        <div class="card-title">
            <span><i class="bi bi-person-gear"></i>分配角色</span>
            <span class="text-muted small">用户：<b><s:property value="targetUser.nickName"/></b>（<s:property value="targetUser.userName"/>）</span>
        </div>
        <s:form action="user_role_save" namespace="/" theme="simple">
            <s:hidden name="userId" value="%{userId}"/>
            <s:iterator value="allRoles" var="r">
                <div class="form-check py-2 border-bottom">
                    <input class="form-check-input" type="checkbox" name="checkedRoleIds" value="<s:property value='#r.id'/>"
                           <s:if test="checkedRoleIds.contains(#r.id)">checked="checked"</s:if>/>
                    <label class="form-check-label">
                        <s:property value="#r.roleName"/>
                        <span class="text-muted small ms-2">key=<s:property value="#r.roleKey"/> · 数据范围=<s:property value="#r.dataScope"/></span>
                    </label>
                </div>
            </s:iterator>
            <div class="mt-4">
                <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg"></i> 保存授权</button>
                <a class="btn btn-outline-secondary ms-2" href="<s:url action='user_list'/>">取消</a>
            </div>
        </s:form>
    </div>
</div>
</body>
</html>
