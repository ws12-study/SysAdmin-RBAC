<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>部门编辑</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
</head>
<body>
<div class="top">
    <span class="brand">部门编辑</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
    <a href="<s:url action='dept_list'/>">返回列表</a>
</div>

<div class="app-container" style="max-width: 720px;">
    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>

    <div class="app-card">
        <div class="card-title"><span><i class="bi bi-pencil-square"></i>部门信息</span></div>
        <form action="<s:url action='dept_save'/>" method="post" class="row g-3">
            <s:hidden name="dept.id"/>
            <div class="col-md-6">
                <label class="form-label small text-muted">上级部门</label>
                <select name="dept.parentId" class="form-select">
                    <option value="0">顶级部门</option>
                    <s:iterator value="allDepts" var="p">
                        <option value="<s:property value='#p.id'/>"
                            <s:if test="dept.parentId == #p.id">selected</s:if>>
                            <s:property value="#p.deptName"/>
                        </option>
                    </s:iterator>
                </select>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">部门名称</label>
                <input type="text" class="form-control" name="dept.deptName" value="<s:property value='dept.deptName'/>"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">显示顺序</label>
                <input type="text" class="form-control" name="dept.orderNum" value="<s:property value='dept.orderNum'/>"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">负责人</label>
                <input type="text" class="form-control" name="dept.leader" value="<s:property value='dept.leader'/>"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">联系电话</label>
                <input type="text" class="form-control" name="dept.phone" value="<s:property value='dept.phone'/>"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">邮箱</label>
                <input type="text" class="form-control" name="dept.email" value="<s:property value='dept.email'/>"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">状态</label>
                <select name="dept.status" class="form-select">
                    <option value="0" <s:if test='dept.status == "0"'>selected</s:if>>正常</option>
                    <option value="1" <s:if test='dept.status == "1"'>selected</s:if>>停用</option>
                </select>
            </div>
            <div class="col-12">
                <label class="form-label small text-muted">备注</label>
                <textarea class="form-control" name="dept.remark" rows="2"><s:property value="dept.remark"/></textarea>
            </div>
            <div class="col-12">
                <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg"></i> 保存</button>
                <a class="btn btn-outline-secondary ms-2" href="<s:url action='dept_list'/>">取消</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
