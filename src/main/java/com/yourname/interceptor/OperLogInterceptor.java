package com.yourname.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.yourname.common.BusinessType;
import com.yourname.common.SessionKeys;
import com.yourname.common.annotation.OperLog;
import com.yourname.entity.SysOperLog;
import com.yourname.entity.SysUser;
import com.yourname.service.SysOperLogService;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * 操作日志拦截器：为带 {@link OperLog} 注解的 Action 方法落一条审计日志
 * <p>
 * 挂在 authStack 中 auth 之后：只记录已登录用户的操作；写库失败不影响业务。
 * 请求参数中的密码类字段会被脱敏。
 *
 * @author yourname
 */
public class OperLogInterceptor extends com.opensymphony.xwork2.interceptor.AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    /** 这些字段的参数值不落库 */
    private static final String[] SENSITIVE_KEYS = {"password", "pwd", "passwd", "token", "secret"};

    @Autowired
    private SysOperLogService operLogService;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        OperLog annotation = resolveAnnotation(invocation);
        if (annotation == null) {
            return invocation.invoke();
        }

        HttpServletRequest request = ServletActionContext.getRequest();
        Object loginUser = invocation.getInvocationContext().getSession().get(SessionKeys.LOGIN_USER);
        String operName = loginUser instanceof SysUser ? ((SysUser) loginUser).getUserName() : "";

        SysOperLog log = new SysOperLog();
        log.setTitle(annotation.title());
        log.setBusinessType(annotation.type());
        log.setOperatorType(1);
        log.setMethod(invocation.getAction().getClass().getName() + "." + methodName(invocation) + "()");
        log.setRequestMethod(request == null ? null : request.getMethod());
        log.setOperUrl(request == null ? null : request.getRequestURI());
        log.setOperIp(request == null ? "" : getRemoteIp(request));
        log.setOperName(operName);
        log.setOperLocation("");
        log.setOperParam(annotation.saveParams() ? params(request) : null);
        log.setOperTime(new Date());
        log.setStatus(0);
        log.setCreateBy(operName);

        String result = null;
        try {
            result = invocation.invoke();
            return result;
        } catch (Exception e) {
            log.setStatus(1);
            log.setErrorMsg(clip(String.valueOf(e.getMessage()), 1900));
            throw e;
        } finally {
            // 业务校验失败时 Action 不抛异常，而是返回 input（如"原密码不正确"），这里补记为异常
            if (log.getStatus() == null || log.getStatus().intValue() == 0) {
                if ("input".equals(result) && request != null && "POST".equalsIgnoreCase(request.getMethod())) {
                    Object errorMsg = invocation.getStack().findValue("errorMsg");
                    if (errorMsg != null) {
                        log.setStatus(1);
                        log.setErrorMsg(clip(String.valueOf(errorMsg), 1900));
                    }
                }
            }
            log.setJsonResult(result == null ? null : clip(result, 1900));
            operLogService.record(log);
        }
    }

    private OperLog resolveAnnotation(ActionInvocation invocation) {
        String name = methodName(invocation);
        try {
            Method method = invocation.getAction().getClass().getMethod(name);
            return method.getAnnotation(OperLog.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private String methodName(ActionInvocation invocation) {
        String name = invocation.getProxy().getMethod();
        return name == null || name.length() == 0 ? "execute" : name;
    }

    /** 请求参数脱敏后拼成 a=1, b=2 形式 */
    private String params(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Map<String, String[]> map = request.getParameterMap();
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sorted = new TreeMap<String, String>();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            if (values == null || values.length == 0) {
                sorted.put(key, "");
            } else if (values.length == 1) {
                sorted.put(key, isSensitive(key) ? "******" : values[0]);
            } else {
                sorted.put(key, isSensitive(key) ? "******" : String.join(",", values));
            }
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return clip(sb.toString(), 1900);
    }

    private boolean isSensitive(String key) {
        String lower = key == null ? "" : key.toLowerCase();
        for (String sensitive : SENSITIVE_KEYS) {
            if (lower.contains(sensitive)) {
                return true;
            }
        }
        return false;
    }

    private String getRemoteIp(HttpServletRequest request) {
        String xip = request.getHeader("X-Forwarded-For");
        if (xip != null && xip.length() > 0) {
            return xip.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    private String clip(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }

}
