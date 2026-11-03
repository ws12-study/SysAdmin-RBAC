package com.yourname.mapper;

import com.yourname.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 用户表 Mapper
 *
 * @author yourname
 */
public interface SysUserMapper {

    /**
     * 根据登录账号查询用户
     */
    SysUser selectByUserName(@Param("userName") String userName);

    /**
     * 更新最后登录 IP 与时间
     */
    int updateLoginInfo(@Param("id") Long id,
                        @Param("loginIp") String loginIp,
                        @Param("loginDate") Date loginDate);

    /**
     * 统计用户总数（排除已删除）
     */
    long countUser();

    /**
     * 新增用户（回写自增主键到 id）
     */
    int insertUser(SysUser user);

    /**
     * 用户列表（含部门名、角色名）
     *
     * @param dataScope 数据权限过滤片段（服务端生成，null 表示不限制）
     * @param offset    起始行，null 表示不分页
     * @param limit     每页条数，null 表示不分页
     * @param userName  账号 / 昵称关键字，空表示不筛选
     * @param status    状态（0正常 1停用），空表示不筛选
     */
    java.util.List<com.yourname.entity.vo.SysUserVo> selectUserList(@Param("dataScope") String dataScope,
                                                                    @Param("offset") Integer offset,
                                                                    @Param("limit") Integer limit,
                                                                    @Param("userName") String userName,
                                                                    @Param("status") String status);

    /**
     * 用户列表总条数（与 selectUserList 同一套过滤条件）
     */
    long selectUserCount(@Param("dataScope") String dataScope,
                         @Param("userName") String userName,
                         @Param("status") String status);

    /**
     * 按主键查询用户
     */
    SysUser selectUserById(@Param("id") Long id);

    /**
     * 判断指定用户是否落在数据范围内（配合 ${dataScope} 片段），用于 ID 级越权校验
     */
    long countUserByIdInScope(@Param("id") Long id, @Param("dataScope") String dataScope);

    /**
     * 更新用户状态（0正常 1停用）
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 更新密码（新密码为 PBKDF2 哈希串）
     */
    int updatePassword(@Param("id") Long id, @Param("password") String password);

}
