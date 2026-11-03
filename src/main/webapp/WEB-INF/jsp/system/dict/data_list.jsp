<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>字典数据 - <s:property value="type"/></title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
</head>
<body>
<div class="top">
    <span class="brand">字典数据</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
    <a href="<s:url action='dict_type_list'/>">返回字典</a>
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
            <span><i class="bi bi-list-ul"></i>字典数据 · <code><s:property value="type"/></code></span>
            <a class="btn btn-primary btn-sm" href="<s:url action='dict_data_input'><s:param name='type' value='type'/></s:url>"><i class="bi bi-plus-lg"></i> 新增数据</a>
        </div>
        <table class="table table-modern align-middle mb-0">
            <thead>
            <tr>
                <th style="width: 60px;">ID</th>
                <th style="width: 70px;">排序</th>
                <th>标签</th>
                <th>键值</th>
                <th style="width: 110px;">回显样式</th>
                <th style="width: 80px;">默认</th>
                <th style="width: 90px;">状态</th>
                <th style="width: 150px;">操作</th>
            </tr>
            </thead>
            <tbody>
            <s:iterator value="datas" var="d">
                <tr>
                    <td><s:property value="#d.id"/></td>
                    <td><s:property value="#d.dictSort"/></td>
                    <td><s:property value="#d.dictLabel"/></td>
                    <td><code><s:property value="#d.dictValue"/></code></td>
                    <td><s:property value="#d.listClass"/></td>
                    <td>
                        <s:if test='#d.isDefault == "Y"'><span class="badge badge-soft-success">默认</span></s:if>
                        <s:else>-</s:else>
                    </td>
                    <td>
                        <s:set var="st" value="#application.dictLabel['sys_normal_disable'][#d.status]"/>
                        <s:if test='#d.status == "1"'>
                            <span class="badge badge-soft-gray"><s:if test="#st != null"><s:property value="#st"/></s:if><s:else><s:property value="#d.status"/></s:else></span>
                        </s:if>
                        <s:else>
                            <span class="badge badge-soft-success"><s:if test="#st != null"><s:property value="#st"/></s:if><s:else><s:property value="#d.status"/></s:else></span>
                        </s:else>
                    </td>
                    <td>
                        <a class="btn-op" href="<s:url action='dict_data_input'><s:param name='id' value='#d.id'/></s:url>"><i class="bi bi-pencil-square"></i> 编辑</a>
                        <a class="btn-op danger" href="<s:url action='dict_data_delete'><s:param name='id' value='#d.id'/></s:url>"
                           onclick="return confirm('确认删除该字典数据？')"><i class="bi bi-trash"></i> 删除</a>
                    </td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
