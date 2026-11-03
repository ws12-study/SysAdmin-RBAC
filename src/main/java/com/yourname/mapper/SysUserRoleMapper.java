package com.yourname.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 用户角色关联表 Mapper
 *
 * @author yourname
 */
public interface SysUserRoleMapper {

    /**
     * 给用户授予角色
     */
    int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

}
