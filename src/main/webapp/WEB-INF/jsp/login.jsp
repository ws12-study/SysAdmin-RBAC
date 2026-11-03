<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>SysAdmin 登录</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
    <style>
        body {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background:
                radial-gradient(1000px 520px at 12% -6%, rgba(71, 118, 230, .35), transparent 60%),
                radial-gradient(880px 500px at 100% 6%, rgba(142, 84, 233, .30), transparent 55%),
                linear-gradient(160deg, #16324f 0%, #1d4b6e 50%, #17314c 100%);
        }
        .box { width: 400px; padding: 42px 38px; background: rgba(255, 255, 255, .98);
               border-radius: 16px; box-shadow: 0 18px 50px rgba(5, 18, 34, .45); }
        .box .logo { text-align: center; margin-bottom: 22px; }
        .box .logo b { font-size: 28px; color: #14385c; letter-spacing: 1px; }
        .box .logo span { display: block; font-size: 12px; color: #93a5b8; margin-top: 6px; }
        .box .icon { width: 60px; height: 60px; margin: 0 auto 12px; border-radius: 16px;
            background: linear-gradient(135deg, #4776E6 0%, #8E54E9 100%);
            display: flex; align-items: center; justify-content: center; font-size: 28px; color: #fff; }
        .tip { margin-top: 16px; font-size: 12px; color: #93a5b8; text-align: center; }
        .tip a { color: #1677ff; text-decoration: none; }
    </style>
</head>
<body>
<div class="box">
    <div class="logo">
        <div class="icon"><i class="bi bi-shield-lock-fill"></i></div>
        <b>SysAdmin</b>
        <span>通用后台 · 用户权限与数据字典系统</span>
    </div>

    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>
    <s:if test='msg == "changed"'>
        <div class="alert alert-success" role="alert">密码已修改，请使用新密码重新登录</div>
    </s:if>

    <form action="<s:url action='login'/>" method="post">
        <div class="mb-3">
            <label class="form-label small text-muted" for="userName">登录账号</label>
            <input type="text" class="form-control" id="userName" name="userName" autocomplete="off" placeholder="请输入账号"/>
        </div>
        <div class="mb-3">
            <label class="form-label small text-muted" for="password">登录密码</label>
            <input type="password" class="form-control" id="password" name="password" autocomplete="off" placeholder="请输入密码"/>
        </div>
        <button type="submit" class="btn btn-primary w-100 py-2">登 录</button>
    </form>

    <div class="tip">
        初始账号：admin / admin123（见 sql/sysadmin_init_data.sql）<br/>
        <a href="<s:url action='register_input'/>">注册新账号</a>
    </div>
</div>
</body>
</html>
