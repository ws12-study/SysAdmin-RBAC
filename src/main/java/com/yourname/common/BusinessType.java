package com.yourname.common;

/**
 * 操作日志业务类型（与 sys_oper_log.business_type 对应）
 *
 * @author yourname
 */
public final class BusinessType {

    /** 其它 */
    public static final int OTHER = 0;
    /** 新增 */
    public static final int INSERT = 1;
    /** 修改 */
    public static final int UPDATE = 2;
    /** 删除 */
    public static final int DELETE = 3;
    /** 授权 */
    public static final int GRANT = 4;
    /** 导出 */
    public static final int EXPORT = 5;
    /** 导入 */
    public static final int IMPORT = 6;

    private BusinessType() {
    }

    public static String desc(Integer type) {
        if (type == null) {
            return "其它";
        }
        switch (type) {
            case INSERT:
                return "新增";
            case UPDATE:
                return "修改";
            case DELETE:
                return "删除";
            case GRANT:
                return "授权";
            case EXPORT:
                return "导出";
            case IMPORT:
                return "导入";
            default:
                return "其它";
        }
    }

}
