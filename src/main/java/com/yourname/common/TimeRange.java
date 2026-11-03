package com.yourname.common;

import java.util.regex.Pattern;

/**
 * 查询条件里的时间范围处理（入参为字符串，避免各页面日期格式不一致）
 *
 * @author yourname
 */
public final class TimeRange {

    private static final Pattern DATE = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
    private static final Pattern DATETIME = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");

    private TimeRange() {
    }

    /**
     * 起始时间：日期补齐为 00:00:00
     *
     * @return 规范化后的字符串；空返回 null
     * @throws ServiceException 格式不合法
     */
    public static String begin(String value) {
        String v = trim(value);
        if (v == null) {
            return null;
        }
        if (DATETIME.matcher(v).matches()) {
            return v;
        }
        if (DATE.matcher(v).matches()) {
            return v + " 00:00:00";
        }
        throw new ServiceException("开始时间格式不正确，应为 yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 结束时间：只给日期时补齐为 23:59:59，保证查得到当天
     *
     * @return 规范化后的字符串；空返回 null
     * @throws ServiceException 格式不合法
     */
    public static String end(String value) {
        String v = trim(value);
        if (v == null) {
            return null;
        }
        if (DATETIME.matcher(v).matches()) {
            return v;
        }
        if (DATE.matcher(v).matches()) {
            return v + " 23:59:59";
        }
        throw new ServiceException("结束时间格式不正确，应为 yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss");
    }

    /** 空串按未填写处理 */
    public static String trim(String value) {
        if (value == null) {
            return null;
        }
        String v = value.trim();
        return v.isEmpty() ? null : v;
    }

}
