package com.yourname.mapper;

import com.yourname.entity.SysDictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典数据 Mapper
 *
 * @author yourname
 */
public interface SysDictDataMapper {

    /**
     * 全部启用状态的字典数据（缓存加载用，按类型与排序）
     */
    List<SysDictData> selectAllData();

    List<SysDictData> selectDataByType(@Param("dictType") String dictType);

    SysDictData selectDataById(@Param("id") Long id);

    int insertData(SysDictData data);

    int updateData(SysDictData data);

    /**
     * 逻辑删除（同时停用）
     */
    int deleteDataById(@Param("id") Long id);

    /**
     * 清空某类型下的默认值标记（保证唯一默认值）
     */
    int clearDefaultByType(@Param("dictType") String dictType);

}
