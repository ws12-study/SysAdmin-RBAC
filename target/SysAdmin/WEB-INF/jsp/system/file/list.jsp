<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>文件管理</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
</head>
<body>
<div class="top">
    <span class="brand">文件管理</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
</div>

<div class="app-container">
    <%@ include file="/WEB-INF/jsp/common/flash.jsp" %>
    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>

    <div class="app-card">
        <div class="card-title"><span><i class="bi bi-cloud-arrow-up"></i>上传文件</span></div>
        <form action="<s:url action='file_upload'/>" method="post" enctype="multipart/form-data" class="row g-3 align-items-end">
            <div class="col-md-4">
                <label class="form-label small text-muted">选择文件</label>
                <input type="file" name="file" class="form-control"/>
            </div>
            <div class="col-md-3">
                <label class="form-label small text-muted">所属模块</label>
                <input type="text" name="module" class="form-control" value="<s:property value='module'/>" placeholder="如 common"/>
            </div>
            <div class="col-auto">
                <button type="submit" class="btn btn-primary"><i class="bi bi-upload"></i> 上传</button>
                <span class="text-muted small ms-2">最大 50MB</span>
            </div>
        </form>
    </div>

    <div class="app-card">
        <div class="card-title"><span><i class="bi bi-funnel"></i>筛选条件</span></div>
        <form method="get" action="<s:url action='file_list'/>" class="row g-3 align-items-end">
            <input type="hidden" name="pageSize" value="<s:property value='page.pageSize'/>"/>
            <div class="col-md-3">
                <label class="form-label small text-muted">模块</label>
                <input type="text" name="module" class="form-control" value="<s:property value='module'/>"/>
            </div>
            <div class="col-auto">
                <button type="submit" class="btn btn-primary"><i class="bi bi-search"></i> 搜索</button>
                <a class="btn btn-outline-secondary" href="<s:url action='file_list'/>">重置</a>
            </div>
        </form>
    </div>

    <div class="app-card">
        <div class="card-title"><span><i class="bi bi-folder2-open"></i>文件列表</span></div>
        <table class="table table-modern align-middle mb-0">
            <thead>
            <tr>
                <th style="width: 60px;">ID</th>
                <th>文件名</th>
                <th style="width: 100px;">大小</th>
                <th style="width: 90px;">模块</th>
                <th style="width: 100px;">上传者</th>
                <th style="width: 160px;">上传时间</th>
                <th style="width: 130px;">操作</th>
            </tr>
            </thead>
            <tbody>
            <s:iterator value="page.rows" var="f">
                <tr>
                    <td><s:property value="#f.id"/></td>
                    <td><s:property value="#f.originalName"/></td>
                    <td>
                        <s:if test="#f.fileSize >= 1048576"><s:property value="#f.fileSize / 1048576"/> MB</s:if>
                        <s:else><s:property value="#f.fileSize / 1024"/> KB</s:else>
                    </td>
                    <td><span class="badge badge-soft-gray"><s:property value="#f.module"/></span></td>
                    <td><s:property value="#f.createBy"/></td>
                    <td class="text-muted"><s:date name="#f.createTime" format="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>
                        <a class="btn-op" href="<s:url action='file_download'><s:param name='id' value='#f.id'/></s:url>"><i class="bi bi-download"></i> 下载</a>
                        <a class="btn-op danger" href="<s:url action='file_delete'><s:param name='id' value='#f.id'/></s:url>"
                           onclick="return confirm('确认删除该文件？');"><i class="bi bi-trash"></i> 删除</a>
                    </td>
                </tr>
            </s:iterator>
            <s:if test="page == null || page.rows.isEmpty()">
                <tr><td colspan="7" class="text-center text-muted py-4">暂无文件</td></tr>
            </s:if>
            </tbody>
        </table>

        <s:set var="pagerAction" value="'file_list'"/>
        <%@ include file="/WEB-INF/jsp/common/pager.jsp" %>
    </div>
</div>
</body>
</html>
