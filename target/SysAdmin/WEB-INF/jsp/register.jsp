<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>SysAdmin 注册</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
    <style>
        body { display: flex; align-items: center; justify-content: center; }
        .box { width: 460px; padding: 36px; background: #fff; border-radius: 16px;
               box-shadow: 0 8px 30px rgba(23, 43, 77, .12); }
        .box h2 { margin: 0 0 20px; text-align: center; font-weight: 700; font-size: 20px; }
        .meter { height: 6px; background: #eee; border-radius: 3px; margin-top: 6px; overflow: hidden; }
        .meter span { display: block; height: 100%; width: 0; background: #d4380d; transition: width .2s, background .2s; }
        .rules { margin: 8px 0 0; padding-left: 16px; font-size: 12px; color: #999; line-height: 1.7; }
        .rules li.ok { color: #389e0d; }
        .tip { margin-top: 14px; font-size: 12px; color: #888; text-align: center; }
        .tip a { color: #1677ff; text-decoration: none; }
    </style>
</head>
<body>
<div class="box">
    <h2>注册新账号</h2>

    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>

    <form action="<s:url action='register'/>" method="post">
        <div class="mb-3">
            <label class="form-label small text-muted" for="userName">登录账号（2~30 字符）</label>
            <input type="text" class="form-control" id="userName" name="userName" value="<s:property value='userName'/>" autocomplete="off"/>
        </div>
        <div class="mb-3">
            <label class="form-label small text-muted" for="nickName">昵称</label>
            <input type="text" class="form-control" id="nickName" name="nickName" value="<s:property value='nickName'/>" autocomplete="off"/>
        </div>
        <div class="mb-3">
            <label class="form-label small text-muted" for="password">密码</label>
            <input type="password" class="form-control" id="password" name="password" autocomplete="new-password"/>
            <div class="meter"><span id="bar"></span></div>
            <ul class="rules">
                <li id="r1">8 ~ 20 位</li>
                <li id="r2">大写字母、小写字母、数字、特殊符号至少 3 类</li>
                <li id="r3">不含空格与中文</li>
                <li id="r4">不含连续或重复字符（aaa / 123 / abc）</li>
            </ul>
        </div>
        <div class="mb-3">
            <label class="form-label small text-muted" for="confirmPassword">确认密码</label>
            <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" autocomplete="new-password"/>
        </div>
        <div class="mb-3">
            <label class="form-label small text-muted" for="phonenumber">手机号（选填）</label>
            <input type="text" class="form-control" id="phonenumber" name="phonenumber" value="<s:property value='phonenumber'/>" autocomplete="off"/>
        </div>
        <div class="mb-3">
            <label class="form-label small text-muted" for="email">邮箱（选填）</label>
            <input type="text" class="form-control" id="email" name="email" value="<s:property value='email'/>" autocomplete="off"/>
        </div>
        <button type="submit" class="btn btn-primary w-100 py-2">注 册</button>
    </form>

    <div class="tip">已有账号？<a href="<s:url action='login_input'/>">返回登录</a></div>
</div>

<script>
    // 前端提示，仅作辅助；真正的校验在服务端 PasswordPolicy
    function hasSequence(p) {
        var repeat = 1, up = 1, down = 1;
        for (var i = 1; i < p.length; i++) {
            var d = p.charCodeAt(i) - p.charCodeAt(i - 1);
            repeat = d === 0 ? repeat + 1 : 1;
            up = d === 1 ? up + 1 : 1;
            down = d === -1 ? down + 1 : 1;
            if (repeat >= 3 || up >= 3 || down >= 3) { return true; }
        }
        return false;
    }

    function checkPwd() {
        var p = document.getElementById('password').value;
        var cats = 0;
        if (/[a-z]/.test(p)) { cats++; }
        if (/[A-Z]/.test(p)) { cats++; }
        if (/[0-9]/.test(p)) { cats++; }
        if (/[^A-Za-z0-9]/.test(p)) { cats++; }

        var ok1 = p.length >= 8 && p.length <= 20;
        var ok2 = cats >= 3;
        var ok3 = p.length > 0 && !/\s/.test(p) && /^[\x21-\x7E]+$/.test(p);
        var ok4 = p.length > 0 && !hasSequence(p);
        mark('r1', ok1); mark('r2', ok2); mark('r3', ok3); mark('r4', ok4);

        var score = [ok1, ok2, ok3, ok4].filter(Boolean).length + (p.length >= 12 ? 1 : 0);
        var bar = document.getElementById('bar');
        var color = ['#d4380d', '#d4380d', '#fa8c16', '#52c41a', '#389e0d'][Math.max(0, Math.min(4, score))];
        bar.style.width = (score / 4 * 100) + '%';
        bar.style.background = color;
    }

    function mark(id, ok) {
        var el = document.getElementById(id);
        if (ok) { el.className = 'ok'; } else { el.className = ''; }
    }

    document.getElementById('password').addEventListener('input', checkPwd);
</script>
</body>
</html>
