package com.yourname.service.impl;

import com.yourname.common.ServiceException;
import com.yourname.entity.SysMenu;
import com.yourname.mapper.SysMenuMapper;
import com.yourname.mapper.SysRoleMapper;
import com.yourname.service.SysMenuService;
import com.yourname.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单服务实现
 *
 * @author yourname
 */
@Service("sysMenuServiceImpl")
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public List<SysMenu> selectMenuTree() {
        List<SysMenu> all = menuMapper.selectMenuList();
        if (all == null) {
            return Collections.emptyList();
        }
        Map<Long, List<SysMenu>> byParent = new HashMap<Long, List<SysMenu>>();
        for (SysMenu menu : all) {
            Long parentId = menu.getParentId() == null ? 0L : menu.getParentId();
            List<SysMenu> children = byParent.get(parentId);
            if (children == null) {
                children = new ArrayList<SysMenu>();
                byParent.put(parentId, children);
            }
            children.add(menu);
        }
        List<SysMenu> result = new ArrayList<SysMenu>();
        append(0L, byParent, result, 0);
        return result;
    }

    private void append(Long parentId, Map<Long, List<SysMenu>> byParent, List<SysMenu> result, int depth) {
        List<SysMenu> children = byParent.get(parentId);
        if (children == null) {
            return;
        }
        for (SysMenu menu : children) {
            menu.setDepth(depth);
            result.add(menu);
            append(menu.getId(), byParent, result, depth + 1);
        }
    }

    @Override
    public List<SysMenu> selectParentOptions() {
        List<SysMenu> all = menuMapper.selectMenuList();
        if (all == null) {
            return Collections.emptyList();
        }
        List<SysMenu> result = new ArrayList<SysMenu>();
        for (SysMenu menu : all) {
            if ("M".equals(menu.getMenuType()) || "C".equals(menu.getMenuType())) {
                result.add(menu);
            }
        }
        return result;
    }

    @Override
    public SysMenu selectMenuById(Long id) {
        return id == null ? null : menuMapper.selectMenuById(id);
    }

    @Override
    public void saveMenu(SysMenu menu, String operator) {
        if (menu == null) {
            throw new ServiceException("菜单信息不能为空");
        }
        if (!StringUtils.hasText(menu.getMenuName())) {
            throw new ServiceException("菜单名称不能为空");
        }
        if (menu.getMenuType() == null) {
            menu.setMenuType("C");
        }
        if (menu.getParentId() == null) {
            menu.setParentId(0L);
        }
        if (menu.getOrderNum() == null) {
            menu.setOrderNum(0);
        }
        if (menu.getVisible() == null) {
            menu.setVisible("0");
        }
        if (menu.getStatus() == null) {
            menu.setStatus("0");
        }
        // 按钮必须有权限标识
        if ("F".equals(menu.getMenuType()) && !StringUtils.hasText(menu.getPerms())) {
            throw new ServiceException("按钮类型必须填写权限标识");
        }
        // 目录不允许有路由地址
        if ("M".equals(menu.getMenuType())) {
            menu.setPath(null);
            menu.setPerms(null);
        }

        if (menu.getId() == null) {
            menu.setCreateBy(operator);
            menuMapper.insertMenu(menu);
        } else {
            if (menuMapper.selectMenuById(menu.getId()) == null) {
                throw new ServiceException("菜单不存在");
            }
            // 父菜单不能是自身，避免环
            if (menu.getId().equals(menu.getParentId())) {
                throw new ServiceException("上级菜单不能是自身");
            }
            menu.setUpdateBy(operator);
            menuMapper.updateMenu(menu);
        }
        // 菜单结构/权限变化，清空权限缓存让所有用户下次请求重新加载
        permissionService.evictAll();
    }

    @Override
    public void deleteMenuById(Long id) {
        if (id == null) {
            throw new ServiceException("菜单ID不能为空");
        }
        if (menuMapper.selectMenuById(id) == null) {
            throw new ServiceException("菜单不存在");
        }
        if (menuMapper.countChildren(id) > 0) {
            throw new ServiceException("存在子菜单，请先删除子菜单");
        }
        // 清理该菜单的角色授权关联，再物理删除
        roleMapper.deleteRoleMenuByMenuId(id);
        menuMapper.deleteMenuById(id);
        permissionService.evictAll();
    }

}
