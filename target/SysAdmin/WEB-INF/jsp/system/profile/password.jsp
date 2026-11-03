<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>修改密码 - SysAdmin</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
    <style>
        .meter { height: 6px; background: #eee; border-radius: 3px; margin-top: 6px; overflow: hidden; }
        .meter span { display: block; height: 100%; width: 0; background: #d4380d; transition: width .2s, background .2s; }
        .rules { margin: 8px 0 0; padding-left: 16px; font-size: 12px; color: #999; line-height: 1.7; }
        .rules li.ok { color: #389e0d; }
    </style>
</head>
<body>
<div class="top">
    <span class="brand">修改密码</span>
    <%@ include file="/WEB-INF/jsp/common/nav.jsp" %>
</div>

<div class="app-container" style="max-width: 520px;">
    <s:if test="errorMsg != null">
        <div class="alert alert-danger" role="alert"><s:property value="errorMsg"/></div>
    </s:if>

    <div class="app-card">
        <div class="card-title"><span><i class="bi bi-key"></i>修改密码</span></div>
        <form action="<s:url action='password_save'/>" method="post" class="row g-3">
            <div class="col-12">
                <label class="form-label small text-muted" for="oldPassword">原密码</label>
                <input type="password" class="form-control" id="oldPassword" name="oldPassword" autocomplete="current-password"/>
            </div>
            <div class="col-12">
                <label class="form-label small text-muted" for="newPassword">新密码</label>
                <input type="password" class="form-control" id="newPassword" name="newPassword" autocomplete="new-password"/>
                <div class="meter"><span id="bar"></span></div>
                <ul class="rules">
                    <li id="r1">8 ~ 20 位</li>
                    <li id="r2">大写字母、小写字母、数字、特殊符号至少 3 类</li>
                    <li id="r3">不含空格与中文</li>
                    <li id="r4">不含连续或重复字符（aaa / 123 / abc）</li>
                    <li id="r5">与原密码不同</li>
                </ul>
            </div>
            <div class="col-12">
                <label class="form-label small text-muted" for="confirmPassword">确认新密码</label>
                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" autocomplete="new-password"/>
            </div>
            <div class="col-12">
                <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg"></i> 确认修改</button>
            </div>
        </form>
    </div>
</div>

<script>
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
        var n = document.getElementById('newPassword').value;
        var o = document.getElementById('oldPassword').value;
        var cats = 0;
        if (/[a-z]/.test(n)) { cats++; }
        if (/[A-Z]/.test(n)) { cats++; }
        if (/[0-9]/.test(n)) { cats++; }
        if (/[^A-Za-z0-9]/.test(n)) { cats++; }

        var ok1 = n.length >= 8 && n.length <= 20;
        var ok2 = cats >= 3;
        var ok3 = n.length > 0 && !/\s/.test(n) && /^[\x21-\x7E]+$/.test(n);
        var ok4 = n.length > 0 && !hasSequence(n);
        var ok5 = n.length > 0 && n !== o;
        mark('r1', ok1); mark('r2', ok2); mark('r3', ok3); mark('r4', ok4); mark('r5', ok5);

        var score = [ok1, ok2, ok3, ok4, ok5].filter(Boolean).length;
        var bar = document.getElementById('bar');
        var colors = ['#d4380d', '#d4380d', '#fa8c16', '#52c41a', '#389e0d', '#389e0d'];
        bar.style.width = (score / 5 * 100) + '%';
        bar.style.background = colors[score];
    }

    function mark(id, ok) {
        document.getElementById(id).className = ok ? 'ok' : '';
    }

    document.getElementById('newPassword').addEventListener('input', checkPwd);
    document.getElementById('oldPassword').addEventListener('input', checkPwd);
</script>
</body>
</html>
