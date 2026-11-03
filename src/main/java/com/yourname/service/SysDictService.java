package com.yourname.service;

import com.yourname.entity.SysDictData;
import com.yourname.entity.SysDictType;

import java.util.List;

/**
 * 数据字典服务
 *
 * @author yourname
 */
public interface SysDictService {

    List<SysDictType> selectTypeList();

    SysDictType selectTypeById(Long id);

    void saveType(SysDictType type, String operator);

    void deleteTypeById(Long id);

    List<SysDictData> selectDataList(String dictType);

    SysDictData selectDataById(Long id);

    /**
     * 保存字典数据并刷新缓存；isDefault=Y 时同类型其它项置为 N
     */
    void saveData(SysDictData data, String operator);

    void deleteDataById(Long id);

    /**
     * 重新加载字典缓存
     */
    void refreshCache();

}
