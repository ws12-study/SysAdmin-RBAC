package com.yourname.service.impl;

import com.yourname.common.ServiceException;
import com.yourname.entity.SysDept;
import com.yourname.mapper.SysDeptMapper;
import com.yourname.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门服务实现
 *
 * @author yourname
 */
@Service("sysDeptServiceImpl")
public class SysDeptServiceImpl implements SysDeptService {

    @Autowired
    private SysDeptMapper deptMapper;

    @Override
    public List<SysDept> selectDeptTree() {
        List<SysDept> all = selectAllDepts();
        Map<Long, List<SysDept>> byParent = new HashMap<Long, List<SysDept>>();
        for (SysDept dept : all) {
            Long parentId = dept.getParentId() == null ? 0L : dept.getParentId();
            List<SysDept> children = byParent.get(parentId);
            if (children == null) {
                children = new ArrayList<SysDept>();
                byParent.put(parentId, children);
            }
            children.add(dept);
        }
        List<SysDept> result = new ArrayList<SysDept>();
        appendChildren(0L, byParent, result, 0);
        return result;
    }

    private void appendChildren(Long parentId, Map<Long, List<SysDept>> byParent,
                                List<SysDept> result, int depth) {
        List<SysDept> children = byParent.get(parentId);
        if (children == null) {
            return;
        }
        for (SysDept dept : children) {
            dept.setDepth(depth);
            result.add(dept);
            appendChildren(dept.getId(), byParent, result, depth + 1);
        }
    }

    @Override
    public List<SysDept> selectAllDepts() {
        List<SysDept> depts = deptMapper.selectAllDepts();
        return depts == null ? Collections.<SysDept>emptyList() : depts;
    }

    @Override
    public SysDept selectDeptById(Long id) {
        return id == null ? null : deptMapper.selectDeptById(id);
    }

    @Override
    public void saveDept(SysDept dept, String operator) {
        if (dept == null) {
            throw new ServiceException("部门信息不能为空");
        }
        if (!StringUtils.hasText(dept.getDeptName())) {
            throw new ServiceException("部门名称不能为空");
        }
        if (dept.getParentId() == null) {
            dept.setParentId(0L);
        }
        if (dept.getOrderNum() == null) {
            dept.setOrderNum(0);
        }
        if (dept.getStatus() == null) {
            dept.setStatus("0");
        }

        // 计算新的祖级路径
        String newAncestors = resolveAncestors(dept.getParentId());
        dept.setAncestors(newAncestors);

        Long id = dept.getId();
        boolean isNew = (id == null);
        if (isNew) {
            dept.setCreateBy(operator);
            deptMapper.insertDept(dept);
        } else {
            SysDept old = deptMapper.selectDeptById(id);
            if (old == null) {
                throw new ServiceException("部门不存在");
            }
            // 父部门不能是自身或其下级，避免形成环
            if (id.equals(dept.getParentId())) {
                throw new ServiceException("父部门不能是自身");
            }
            List<Long> descendants = deptMapper.selectDeptIdsByAncestor(id);
            if (descendants != null && descendants.contains(dept.getParentId())) {
                throw new ServiceException("父部门不能选择自己的下级部门");
            }
            boolean parentChanged = !old.getParentId().equals(dept.getParentId());
            dept.setUpdateBy(operator);
            deptMapper.updateDept(dept);
            // 移动了层级：级联刷新所有下级的祖级路径
            if (parentChanged) {
                refreshChildrenAncestors(dept);
            }
        }
    }

    private String resolveAncestors(Long parentId) {
        if (parentId == null || parentId.longValue() == 0L) {
            return "0";
        }
        SysDept parent = deptMapper.selectDeptById(parentId);
        if (parent == null) {
            throw new ServiceException("父部门不存在");
        }
        return parent.getAncestors() + "," + parentId;
    }

    /** 递归刷新所有下级部门的祖级路径与父级引用 */
    private void refreshChildrenAncestors(SysDept dept) {
        List<Long> childIds = deptMapper.selectDirectChildren(dept.getId());
        if (childIds == null) {
            return;
        }
        String ancestorsPrefix = dept.getAncestors() + "," + dept.getId();
        for (Long childId : childIds) {
            deptMapper.updateAncestors(childId, ancestorsPrefix, dept.getId());
            SysDept child = deptMapper.selectDeptById(childId);
            if (child != null) {
                child.setAncestors(ancestorsPrefix);
                refreshChildrenAncestors(child);
            }
        }
    }

    @Override
    public void deleteDeptById(Long id) {
        if (id == null) {
            throw new ServiceException("部门ID不能为空");
        }
        if (deptMapper.selectDeptById(id) == null) {
            throw new ServiceException("部门不存在");
        }
        List<Long> directChildren = deptMapper.selectDirectChildren(id);
        if (directChildren != null && !directChildren.isEmpty()) {
            throw new ServiceException("存在下级部门，请先删除下级部门");
        }
        List<Long> deptIds = deptMapper.selectDeptIdsByAncestor(id);
        if (deptMapper.countUsersByDeptIds(deptIds) > 0) {
            throw new ServiceException("部门下存在用户，请先调整用户的所属部门");
        }
        deptMapper.deleteDeptById(id);
    }

}
