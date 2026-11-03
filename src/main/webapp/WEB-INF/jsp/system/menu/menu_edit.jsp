<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>菜单编辑</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
    <script>
        function toggleType() {
            var t = document.getElementById('menuType').value;
            setVisible('row-path', t === 'C');
            setVisible('row-perms', t === 'C' || t === 'F');
            setVisible('row-icon', t === 'M');
        }
        function setVisible(id, show) {
            var el = document.getElementById(id);
            if (el) { el.classList.toggle('d-none', !show); }
        }
        window.onload = function () { toggleType(); };
    </script>
</head>
<body>
<div class="top">
    <span class="brand">菜单编辑</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
    <a href="<s:url action='menu_list'/>">返回列表</a>
</div>

<div class="app-container" style="max-width: 760px;">
    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>

    <div class="app-card">
        <div class="card-title"><span><i class="bi bi-pencil-square"></i>菜单信息</span></div>
        <form action="<s:url action='menu_save'/>" method="post" class="row g-3">
            <s:hidden name="menu.id"/>
            <div class="col-md-6">
                <label class="form-label small text-muted">上级菜单</label>
                <select name="menu.parentId" class="form-select">
                    <option value="0">主目录</option>
                    <s:iterator value="parentOptions" var="p">
                        <option value="<s:property value='#p.id'/>"
                            <s:if test="menu.parentId == #p.id">selected</s:if>>
                            <s:property value="#p.menuName"/>
                        </option>
                    </s:iterator>
                </select>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">菜单类型</label>
                <select name="menu.menuType" id="menuType" class="form-select" onchange="toggleType()">
                    <option value="M" <s:if test='menu.menuType == "M"'>selected</s:if>>目录</option>
                    <option value="C" <s:if test='menu.menuType == "C"'>selected</s:if>>菜单</option>
                    <option value="F" <s:if test='menu.menuType == "F"'>selected</s:if>>按钮</option>
                </select>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">菜单名称</label>
                <input type="text" class="form-control" name="menu.menuName" value="<s:property value='menu.menuName'/>"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">显示顺序</label>
                <input type="text" class="form-control" name="menu.orderNum" value="<s:property value='menu.orderNum'/>"/>
            </div>
            <div class="col-md-6" id="row-path">
                <label class="form-label small text-muted">路由地址</label>
                <input type="text" class="form-control" name="menu.path" value="<s:property value='menu.path'/>"
                       placeholder="如 user_list（对应 Action 名）"/>
            </div>
            <div class="col-md-6" id="row-perms">
                <label class="form-label small text-muted">权限标识</label>
                <input type="text" class="form-control" name="menu.perms" value="<s:property value='menu.perms'/>"
                       placeholder="如 system:user:edit"/>
            </div>
            <div class="col-md-6" id="row-icon">
                <label class="form-label small text-muted">图标</label>
                <input type="text" class="form-control" name="menu.icon" value="<s:property value='menu.icon'/>" placeholder="预留字段"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">显示状态</label>
                <select name="menu.visible" class="form-select">
                    <option value="0" <s:if test='menu.visible == "0"'>selected</s:if>>显示</option>
                    <option value="1" <s:if test='menu.visible == "1"'>selected</s:if>>隐藏</option>
                </select>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">菜单状态</label>
                <select name="menu.status" class="form-select">
                    <option value="0" <s:if test='menu.status == "0"'>selected</s:if>>正常</option>
                    <option value="1" <s:if test='menu.status == "1"'>selected</s:if>>停用</option>
                </select>
            </div>
            <div class="col-12">
                <label class="form-label small text-muted">备注</label>
                <textarea class="form-control" name="menu.remark" rows="2"><s:property value="menu.remark"/></textarea>
            </div>
            <div class="col-12">
                <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg"></i> 保存</button>
                <a class="btn btn-outline-secondary ms-2" href="<s:url action='menu_list'/>">取消</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
