<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>字典编辑</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
</head>
<body>
<div class="top">
    <span class="brand">字典编辑</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
    <a href="<s:url action='dict_type_list'/>">返回列表</a>
</div>

<div class="app-container" style="max-width: 640px;">
    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>
    <s:actionerror/>
    <s:fielderror/>

    <div class="app-card">
        <div class="card-title"><span><i class="bi bi-pencil-square"></i>字典信息</span></div>
        <s:form action="dict_type_save" namespace="/" theme="simple" cssClass="row g-3">
            <s:hidden name="dictType.id" value="%{dictType.id}"/>

            <div class="col-12">
                <label class="form-label small text-muted">字典名称</label>
                <s:textfield name="dictType.dictName" cssClass="form-control"/>
            </div>
            <div class="col-12">
                <label class="form-label small text-muted">字典类型（唯一标识，如 sys_user_sex）</label>
                <s:textfield name="dictType.dictType" cssClass="form-control"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">状态（下拉项来自字典 sys_normal_disable）</label>
                <s:select name="dictType.status" cssClass="form-select"
                          list="#application.dictMap['sys_normal_disable']"
                          listKey="dictValue" listValue="dictLabel"
                          value="%{dictType.status}"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">备注</label>
                <s:textfield name="dictType.remark" cssClass="form-control"/>
            </div>

            <div class="col-12">
                <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg"></i> 保存</button>
                <a class="btn btn-outline-secondary ms-2" href="<s:url action='dict_type_list'/>">取消</a>
            </div>
        </s:form>
        <div class="text-muted small mt-2">保存后字典缓存会自动刷新，页面下拉立即可见。</div>
    </div>
</div>
</body>
</html>
