package com.yourname.service.impl;

import com.yourname.common.DataScope;
import com.yourname.entity.SysRole;
import com.yourname.entity.SysUser;
import com.yourname.mapper.SysDeptMapper;
import com.yourname.mapper.SysRoleMapper;
import com.yourname.service.DataScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据权限解析实现：按用户角色的 data_scope 生成查询过滤条件。
 * 片段由服务端依据库内 Long 型 ID 拼装，不含外部输入。
 *
 * @author yourname
 */
@Service("dataScopeServiceImpl")
public class DataScopeServiceImpl implements DataScopeService {

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysDeptMapper deptMapper;

    @Override
    public String buildUserScopeSql(SysUser user) {
        List<SysRole> roles = activeRoles(user);
        if (user == null || user.getId() == null) {
            return "1 = 0";
        }
        if (roles.isEmpty()) {
            // 无角色：只能看到自己
            return "u.id = " + user.getId();
        }
        for (SysRole role : roles) {
            if (DataScope.ALL.equals(role.getDataScope())) {
                return null;
            }
        }

        Set<Long> deptIds = new LinkedHashSet<Long>();
        boolean selfOnly = false;
        for (SysRole role : roles) {
            String scope = role.getDataScope();
            if (DataScope.CUSTOM.equals(scope)) {
                deptIds.addAll(nonNull(roleMapper.selectDeptIdsByRoleId(role.getId())));
            } else if (DataScope.DEPT.equals(scope)) {
                if (user.getDeptId() != null) {
                    deptIds.add(user.getDeptId());
                }
            } else if (DataScope.DEPT_AND_BELOW.equals(scope)) {
                if (user.getDeptId() != null) {
                    deptIds.addAll(nonNull(deptMapper.selectDeptIdsByAncestor(user.getDeptId())));
                }
            } else if (DataScope.SELF.equals(scope)) {
                selfOnly = true;
            }
        }

        StringBuilder sql = new StringBuilder();
        if (!deptIds.isEmpty()) {
            sql.append("u.dept_id IN (").append(join(deptIds)).append(")");
        }
        if (selfOnly) {
            if (sql.length() > 0) {
                sql.append(" OR ");
            }
            sql.append("u.id = ").append(user.getId());
        }
        return sql.length() == 0 ? "1 = 0" : sql.toString();
    }

    @Override
    public String describe(SysUser user) {
        List<SysRole> roles = activeRoles(user);
        if (roles.isEmpty()) {
            return "无角色（仅本人）";
        }
        List<String> descs = new ArrayList<String>();
        for (SysRole role : roles) {
            String desc = DataScope.desc(role.getDataScope());
            if (!descs.contains(desc)) {
                descs.add(desc);
            }
        }
        return String.join("、", descs);
    }

    private List<SysRole> activeRoles(SysUser user) {
        List<SysRole> result = new ArrayList<SysRole>();
        if (user == null || user.getId() == null) {
            return result;
        }
        List<SysRole> roles = roleMapper.selectRolesByUserId(user.getId());
        if (roles == null) {
            return result;
        }
        for (SysRole role : roles) {
            // 停用角色不参与数据范围计算
            if (role != null && !"1".equals(role.getStatus())) {
                result.add(role);
            }
        }
        return result;
    }

    private List<Long> nonNull(List<Long> list) {
        return list == null ? new ArrayList<Long>() : list;
    }

    /**
     * 仅拼接 Long 型 ID，规避 SQL 注入
     */
    private String join(Set<Long> ids) {
        StringBuilder sb = new StringBuilder();
        for (Long id : ids) {
            if (id == null) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(id.longValue());
        }
        return sb.toString();
    }

}
