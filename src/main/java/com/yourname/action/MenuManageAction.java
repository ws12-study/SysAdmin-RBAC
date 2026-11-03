package com.yourname.action;

import com.yourname.common.BusinessType;
import com.yourname.common.ServiceException;
import com.yourname.common.annotation.OperLog;
import com.yourname.common.annotation.RequiresPermissions;
import com.yourname.entity.SysMenu;
import com.yourname.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 菜单管理
 *
 * @author yourname
 */
public class MenuManageAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private SysMenuService menuService;

    private List<SysMenu> menus;
    private SysMenu menu;
    private List<SysMenu> parentOptions;
    private Long id;
    private String errorMsg;

    @RequiresPermissions("system:menu:list")
    public String list() {
        menus = menuService.selectMenuTree();
        return SUCCESS;
    }

    @RequiresPermissions("system:menu:list")
    public String input() {
        try {
            parentOptions = menuService.selectParentOptions();
            if (id != null) {
                menu = menuService.selectMenuById(id);
                if (menu == null) {
                    errorMsg = "菜单不存在";
                    return INPUT;
                }
            } else {
                menu = new SysMenu();
                menu.setParentId(0L);
                menu.setMenuType("C");
                menu.setOrderNum(0);
                menu.setVisible("0");
                menu.setStatus("0");
            }
            return SUCCESS;
        } catch (ServiceException e) {
            errorMsg = e.getMessage();
            return INPUT;
        }
    }

    @RequiresPermissions("system:menu:list")
    @OperLog(title = "菜单管理", type = BusinessType.UPDATE)
    public String save() {
        try {
            menuService.saveMenu(menu, currentUserName());
            flashOk("菜单保存成功");
            return SUCCESS;
        } catch (ServiceException e) {
            errorMsg = e.getMessage();
            parentOptions = menuService.selectParentOptions();
            return INPUT;
        }
    }

    @RequiresPermissions("system:menu:list")
    @OperLog(title = "菜单管理", type = BusinessType.DELETE)
    public String delete() {
        try {
            menuService.deleteMenuById(id);
            flashOk("菜单已删除");
            return SUCCESS;
        } catch (ServiceException e) {
            flashErr(e.getMessage());
            return INPUT;
        }
    }

    public List<SysMenu> getMenus() {
        return menus;
    }

    public SysMenu getMenu() {
        return menu;
    }

    public void setMenu(SysMenu menu) {
        this.menu = menu;
    }

    public List<SysMenu> getParentOptions() {
        return parentOptions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
