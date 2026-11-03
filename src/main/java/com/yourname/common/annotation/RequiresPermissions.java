package com.yourname.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法级权限标识：与 sys_menu.perms 对应
 * <p>
 * 标注在 Action 方法上，由 {@code AuthInterceptor} 在服务端强制校验（页面隐藏不算权限控制）。
 *
 * @author yourname
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermissions {

    /** 所需权限标识，如 system:user:list */
    String value();

}
