<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>公告编辑</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
</head>
<body>
<div class="top">
    <span class="brand">公告编辑</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
    <a href="<s:url action='notice_list'/>">返回列表</a>
</div>

<div class="app-container" style="max-width: 720px;">
    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>

    <div class="app-card">
        <div class="card-title"><span><i class="bi bi-pencil-square"></i>公告信息</span></div>
        <form action="<s:url action='notice_save'/>" method="post" class="row g-3">
            <s:hidden name="notice.id"/>
            <div class="col-12">
                <label class="form-label small text-muted">公告标题</label>
                <input type="text" class="form-control" name="notice.noticeTitle" value="<s:property value='notice.noticeTitle'/>"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">公告类型</label>
                <select name="notice.noticeType" class="form-select">
                    <option value="1" <s:if test='notice.noticeType == "1"'>selected</s:if>>通知</option>
                    <option value="2" <s:if test='notice.noticeType == "2"'>selected</s:if>>公告</option>
                </select>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">状态</label>
                <select name="notice.status" class="form-select">
                    <option value="0" <s:if test='notice.status == "0"'>selected</s:if>>正常</option>
                    <option value="1" <s:if test='notice.status == "1"'>selected</s:if>>关闭</option>
                </select>
            </div>
            <div class="col-12">
                <label class="form-label small text-muted">公告内容</label>
                <textarea class="form-control" name="notice.noticeContent" rows="6"><s:property value="notice.noticeContent"/></textarea>
            </div>
            <div class="col-12">
                <label class="form-label small text-muted">备注</label>
                <textarea class="form-control" name="notice.remark" rows="2"><s:property value="notice.remark"/></textarea>
            </div>
            <div class="col-12">
                <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg"></i> 保存</button>
                <a class="btn btn-outline-secondary ms-2" href="<s:url action='notice_list'/>">取消</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
