package com.yourname.mapper;

import com.yourname.entity.SysDictType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典类型 Mapper
 *
 * @author yourname
 */
public interface SysDictTypeMapper {

    List<SysDictType> selectAllTypes();

    SysDictType selectTypeById(@Param("id") Long id);

    SysDictType selectTypeByType(@Param("dictType") String dictType);

    int insertType(SysDictType type);

    int updateType(SysDictType type);

    /**
     * 逻辑删除（同时停用，避免缓存继续命中）
     */
    int deleteTypeById(@Param("id") Long id);

}
