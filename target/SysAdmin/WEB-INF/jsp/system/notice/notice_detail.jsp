<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>公告详情</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
</head>
<body>
<div class="top">
    <span class="brand">公告详情</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
    <a href="<s:url action='index'/>">返回首页</a>
</div>

<div class="app-container" style="max-width: 820px;">
    <div class="app-card">
        <s:if test="notice != null">
            <h3 style="font-weight: 700; margin: 0 0 12px; color: #1f2d3d;"><s:property value="notice.noticeTitle"/></h3>
            <div class="d-flex align-items-center flex-wrap gap-2 text-muted small pb-3 mb-3" style="border-bottom: 1px solid #f0f2f5;">
                <s:if test='notice.noticeType == "2"'>
                    <span class="badge badge-soft-success">公告</span>
                </s:if>
                <s:else>
                    <span class="badge badge-soft-primary">通知</span>
                </s:else>
                <span>发布人：<s:property value="notice.createBy"/></span>
                <span>发布时间：<s:date name="notice.createTime" format="yyyy-MM-dd HH:mm:ss"/></span>
            </div>
            <div style="font-size: 14px; line-height: 1.8; color: #333; white-space: pre-wrap; word-break: break-word;">
                <s:property value="notice.noticeContent"/>
            </div>
            <s:if test="notice.remark != null && notice.remark != ''">
                <div class="text-muted small mt-3 pt-3" style="border-top: 1px dashed #e8e8e8;">备注：<s:property value="notice.remark"/></div>
            </s:if>
        </s:if>
        <s:else>
            <div class="text-center text-danger py-5"><s:property value="errorMsg"/></div>
        </s:else>
        <a class="btn btn-primary mt-3" href="<s:url action='index'/>"><i class="bi bi-arrow-left"></i> 返回首页</a>
    </div>
</div>
</body>
</html>
