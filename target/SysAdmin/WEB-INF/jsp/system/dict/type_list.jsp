<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>数据字典</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
</head>
<body>
<div class="top">
    <span class="brand">数据字典</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
</div>

<div class="app-container">
    <%@ include file="/WEB-INF/jsp/common/flash.jsp" %>
    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>
    <s:actionerror/>
    <s:fielderror/>

    <div class="app-card">
        <div class="card-title">
            <span><i class="bi bi-book"></i>字典列表</span>
            <span>
                <a class="btn btn-outline-secondary btn-sm" href="<s:url action='dict_refresh'/>"><i class="bi bi-arrow-clockwise"></i> 刷新缓存</a>
                <a class="btn btn-primary btn-sm ms-1" href="<s:url action='dict_type_input'/>"><i class="bi bi-plus-lg"></i> 新增字典</a>
            </span>
        </div>
        <table class="table table-modern align-middle mb-0">
            <thead>
            <tr>
                <th style="width: 60px;">ID</th>
                <th>字典名称</th>
                <th>字典类型</th>
                <th style="width: 90px;">状态</th>
                <th style="width: 220px;">操作</th>
            </tr>
            </thead>
            <tbody>
            <s:iterator value="types" var="t">
                <tr>
                    <td><s:property value="#t.id"/></td>
                    <td><s:property value="#t.dictName"/></td>
                    <td><code><s:property value="#t.dictType"/></code></td>
                    <td>
                        <s:set var="st" value="#application.dictLabel['sys_normal_disable'][#t.status]"/>
                        <s:if test='#t.status == "1"'>
                            <span class="badge badge-soft-gray"><s:if test="#st != null"><s:property value="#st"/></s:if><s:else><s:property value="#t.status"/></s:else></span>
                        </s:if>
                        <s:else>
                            <span class="badge badge-soft-success"><s:if test="#st != null"><s:property value="#st"/></s:if><s:else><s:property value="#t.status"/></s:else></span>
                        </s:else>
                    </td>
                    <td>
                        <a class="btn-op" href="<s:url action='dict_data_list'><s:param name='type' value='#t.dictType'/></s:url>"><i class="bi bi-list-ul"></i> 字典数据</a>
                        <a class="btn-op" href="<s:url action='dict_type_input'><s:param name='id' value='#t.id'/></s:url>"><i class="bi bi-pencil-square"></i> 编辑</a>
                        <a class="btn-op danger" href="<s:url action='dict_type_delete'><s:param name='id' value='#t.id'/></s:url>"
                           onclick="return confirm('确认删除该字典？')"><i class="bi bi-trash"></i> 删除</a>
                    </td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
