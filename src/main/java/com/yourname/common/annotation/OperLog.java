package com.yourname.common.annotation;

import com.yourname.common.BusinessType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记需要记录操作日志的 Action 方法（由 OperLogInterceptor 落库）
 *
 * @author yourname
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperLog {

    /** 模块标题，如"用户管理" */
    String title();

    /** 业务类型，见 {@link BusinessType} */
    int type() default BusinessType.OTHER;

    /** 是否记录请求参数（涉及敏感字段时会自动脱敏，仍可整体关闭） */
    boolean saveParams() default true;

}
