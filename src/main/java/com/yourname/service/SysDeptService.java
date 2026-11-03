package com.yourname.service;

import com.yourname.entity.SysDept;

import java.util.List;

/**
 * 部门 Service
 *
 * @author yourname
 */
public interface SysDeptService {

    /**
     * 部门树（深度优先平铺，带缩进层级 depth，供列表页渲染）
     */
    List<SysDept> selectDeptTree();

    /**
     * 全部未删除部门（父部门下拉等场景）
     */
    List<SysDept> selectAllDepts();

    /**
     * 按主键查询
     */
    SysDept selectDeptById(Long id);

    /**
     * 新增 / 修改部门（自动维护 ancestors 祖级路径，移动部门时级联刷新下级）
     */
    void saveDept(SysDept dept, String operator);

    /**
     * 逻辑删除部门（存在下级部门或部门下仍有用户时拒绝）
     */
    void deleteDeptById(Long id);

}
