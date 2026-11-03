package com.yourname.service;

import com.yourname.entity.SysUser;

/**
 * 数据权限（data_scope）解析
 *
 * @author yourname
 */
public interface DataScopeService {

    /**
     * 生成用户数据范围的 SQL 过滤片段（作用于别名 u 的 sys_user 表）
     *
     * @return null 表示不做限制（全部数据）；否则返回如 {@code u.dept_id IN (101,103) OR u.id = 2}
     */
    String buildUserScopeSql(SysUser user);

    /**
     * 数据范围的中文描述（页面提示用）
     */
    String describe(SysUser user);

}
