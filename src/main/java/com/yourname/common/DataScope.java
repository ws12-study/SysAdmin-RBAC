package com.yourname.common;

/**
 * 数据权限范围常量（对应 sys_role.data_scope）
 *
 * @author yourname
 */
public final class DataScope {

    /** 全部数据 */
    public static final String ALL = "1";
    /** 自定义部门 */
    public static final String CUSTOM = "2";
    /** 本部门 */
    public static final String DEPT = "3";
    /** 本部门及以下 */
    public static final String DEPT_AND_BELOW = "4";
    /** 仅本人 */
    public static final String SELF = "5";

    private DataScope() {
    }

    public static String desc(String scope) {
        if (ALL.equals(scope)) {
            return "全部数据";
        }
        if (CUSTOM.equals(scope)) {
            return "自定义部门";
        }
        if (DEPT.equals(scope)) {
            return "本部门";
        }
        if (DEPT_AND_BELOW.equals(scope)) {
            return "本部门及以下";
        }
        if (SELF.equals(scope)) {
            return "仅本人";
        }
        return "未设置";
    }

}
