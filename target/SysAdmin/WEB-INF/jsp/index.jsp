<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>SysAdmin 首页</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
</head>
<body>
<div class="top">
    <span class="brand">SysAdmin 控制台</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
</div>

<div class="app-container">

    <!-- 欢迎横幅 -->
    <div class="app-card" style="background: linear-gradient(135deg, #4776E6 0%, #8E54E9 100%); color: #fff; border: none;">
        <div class="d-flex align-items-center flex-wrap gap-3">
            <div class="me-auto">
                <h4 class="mb-1" style="color: #fff; font-weight: 700;">欢迎回来，<s:property value="currentUser.nickName"/></h4>
                <div class="small" style="opacity: .9;">
                    账号：<s:property value="currentUser.userName"/> ·
                    上次登录：<s:date name="currentUser.loginDate" format="yyyy-MM-dd HH:mm:ss"/>
                </div>
            </div>
            <div>
                <s:iterator value="roles" var="r">
                    <span class="badge" style="background: rgba(255,255,255,.22); color: #fff;"><s:property value="#r.roleName"/></span>
                </s:iterator>
                <s:if test="isAdmin">
                    <span class="badge" style="background: rgba(255,255,255,.34); color: #fff;">超级管理员</span>
                </s:if>
                <s:else>
                    <span class="badge" style="background: rgba(255,255,255,.18); color: #fff;">普通用户</span>
                </s:else>
            </div>
        </div>
    </div>

    <!-- 统计卡片 -->
    <div class="row g-3" style="margin-bottom: 20px;">
        <div class="col-md-4">
            <div class="stat-card">
                <div class="icon" style="background: linear-gradient(135deg, #4776E6 0%, #8E54E9 100%);">
                    <i class="bi bi-people-fill"></i>
                </div>
                <div>
                    <div class="num"><s:property value="userCount"/></div>
                    <div class="label">用户总数</div>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stat-card">
                <div class="icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                    <i class="bi bi-shield-lock-fill"></i>
                </div>
                <div>
                    <div class="num"><s:property value="loginCount"/></div>
                    <div class="label"><s:if test="isAdmin">登录记录</s:if><s:else>我的登录次数</s:else></div>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stat-card">
                <div class="icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                    <i class="bi bi-megaphone-fill"></i>
                </div>
                <div>
                    <div class="num"><s:property value="noticeCount"/></div>
                    <div class="label">通知公告</div>
                </div>
            </div>
        </div>
    </div>

    <!-- 最新公告 -->
    <div class="app-card">
        <div class="card-title">
            <span><i class="bi bi-megaphone"></i>最新公告</span>
            <a class="btn btn-sm btn-outline-secondary" href="<c:url value='/notice_view.action'/>">查看全部</a>
        </div>
        <table class="table table-modern align-middle mb-0">
            <thead>
            <tr>
                <th style="width: 90px;">类型</th>
                <th>标题</th>
                <th style="width: 180px;">发布时间</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${latestNotices}" var="n">
                <tr>
                    <td><span class="badge ${n.noticeType == '2' ? 'badge-soft-success' : 'badge-soft-primary'}">${n.noticeType == '2' ? '公告' : '通知'}</span></td>
                    <td>
                        <a class="text-decoration-none" style="color: #1677ff;"
                           href="<c:url value='/notice_detail.action'><c:param name='id' value='${n.id}'/></c:url>">
                            <c:out value="${n.noticeTitle}"/>
                        </a>
                    </td>
                    <td class="text-muted"><fmt:formatDate value="${n.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                </tr>
            </c:forEach>
            <c:if test="${empty latestNotices}">
                <tr><td colspan="3" class="text-center text-muted py-4">暂无公告</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>

    <!-- 最近登录 -->
    <div class="app-card">
        <div class="card-title"><span><i class="bi bi-clock-history"></i>最近登录</span></div>
        <table class="table table-modern align-middle mb-0">
            <thead>
            <tr>
                <th>IP</th>
                <th>浏览器</th>
                <th>状态</th>
                <th>提示</th>
                <th>时间</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${latestLogs}" var="log">
                <tr>
                    <td>${log.ipAddress}</td>
                    <td>${log.browser}</td>
                    <td><span class="badge ${log.status == '0' ? 'badge-soft-success' : 'badge-soft-danger'}">${log.status == '0' ? '成功' : '失败'}</span></td>
                    <td>${log.msg}</td>
                    <td class="text-muted"><fmt:formatDate value="${log.loginTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                </tr>
            </c:forEach>
            <c:if test="${empty latestLogs}">
                <tr><td colspan="5" class="text-center text-muted py-4">暂无记录</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>

</div>
</body>
</html>
