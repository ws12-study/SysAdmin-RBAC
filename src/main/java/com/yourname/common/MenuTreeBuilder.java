package com.yourname.common;

import com.yourname.entity.SysMenu;
import com.yourname.entity.vo.MenuVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 把扁平菜单列表整理成树，供页面导航渲染
 *
 * @author yourname
 */
public final class MenuTreeBuilder {

    private MenuTreeBuilder() {
    }

    /**
     * @param menus 用户可见菜单（已由 SQL 按角色过滤）
     * @return 根节点列表，按 order_num 排序，层级不限
     */
    public static List<MenuVo> build(List<SysMenu> menus) {
        if (menus == null || menus.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, MenuVo> nodes = new LinkedHashMap<Long, MenuVo>();
        for (SysMenu menu : menus) {
            if (menu == null || menu.getId() == null) {
                continue;
            }
            nodes.put(menu.getId(), new MenuVo(menu));
        }

        List<MenuVo> roots = new ArrayList<MenuVo>();
        for (MenuVo node : nodes.values()) {
            Long parentId = node.getMenu().getParentId();
            MenuVo parent = parentId == null || parentId.longValue() == 0L ? null : nodes.get(parentId);
            if (parent != null && parent != node) {
                parent.addChild(node);
            } else {
                roots.add(node);
            }
        }
        sort(roots);
        return roots;
    }

    private static void sort(List<MenuVo> nodes) {
        Collections.sort(nodes, new Comparator<MenuVo>() {
            @Override
            public int compare(MenuVo a, MenuVo b) {
                int byOrder = compareNullSafe(a.getMenu().getOrderNum(), b.getMenu().getOrderNum());
                return byOrder != 0 ? byOrder : compareNullSafe(a.getMenu().getId(), b.getMenu().getId());
            }
        });
        for (MenuVo node : nodes) {
            sort(node.getChildren());
        }
    }

    private static <T extends Comparable<? super T>> int compareNullSafe(T a, T b) {
        if (a == null && b == null) {
            return 0;
        }
        if (a == null) {
            return 1;
        }
        if (b == null) {
            return -1;
        }
        return a.compareTo(b);
    }

}
