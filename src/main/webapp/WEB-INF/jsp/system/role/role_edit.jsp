<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>角色编辑与授权</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
    <style>
        .tree { border: 1px solid #eef1f5; border-radius: 8px; padding: 14px; max-height: 320px; overflow: auto; background: #fafbfd; }
        .tree .form-check { margin-bottom: 2px; }
        .lv2 { margin-left: 22px; }
        .lv3 { margin-left: 44px; }
        .tag { font-size: 11px; color: #999; margin-left: 6px; }
    </style>
</head>
<body>
<div class="top">
    <span class="brand">角色编辑与授权</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
    <a href="<s:url action='role_list'/>">返回列表</a>
</div>

<div class="app-container" style="max-width: 860px;">
    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>
    <s:actionerror/>
    <s:fielderror/>

    <div class="app-card">
        <div class="card-title"><span><i class="bi bi-pencil-square"></i>角色信息</span></div>
        <s:form action="role_save" namespace="/" theme="simple" cssClass="row g-3">
            <s:hidden name="role.id" value="%{role.id}"/>

            <div class="col-md-6">
                <label class="form-label small text-muted">角色名称</label>
                <s:textfield name="role.roleName" cssClass="form-control"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">权限字符（如 admin / common）</label>
                <s:textfield name="role.roleKey" cssClass="form-control"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">显示顺序</label>
                <s:textfield name="role.roleSort" cssClass="form-control"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">数据范围</label>
                <s:select name="role.dataScope" cssClass="form-select"
                          list="#{'1':'全部数据','2':'自定义','3':'本部门','4':'本部门及以下','5':'仅本人'}"/>
            </div>
            <div class="col-md-6">
                <label class="form-label small text-muted">状态</label>
                <s:select name="role.status" cssClass="form-select" list="#{'0':'正常','1':'停用'}"/>
            </div>

            <div class="col-12">
                <label class="form-label small text-muted">菜单权限</label>
                <div class="tree">
                    <s:iterator value="menuList" var="m">
                        <s:if test="#m.parentId == 0">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="checkedMenuIds" value="<s:property value='#m.id'/>"
                                       <s:if test="checkedMenuIds.contains(#m.id)">checked="checked"</s:if>/>
                                <label class="form-check-label"><s:property value="#m.menuName"/></label>
                            </div>
                            <s:iterator value="menuList" var="c">
                                <s:if test="#c.parentId == #m.id">
                                    <div class="form-check lv2">
                                        <input class="form-check-input" type="checkbox" name="checkedMenuIds" value="<s:property value='#c.id'/>"
                                               <s:if test="checkedMenuIds.contains(#c.id)">checked="checked"</s:if>/>
                                        <label class="form-check-label"><s:property value="#c.menuName"/></label>
                                    </div>
                                    <s:iterator value="menuList" var="g">
                                        <s:if test="#g.parentId == #c.id">
                                            <div class="form-check lv3">
                                                <input class="form-check-input" type="checkbox" name="checkedMenuIds" value="<s:property value='#g.id'/>"
                                                       <s:if test="checkedMenuIds.contains(#g.id)">checked="checked"</s:if>/>
                                                <label class="form-check-label">
                                                    <s:property value="#g.menuName"/>
                                                    <span class="tag"><s:property value="#g.perms"/></span>
                                                </label>
                                            </div>
                                        </s:if>
                                    </s:iterator>
                                </s:if>
                            </s:iterator>
                        </s:if>
                    </s:iterator>
                </div>
            </div>

            <div class="col-12">
                <label class="form-label small text-muted">自定义数据权限部门（仅「自定义」范围生效）</label>
                <div class="tree">
                    <s:iterator value="deptList" var="d">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" name="checkedDeptIds" value="<s:property value='#d.id'/>"
                                   <s:if test="checkedDeptIds.contains(#d.id)">checked="checked"</s:if>/>
                            <label class="form-check-label">
                                <s:property value="#d.deptName"/>
                                <span class="tag">id=<s:property value="#d.id"/></span>
                            </label>
                        </div>
                    </s:iterator>
                </div>
            </div>

            <div class="col-12">
                <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg"></i> 保存</button>
                <a class="btn btn-outline-secondary ms-2" href="<s:url action='role_list'/>">取消</a>
            </div>
        </s:form>
    </div>
</div>
</body>
</html>
