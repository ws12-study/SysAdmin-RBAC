<%-- 顶栏导航：菜单按当前用户角色动态渲染（menuTree 由 BaseAction 提供）
     被静态包含时必须自行声明编码，否则 Tomcat 按 ISO-8859-1 读取，中文会乱码 --%>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style>
    /* ---------- 顶部导航栏：深色高级渐变，悬浮阴影 ---------- */
    .top {
        display: flex;
        align-items: center;
        height: 64px;
        line-height: normal;
        padding: 0 28px;
        background: linear-gradient(to right, #141E30 0%, #243B55 100%);
        color: #fff;
        box-shadow: 0 4px 18px rgba(20, 30, 48, .35);
        position: sticky;
        top: 0;
        z-index: 100;
        font-size: 15px;
        font-weight: 600;
    }
    .top .brand { font-size: 16px; font-weight: 700; letter-spacing: .5px;
                  color: #fff; white-space: nowrap; }
    .top .brand::before { content: "◆"; color: #4fc3f7; margin-right: 8px; }
    .top .topnav { margin-left: auto; display: flex; align-items: center; gap: 22px; }
    .top .topnav a { float: none !important; margin: 0 !important; padding: 7px 2px;
        color: rgba(255,255,255,.85); text-decoration: none; font-size: 14px; font-weight: 500;
        border-bottom: 2px solid transparent; transition: color .18s, border-color .18s; }
    .top .topnav a:hover { color: #fff; border-bottom-color: #4fc3f7; }
    .top .topnav .divider { width: 1px; height: 18px; background: rgba(255,255,255,.24); }
    .top .topnav .op { color: rgba(255,255,255,.62); }
    .top .topnav .op:hover { color: #fff; border-bottom-color: transparent; }
    .top .topnav .who { color: rgba(255,255,255,.9); font-size: 13px; font-weight: 400; }
    /* 编辑页"返回列表"等裸链接（.top 直接子元素） */
    .top > a { margin-left: 18px; color: rgba(255,255,255,.7); text-decoration: none; font-size: 13px; font-weight: 400; }
    .top > a:hover { color: #fff; }
</style>
<nav class="topnav">
    <a href="<s:url action='index'/>">首页</a>
    <s:iterator value="menuTree" var="p">
        <s:if test="#p.hasChildren()">
            <s:iterator value="#p.children" var="c">
                <%-- 目录(M)只作容器，没有可访问的 Action，不渲染链接 --%>
                <s:if test='#c.menu.menuType != "M"'>
                    <a href="<s:url action='%{#c.menu.path}'/>"><s:property value="#c.menu.menuName"/></a>
                </s:if>
            </s:iterator>
        </s:if>
        <s:elseif test='#p.menu.menuType != "M"'>
            <a href="<s:url action='%{#p.menu.path}'/>"><s:property value="#p.menu.menuName"/></a>
        </s:elseif>
    </s:iterator>
    <span class="divider"></span>
    <span class="who"><s:property value="currentUser.nickName"/></span>
    <a class="op" href="<s:url action='password_input'/>">修改密码</a>
    <a class="op" href="<s:url action='logout'/>">退出登录</a>
</nav>
