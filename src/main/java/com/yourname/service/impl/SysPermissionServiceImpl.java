package com.yourname.service.impl;

import com.yourname.common.SessionKeys;
import com.yourname.entity.SysMenu;
import com.yourname.entity.SysRole;
import com.yourname.mapper.SysMenuMapper;
import com.yourname.mapper.SysRoleMapper;
import com.yourname.service.SysPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 权限服务实现：进程内按用户缓存，授权变更时精确失效。
 * <p>
 * 相比把权限塞进 session，这样改完角色授权后立即生效，无需用户重新登录。
 *
 * @author yourname
 */
@Service("sysPermissionServiceImpl")
public class SysPermissionServiceImpl implements SysPermissionService {

    private static final Logger log = LoggerFactory.getLogger(SysPermissionServiceImpl.class);

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    private final Map<Long, Snapshot> cache = new ConcurrentHashMap<Long, Snapshot>();

    @Override
    public Set<String> getPerms(Long userId) {
        return snapshot(userId).perms;
    }

    @Override
    public List<SysRole> getRoles(Long userId) {
        return snapshot(userId).roles;
    }

    @Override
    public List<SysMenu> getMenus(Long userId) {
        return snapshot(userId).menus;
    }

    @Override
    public boolean isAdmin(Long userId) {
        return snapshot(userId).admin;
    }

    @Override
    public void evict(Long userId) {
        if (userId == null) {
            return;
        }
        cache.remove(userId);
    }

    @Override
    public void evictByRole(Long roleId) {
        if (roleId == null) {
            return;
        }
        List<Long> userIds = roleMapper.selectUserIdsByRoleId(roleId);
        if (userIds == null) {
            return;
        }
        for (Long userId : userIds) {
            cache.remove(userId);
        }
        if (!userIds.isEmpty()) {
            log.info("角色 {} 权限变更，已失效 {} 个用户的权限缓存", roleId, userIds.size());
        }
    }

    @Override
    public void evictAll() {
        cache.clear();
    }

    private Snapshot snapshot(Long userId) {
        if (userId == null) {
            return Snapshot.EMPTY;
        }
        Snapshot cached = cache.get(userId);
        if (cached != null) {
            return cached;
        }
        Snapshot loaded = load(userId);
        cache.put(userId, loaded);
        return loaded;
    }

    private Snapshot load(Long userId) {
        List<SysRole> roles = roleMapper.selectRolesByUserId(userId);
        if (roles == null) {
            roles = Collections.emptyList();
        }
        boolean admin = false;
        for (SysRole role : roles) {
            if (SessionKeys.ROLE_KEY_ADMIN.equals(role.getRoleKey())) {
                admin = true;
                break;
            }
        }
        List<String> perms = menuMapper.selectPermsByUserId(userId);
        Set<String> permSet = perms == null ? Collections.<String>emptySet()
                : Collections.unmodifiableSet(new HashSet<String>(perms));
        List<SysMenu> menus = menuMapper.selectMenusByUserId(userId);
        if (menus == null) {
            menus = Collections.emptyList();
        }
        return new Snapshot(roles, permSet, menus, admin);
    }

    /**
     * 用户权限快照（不可变）
     */
    private static final class Snapshot {

        private static final Snapshot EMPTY =
                new Snapshot(Collections.<SysRole>emptyList(), Collections.<String>emptySet(),
                        Collections.<SysMenu>emptyList(), false);

        private final List<SysRole> roles;
        private final Set<String> perms;
        private final List<SysMenu> menus;
        private final boolean admin;

        private Snapshot(List<SysRole> roles, Set<String> perms, List<SysMenu> menus, boolean admin) {
            this.roles = new ArrayList<SysRole>(roles);
            this.perms = perms;
            this.menus = menus;
            this.admin = admin;
        }
    }

}
