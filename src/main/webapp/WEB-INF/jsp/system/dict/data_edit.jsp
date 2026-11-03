<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>字典数据编辑</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
</head>
<body>
<div class="top">
    <span class="brand">字典数据编辑</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
    <a href="<s:url action='dict_data_list'><s:param name='type' value='dictData.dictType'/></s:url>">返回列表</a>
</div>

<div class="app-container" style="max-width: 640px;">
    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>
    <s:actionerror/>
    <s:fielderror/>

    <div class="app-card">
        <div class="card-title"><span><i class="bi bi-pencil-square"></i>字典数据信息</span></div>
        <s:form action="dict_data_save" namespace="/" theme="simple" cssClass="row g-3">
            <s:hidden name="dictData.id" value="%{dictData.id}"/>

            <div class="col-md-6">
                <label class="form-label small text-muted">字典类型</label>
                <s:textfield name="dictData.dictType" cssClass="form-control" readonly="true"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">字典标签（显示文本）</label>
                <s:textfield name="dictData.dictLabel" cssClass="form-control"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">字典键值（存储值）</label>
                <s:textfield name="dictData.dictValue" cssClass="form-control"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">显示排序</label>
                <s:textfield name="dictData.dictSort" cssClass="form-control"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">回显样式（primary / success / info / danger）</label>
                <s:textfield name="dictData.listClass" cssClass="form-control"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">是否默认</label>
                <s:select name="dictData.isDefault" cssClass="form-select"
                          list="#{'Y':'是','N':'否'}" value="%{dictData.isDefault}"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">状态（下拉项来自字典 sys_normal_disable）</label>
                <s:select name="dictData.status" cssClass="form-select"
                          list="#application.dictMap['sys_normal_disable']"
                          listKey="dictValue" listValue="dictLabel"
                          value="%{dictData.status}"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">备注</label>
                <s:textfield name="dictData.remark" cssClass="form-control"/>
            </div>

            <div class="col-12">
                <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg"></i> 保存</button>
                <a class="btn btn-outline-secondary ms-2" href="<s:url action='dict_data_list'><s:param name='type' value='dictData.dictType'/></s:url>">取消</a>
            </div>
        </s:form>
    </div>
</div>
</body>
</html>
