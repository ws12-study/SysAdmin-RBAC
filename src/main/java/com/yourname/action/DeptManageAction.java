package com.yourname.action;

import com.yourname.common.BusinessType;
import com.yourname.common.ServiceException;
import com.yourname.common.annotation.OperLog;
import com.yourname.common.annotation.RequiresPermissions;
import com.yourname.entity.SysDept;
import com.yourname.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 部门管理
 *
 * @author yourname
 */
public class DeptManageAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private SysDeptService deptService;

    /** 树形列表（DFS 平铺 + 缩进层级） */
    private List<SysDept> depts;
    /** 编辑对象 */
    private SysDept dept;
    /** 父部门下拉 */
    private List<SysDept> allDepts;
    private Long id;
    private String errorMsg;

    @RequiresPermissions("system:dept:list")
    public String list() {
        depts = deptService.selectDeptTree();
        return SUCCESS;
    }

    @RequiresPermissions("system:dept:list")
    public String input() {
        try {
            allDepts = deptService.selectAllDepts();
            if (id != null) {
                dept = deptService.selectDeptById(id);
                if (dept == null) {
                    errorMsg = "部门不存在";
                    return INPUT;
                }
            } else {
                dept = new SysDept();
                dept.setParentId(0L);
                dept.setOrderNum(0);
                dept.setStatus("0");
            }
            return SUCCESS;
        } catch (ServiceException e) {
            errorMsg = e.getMessage();
            return INPUT;
        }
    }

    @RequiresPermissions("system:dept:list")
    @OperLog(title = "部门管理", type = BusinessType.UPDATE)
    public String save() {
        try {
            deptService.saveDept(dept, currentUserName());
            flashOk("部门保存成功");
            return SUCCESS;
        } catch (ServiceException e) {
            // 失败回编辑页并加载下拉
            errorMsg = e.getMessage();
            allDepts = deptService.selectAllDepts();
            return INPUT;
        }
    }

    @RequiresPermissions("system:dept:list")
    @OperLog(title = "部门管理", type = BusinessType.DELETE)
    public String delete() {
        try {
            deptService.deleteDeptById(id);
            flashOk("部门已删除");
            return SUCCESS;
        } catch (ServiceException e) {
            flashErr(e.getMessage());
            return INPUT;
        }
    }

    public List<SysDept> getDepts() {
        return depts;
    }

    public SysDept getDept() {
        return dept;
    }

    public void setDept(SysDept dept) {
        this.dept = dept;
    }

    public List<SysDept> getAllDepts() {
        return allDepts;
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
