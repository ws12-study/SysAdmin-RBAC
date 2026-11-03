package com.yourname.mapper;

import com.yourname.entity.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门表 Mapper
 *
 * @author yourname
 */
public interface SysDeptMapper {

    /**
     * 全部未删除部门（按显示顺序）
     */
    List<SysDept> selectAllDepts();

    /**
     * 指定部门及其全部下级部门 ID（依赖 ancestors 祖级路径）
     */
    List<Long> selectDeptIdsByAncestor(@Param("deptId") Long deptId);

    /**
     * 按主键查询单个部门
     */
    SysDept selectDeptById(@Param("id") Long id);

    /**
     * 直接子部门 ID
     */
    List<Long> selectDirectChildren(@Param("parentId") Long parentId);

    /**
     * 新增部门
     */
    int insertDept(SysDept dept);

    /**
     * 更新部门（全字段）
     */
    int updateDept(SysDept dept);

    /**
     * 更新祖级路径与父部门（移动部门时级联刷新下级用）
     */
    int updateAncestors(@Param("id") Long id, @Param("ancestors") String ancestors, @Param("parentId") Long parentId);

    /**
     * 逻辑删除部门
     */
    int deleteDeptById(@Param("id") Long id);

    /**
     * 统计指定部门集合下的用户数（删除保护）
     */
    int countUsersByDeptIds(@Param("deptIds") List<Long> deptIds);

}
