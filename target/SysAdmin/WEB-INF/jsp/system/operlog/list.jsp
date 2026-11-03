<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>SysAdmin · 操作日志</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
</head>
<body>
<div class="top">
    <span class="brand">操作日志</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
</div>

<div class="app-container">
    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>

    <div class="app-card">
        <div class="card-title"><span><i class="bi bi-funnel"></i>筛选条件</span></div>
        <form method="get" action="<s:url action='operlog_list'/>" class="row g-3 align-items-end">
            <input type="hidden" name="pageSize" value="<s:property value='page.pageSize'/>"/>
            <div class="col-md-2">
                <label class="form-label small text-muted">模块</label>
                <input type="text" name="query.title" class="form-control" value="<s:property value='query.title'/>"/>
            </div>
            <div class="col-md-2">
                <label class="form-label small text-muted">操作人</label>
                <input type="text" name="query.operName" class="form-control" value="<s:property value='query.operName'/>"/>
            </div>
            <div class="col-md-2">
                <label class="form-label small text-muted">状态</label>
                <select name="query.status" class="form-select">
                    <option value="">全部</option>
                    <option value="0" <s:if test='query.status == "0"'>selected</s:if>>正常</option>
                    <option value="1" <s:if test='query.status == "1"'>selected</s:if>>异常</option>
                </select>
            </div>
            <div class="col-md-3">
                <label class="form-label small text-muted">开始时间</label>
                <input type="text" name="query.beginTime" class="form-control" value="<s:property value='query.beginTime'/>" placeholder="2026-01-01"/>
            </div>
            <div class="col-md-2">
                <label class="form-label small text-muted">结束时间</label>
                <input type="text" name="query.endTime" class="form-control" value="<s:property value='query.endTime'/>"/>
            </div>
            <div class="col-auto">
                <button type="submit" class="btn btn-primary"><i class="bi bi-search"></i> 搜索</button>
                <a class="btn btn-outline-secondary" href="<s:url action='operlog_list'/>">重置</a>
            </div>
        </form>
    </div>

    <div class="app-card">
        <div class="card-title">
            <span><i class="bi bi-list-check"></i>操作日志</span>
            <span class="text-muted small">共 <s:property value="page.total"/> 条</span>
        </div>
        <s:if test="page.rows.isEmpty()">
            <div class="text-center text-muted py-4">暂无操作日志。</div>
        </s:if>
        <s:else>
            <table class="table table-modern align-middle mb-0">
                <thead>
                <tr>
                    <th style="width: 150px;">操作时间</th>
                    <th style="width: 100px;">操作人员</th>
                    <th style="width: 90px;">模块</th>
                    <th style="width: 70px;">类型</th>
                    <th style="width: 60px;">状态</th>
                    <th style="width: 140px;">请求</th>
                    <th style="width: 110px;">IP</th>
                    <th>参数 / 错误信息</th>
                </tr>
                </thead>
                <tbody>
                <s:iterator value="page.rows" var="l">
                    <tr>
                        <td class="text-muted"><s:date name="#l.operTime" format="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><s:property value="#l.operName"/></td>
                        <td><s:property value="#l.title"/></td>
                        <td><span class="badge badge-soft-primary"><s:property value="typeDesc(#l.businessType)"/></span></td>
                        <td>
                            <s:if test="#l.status != null && #l.status == 1"><span class="badge badge-soft-danger">异常</span></s:if>
                            <s:else><span class="badge badge-soft-success">正常</span></s:else>
                        </td>
                        <td class="text-muted"><s:property value="#l.requestMethod"/> <s:property value="#l.operUrl"/></td>
                        <td><s:property value="#l.operIp"/></td>
                        <td class="text-muted">
                            <s:if test="#l.status != null && #l.status == 1">
                                <span class="text-danger"><s:property value="#l.errorMsg"/></span>
                            </s:if>
                            <s:else>
                                <s:property value="#l.operParam"/>
                            </s:else>
                        </td>
                    </tr>
                </s:iterator>
                </tbody>
            </table>
        </s:else>

        <s:set var="pagerAction" value="'operlog_list'"/>
        <%@ include file="/WEB-INF/jsp/common/pager.jsp" %>
    </div>
</div>
</body>
</html>
