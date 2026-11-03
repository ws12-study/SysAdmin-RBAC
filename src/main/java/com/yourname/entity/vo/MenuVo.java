package com.yourname.entity.vo;

import com.yourname.entity.SysMenu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单树节点（页面导航渲染用）
 *
 * @author yourname
 */
public class MenuVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private final SysMenu menu;
    private final List<MenuVo> children = new ArrayList<MenuVo>();

    public MenuVo(SysMenu menu) {
        this.menu = menu;
    }

    public SysMenu getMenu() {
        return menu;
    }

    public List<MenuVo> getChildren() {
        return children;
    }

    public void addChild(MenuVo child) {
        children.add(child);
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

}
