package com.yourname.mapper;

import com.yourname.entity.SysFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文件附件 Mapper
 *
 * @author yourname
 */
public interface SysFileMapper {

    List<SysFile> selectPage(@Param("offset") int offset, @Param("limit") int limit, @Param("module") String module);

    long countPage(@Param("module") String module);

    SysFile selectById(@Param("id") Long id);

    int insert(SysFile file);

    int deleteById(@Param("id") Long id);

}
