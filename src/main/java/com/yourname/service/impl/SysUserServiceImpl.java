package com.yourname.service.impl;

import com.yourname.common.PageResult;
import com.yourname.common.PasswordHasher;
import com.yourname.common.PasswordPolicy;
import com.yourname.common.ServiceException;
import com.yourname.common.SessionKeys;
import com.yourname.common.TimeRange;
import com.yourname.entity.SysLoginLog;
import com.yourname.entity.query.LogQuery;
import com.yourname.entity.SysRole;
import com.yourname.entity.SysUser;
import com.yourname.entity.vo.SysUserVo;
import com.yourname.mapper.SysLoginLogMapper;
import com.yourname.mapper.SysMenuMapper;
import com.yourname.mapper.SysRoleMapper;
import com.yourname.mapper.SysUserMapper;
import com.yourname.mapper.SysUserRoleMapper;
import com.yourname.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 用户 Service 实现
 *
 * @author yourname
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysLoginLogMapper loginLogMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private com.yourname.service.DataScopeService dataScopeService;

    @Autowired
    private com.yourname.service.SysPermissionService permissionService;

    /** 连续失败达到该次数即锁定账号 */
    private static final int MAX_LOGIN_FAIL = 5;
    /** 锁定窗口（分钟）：窗口内的失败才计数，超过窗口自动解锁 */
    private static final int LOGIN_LOCK_MINUTES = 15;

    /** 注册用户默认归属部门：总公司 */
    private static final Long DEFAULT_DEPT_ID = 100L;
    /** 注册用户默认角色：普通角色 */
    private static final Long DEFAULT_ROLE_ID = 2L;

    /** 系统保留账号：不允许通过注册页面占用 */
    private static final Set<String> RESERVED_USER_NAMES =
            new HashSet<>(Arrays.asList("admin", "administrator", "root", "system", "sysadmin"));

    @Override
    public SysUser login(String userName, String password) {
        if (!StringUtils.hasText(userName) || !StringUtils.hasText(password)) {
            throw new ServiceException("用户名和密码不能为空");
        }
        String loginName = userName.trim();
        // 失败次数超限直接拒绝，不再校验密码（防暴力破解）
        long failures = loginLogMapper.countRecentFailures(loginName, LOGIN_LOCK_MINUTES);
        if (failures >= MAX_LOGIN_FAIL) {
            throw new ServiceException("连续登录失败 " + MAX_LOGIN_FAIL + " 次，账号已锁定，请 "
                    + LOGIN_LOCK_MINUTES + " 分钟后重试");
        }

        SysUser user = userMapper.selectByUserName(loginName);
        if (user == null) {
            throw new ServiceException("账号不存在");
        }
        if (!PasswordHasher.matches(password, user.getPassword())) {
            int remain = (int) (MAX_LOGIN_FAIL - failures - 1);
            throw new ServiceException(remain > 0
                    ? "密码错误，还可尝试 " + remain + " 次（超过将锁定 " + LOGIN_LOCK_MINUTES + " 分钟）"
                    : "密码错误，账号即将锁定");
        }
        if ("1".equals(user.getStatus())) {
            throw new ServiceException("账号已停用，请联系管理员");
        }
        // 历史无盐 MD5 口令：校验通过后就地升级为 PBKDF2，对用户无感知
        if (PasswordHasher.isLegacy(user.getPassword())) {
            userMapper.updatePassword(user.getId(), PasswordHasher.encode(password));
        }
        // 密码哈希不进 session
        user.setPassword(null);
        return user;
    }

    @Override
    public void recordLoginInfo(SysUser user, String ip, boolean success, String msg, String browser, String os) {
        Date now = new Date();

        if (user != null && user.getId() != null && success) {
            userMapper.updateLoginInfo(user.getId(), ip, now);
        }

        SysLoginLog log = new SysLoginLog();
        log.setLoginName(user == null ? "" : user.getUserName());
        log.setIpAddress(ip);
        log.setLoginLocation("本地 Lan");
        log.setBrowser(browser);
        log.setOs(os);
        log.setStatus(success ? "0" : "1");
        log.setMsg(msg);
        log.setLoginTime(now);
        log.setCreateBy(user == null ? "guest" : user.getUserName());
        loginLogMapper.insertLoginLog(log);
    }

    @Override
    public SysUser register(SysUser user, String rawPassword) {
        if (user == null || !StringUtils.hasText(user.getUserName())) {
            throw new ServiceException("登录账号不能为空");
        }
        String userName = user.getUserName().trim();
        if (userName.length() < 2 || userName.length() > 30) {
            throw new ServiceException("登录账号长度必须为 2 ~ 30 个字符");
        }
        String policyError = com.yourname.common.PasswordPolicy.validate(rawPassword, userName);
        if (policyError != null) {
            throw new ServiceException(policyError);
        }
        if (RESERVED_USER_NAMES.contains(userName.toLowerCase(Locale.ENGLISH))) {
            throw new ServiceException("该账号为系统保留账号，无法注册");
        }
        if (userMapper.selectByUserName(userName) != null) {
            throw new ServiceException("该账号已被注册");
        }

        user.setUserName(userName);
        user.setPassword(PasswordHasher.encode(rawPassword));
        user.setDeptId(user.getDeptId() == null ? DEFAULT_DEPT_ID : user.getDeptId());
        user.setNickName(StringUtils.hasText(user.getNickName()) ? user.getNickName() : userName);
        user.setUserType("0");
        user.setStatus("0");
        user.setDelFlag("0");
        user.setCreateBy(userName);
        user.setUpdateBy(userName);

        userMapper.insertUser(user);
        // 授予默认角色，否则新账号登录后无任何菜单权限
        userRoleMapper.insertUserRole(user.getId(), DEFAULT_ROLE_ID);

        // 避免把密码敏感信息继续向上传递
        user.setPassword(null);
        return user;
    }

    @Override
    public void changeStatus(Long userId, String status, SysUser currentUser) {
        if (userId == null) {
            throw new ServiceException("用户ID不能为空");
        }
        if (!"0".equals(status) && !"1".equals(status)) {
            throw new ServiceException("状态值不合法");
        }
        // 数据范围校验：不能操作范围外的用户
        SysUser target = selectByIdInScope(userId, currentUser);
        if (target == null) {
            throw new ServiceException("用户不存在或不在你的数据范围内");
        }
        if (currentUser != null && userId.equals(currentUser.getId())) {
            throw new ServiceException("不能停用/启用自己的账号");
        }
        if (status.equals(target.getStatus())) {
            throw new ServiceException("1".equals(status) ? "该用户已是停用状态" : "该用户已是正常状态");
        }
        userMapper.updateStatus(userId, status,
                currentUser == null ? "system" : currentUser.getUserName());
    }

    @Override
    public void changePassword(Long userId, String oldRawPassword, String newRawPassword) {
        if (userId == null) {
            throw new ServiceException("用户ID不能为空");
        }
        if (!StringUtils.hasText(oldRawPassword)) {
            throw new ServiceException("请输入原密码");
        }
        SysUser user = userMapper.selectUserById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        if (!PasswordHasher.matches(oldRawPassword, user.getPassword())) {
            throw new ServiceException("原密码不正确");
        }
        String policyError = PasswordPolicy.validate(newRawPassword, user.getUserName());
        if (policyError != null) {
            throw new ServiceException(policyError);
        }
        if (newRawPassword.equals(oldRawPassword)) {
            throw new ServiceException("新密码不能与原密码相同");
        }
        userMapper.updatePassword(userId, PasswordHasher.encode(newRawPassword));
    }

    @Override
    public long countUser() {
        return userMapper.countUser();
    }

    @Override
    public List<SysLoginLog> latestLoginLogs(String loginName, int limit) {
        return loginLogMapper.selectLatestByLoginName(loginName, limit);
    }

    @Override
    public long countLoginLog() {
        return loginLogMapper.countLoginLog();
    }

    @Override
    public long countLoginLogByUser(String loginName) {
        return loginLogMapper.countByLoginName(loginName);
    }

    @Override
    public List<SysLoginLog> latestLoginLogsOfAll(int limit) {
        return loginLogMapper.selectLatest(limit > 0 ? limit : 100);
    }

    @Override
    public PageResult<SysLoginLog> loginLogPage(int pageNum, int pageSize, LogQuery query) {
        int num = PageResult.fixPageNum(pageNum);
        int size = PageResult.fixPageSize(pageSize);
        LogQuery q = query == null ? new LogQuery() : query;
        // 只给日期时补齐时分秒，保证"当天"查得到
        q.setBeginTime(TimeRange.begin(q.getBeginTime()));
        q.setEndTime(TimeRange.end(q.getEndTime()));
        List<SysLoginLog> rows = loginLogMapper.selectByPage(PageResult.offset(num, size), size, q);
        return PageResult.of(rows, loginLogMapper.countByPage(q), num, size);
    }

    @Override
    public List<SysRole> getUserRoles(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        List<SysRole> roles = roleMapper.selectRolesByUserId(userId);
        return roles == null ? Collections.emptyList() : roles;
    }

    @Override
    public Set<String> getUserPerms(Long userId) {
        if (userId == null) {
            return Collections.emptySet();
        }
        List<String> perms = menuMapper.selectPermsByUserId(userId);
        return perms == null ? Collections.emptySet() : new HashSet<>(perms);
    }

    @Override
    public List<SysUserVo> selectUserList(SysUser currentUser) {
        if (currentUser == null) {
            // 不允许用 null 绕过数据范围
            throw new ServiceException("未获取到登录用户，拒绝查询用户列表");
        }
        String dataScope = dataScopeService.buildUserScopeSql(currentUser);
        List<SysUserVo> list = userMapper.selectUserList(dataScope, null, null, null, null);
        return list == null ? Collections.<SysUserVo>emptyList() : list;
    }

    @Override
    public PageResult<SysUserVo> selectUserPage(SysUser currentUser, int pageNum, int pageSize,
                                                String userName, String status) {
        if (currentUser == null) {
            // 不允许用 null 绕过数据范围
            throw new ServiceException("未获取到登录用户，拒绝查询用户列表");
        }
        int num = PageResult.fixPageNum(pageNum);
        int size = PageResult.fixPageSize(pageSize);
        String keyword = TimeRange.trim(userName);
        String statusValue = TimeRange.trim(status);
        String dataScope = dataScopeService.buildUserScopeSql(currentUser);
        List<SysUserVo> rows = userMapper.selectUserList(dataScope, PageResult.offset(num, size), size,
                keyword, statusValue);
        long total = userMapper.selectUserCount(dataScope, keyword, statusValue);
        return PageResult.of(rows, total, num, size);
    }

    @Override
    public SysUser selectById(Long id) {
        return id == null ? null : userMapper.selectUserById(id);
    }

    @Override
    public SysUser selectByIdInScope(Long id, SysUser currentUser) {
        if (id == null) {
            return null;
        }
        String dataScope = dataScopeService.buildUserScopeSql(currentUser);
        if (userMapper.countUserByIdInScope(id, dataScope) == 0) {
            return null;
        }
        return userMapper.selectUserById(id);
    }

    @Override
    public List<SysRole> selectAllRoles() {
        return roleMapper.selectAllRoles();
    }

    @Override
    public List<Long> selectRoleIdsByUserId(Long userId) {
        return roleMapper.selectRoleIdsByUserId(userId);
    }

    @Override
    public void assignRoles(Long userId, List<Long> roleIds, SysUser currentUser) {
        if (userId == null) {
            throw new ServiceException("用户ID不能为空");
        }
        // 越权防护：不在数据范围内的用户不允许被操作
        if (selectByIdInScope(userId, currentUser) == null) {
            throw new ServiceException("用户不存在或不在你的数据范围内");
        }
        roleMapper.deleteUserRolesByUserId(userId);
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                if (roleId != null) {
                    userRoleMapper.insertUserRole(userId, roleId);
                }
            }
        }
        // 角色变更后立即失效权限缓存，无需用户重新登录
        permissionService.evict(userId);
    }

    @Override
    public boolean isAdmin(Long userId) {
        for (SysRole role : getUserRoles(userId)) {
            if (SessionKeys.ROLE_KEY_ADMIN.equals(role.getRoleKey())) {
                return true;
            }
        }
        return false;
    }

}
