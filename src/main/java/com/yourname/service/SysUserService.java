package com.yourname.service;

import com.yourname.entity.SysLoginLog;
import com.yourname.entity.SysUser;

import java.util.List;

/**
 * 用户 Service
 *
 * @author yourname
 */
public interface SysUserService {

    /**
     * 分页查询用户（数据范围过滤同 selectUserList）
     *
     * @param pageNum  页码，从 1 开始
     * @param pageSize 每页条数
     * @param userName 账号 / 昵称关键字，空表示不筛选
     * @param status   状态（0正常 1停用），空表示不筛选
     */
    com.yourname.common.PageResult<com.yourname.entity.vo.SysUserVo> selectUserPage(
            com.yourname.entity.SysUser currentUser, int pageNum, int pageSize,
            String userName, String status);

    /**
     * 停用 / 启用用户
     *
     * @param userId      目标用户
     * @param status      0正常 1停用
     * @param currentUser 操作人，用于数据范围校验，且不允许停用自己的账号
     */
    void changeStatus(Long userId, String status, com.yourname.entity.SysUser currentUser);

    /**
     * 登录校验
     *
     * @return 校验通过的当前用户
     * @throws com.yourname.common.ServiceException 账号不存在 / 密码错误 / 账号已停用
     */
    SysUser login(String userName, String password);

    /**
     * 记录一次登录结果，并回写用户最后登录信息
     */
    void recordLoginInfo(SysUser user, String ip, boolean success, String msg, String browser, String os);

    /**
     * 注册新用户：校验账号唯一性、密码加密后落库，并授予默认角色
     *
     * @param user        待注册用户（userName / password / nickName / phonenumber / email）
     * @param rawPassword 明文密码
     * @return 注册成功后的用户（含自增 id）
     */
    SysUser register(SysUser user, String rawPassword);

    /**
     * 修改密码：校验原密码、校验新密码强度，通过后更新为 PBKDF2 哈希
     *
     * @param userId          用户 ID
     * @param oldRawPassword  原密码明文
     * @param newRawPassword  新密码明文
     */
    void changePassword(Long userId, String oldRawPassword, String newRawPassword);

    /**
     * 用户总数
     */
    long countUser();

    /**
     * 最近登录记录
     */
    List<SysLoginLog> latestLoginLogs(String loginName, int limit);

    /**
     * 登录日志总数
     */
    long countLoginLog();

    /**
     * 指定账号的登录次数（首页普通用户统计用）
     */
    long countLoginLogByUser(String loginName);

    /**
     * 最近的登录日志（全部账号，登录日志页用）
     */
    List<SysLoginLog> latestLoginLogsOfAll(int limit);

    /**
     * 分页查询登录日志
     *
     * @param query 筛选条件（账号 / 状态 / 时间范围），null 表示不筛选
     */
    com.yourname.common.PageResult<SysLoginLog> loginLogPage(int pageNum, int pageSize,
                                                             com.yourname.entity.query.LogQuery query);

    /**
     * 查询用户拥有的角色列表
     */
    java.util.List<com.yourname.entity.SysRole> getUserRoles(Long userId);

    /**
     * 是否超级管理员（拥有 role_key = admin 的角色）
     */
    boolean isAdmin(Long userId);

    /**
     * 查询用户拥有的权限标识（sys_menu.perms）
     */
    java.util.Set<String> getUserPerms(Long userId);

    /**
     * 用户列表（按当前登录用户的数据权限过滤）
     *
     * @param currentUser 当前登录用户，为空时拒绝查询（不允许绕过数据范围）
     */
    java.util.List<com.yourname.entity.vo.SysUserVo> selectUserList(com.yourname.entity.SysUser currentUser);

    /**
     * 按主键查询用户（不做数据范围校验，仅供内部流程使用）
     */
    com.yourname.entity.SysUser selectById(Long id);

    /**
     * 按主键查询用户，并校验其是否落在当前登录用户的数据范围内
     *
     * @return 在范围内返回用户；不存在或越权返回 null（调用方统一提示，避免信息泄露）
     */
    com.yourname.entity.SysUser selectByIdInScope(Long id, com.yourname.entity.SysUser currentUser);

    /**
     * 查询全部角色
     */
    java.util.List<com.yourname.entity.SysRole> selectAllRoles();

    /**
     * 查询用户已拥有的角色 ID
     */
    java.util.List<Long> selectRoleIdsByUserId(Long userId);

    /**
     * 重新分配用户角色（先清空再批量授予，事务内执行）
     *
     * @param roleIds     为空表示撤销全部角色
     * @param currentUser 操作人，用于数据范围校验；被操作用户不在范围内则拒绝
     */
    void assignRoles(Long userId, java.util.List<Long> roleIds,
                     com.yourname.entity.SysUser currentUser);

}
