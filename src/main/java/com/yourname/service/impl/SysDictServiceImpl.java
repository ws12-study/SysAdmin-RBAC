package com.yourname.service.impl;

import com.yourname.common.DictCache;
import com.yourname.common.ServiceException;
import com.yourname.entity.SysDictData;
import com.yourname.entity.SysDictType;
import com.yourname.mapper.SysDictDataMapper;
import com.yourname.mapper.SysDictTypeMapper;
import com.yourname.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * 数据字典服务实现
 *
 * @author yourname
 */
@Service("sysDictServiceImpl")
public class SysDictServiceImpl implements SysDictService {

    @Autowired
    private SysDictTypeMapper dictTypeMapper;

    @Autowired
    private SysDictDataMapper dictDataMapper;

    @Autowired
    private DictCache dictCache;

    @Override
    public List<SysDictType> selectTypeList() {
        List<SysDictType> list = dictTypeMapper.selectAllTypes();
        return list == null ? Collections.<SysDictType>emptyList() : list;
    }

    @Override
    public SysDictType selectTypeById(Long id) {
        return id == null ? null : dictTypeMapper.selectTypeById(id);
    }

    @Override
    public void saveType(SysDictType type, String operator) {
        if (type == null) {
            throw new ServiceException("字典信息不能为空");
        }
        if (!StringUtils.hasText(type.getDictName()) || !StringUtils.hasText(type.getDictType())) {
            throw new ServiceException("字典名称与字典类型不能为空");
        }
        type.setCreateBy(operator);

        SysDictType exists = dictTypeMapper.selectTypeByType(type.getDictType());
        if (exists != null && (type.getId() == null || !exists.getId().equals(type.getId()))) {
            throw new ServiceException("字典类型已存在：" + type.getDictType());
        }

        if (type.getId() == null) {
            type.setStatus(type.getStatus() == null ? "0" : type.getStatus());
            dictTypeMapper.insertType(type);
        } else {
            if (dictTypeMapper.selectTypeById(type.getId()) == null) {
                throw new ServiceException("字典不存在");
            }
            dictTypeMapper.updateType(type);
        }
        dictCache.refresh();
    }

    @Override
    public void deleteTypeById(Long id) {
        if (id == null) {
            throw new ServiceException("字典ID不能为空");
        }
        SysDictType type = dictTypeMapper.selectTypeById(id);
        if (type == null) {
            throw new ServiceException("字典不存在");
        }
        // 类型停用后其下数据一并停用，避免脏数据继续被缓存命中
        dictTypeMapper.deleteTypeById(id);
        dictDataMapper.clearDefaultByType(type.getDictType());
        dictCache.refresh();
    }

    @Override
    public List<SysDictData> selectDataList(String dictType) {
        if (!StringUtils.hasText(dictType)) {
            return Collections.emptyList();
        }
        List<SysDictData> list = dictDataMapper.selectDataByType(dictType);
        return list == null ? Collections.<SysDictData>emptyList() : list;
    }

    @Override
    public SysDictData selectDataById(Long id) {
        return id == null ? null : dictDataMapper.selectDataById(id);
    }

    @Override
    public void saveData(SysDictData data, String operator) {
        if (data == null) {
            throw new ServiceException("字典数据不能为空");
        }
        if (!StringUtils.hasText(data.getDictType()) || !StringUtils.hasText(data.getDictValue())) {
            throw new ServiceException("字典类型与键值不能为空");
        }
        data.setCreateBy(operator);
        data.setIsDefault("Y".equals(data.getIsDefault()) ? "Y" : "N");
        data.setStatus(data.getStatus() == null ? "0" : data.getStatus());

        if (data.getId() == null) {
            dictDataMapper.insertData(data);
        } else {
            if (dictDataMapper.selectDataById(data.getId()) == null) {
                throw new ServiceException("字典数据不存在");
            }
            dictDataMapper.updateData(data);
        }

        if ("Y".equals(data.getIsDefault())) {
            dictDataMapper.clearDefaultByType(data.getDictType());
            dictDataMapper.updateData(data);
        }
        dictCache.refresh();
    }

    @Override
    public void deleteDataById(Long id) {
        if (id == null) {
            throw new ServiceException("字典数据ID不能为空");
        }
        if (dictDataMapper.selectDataById(id) == null) {
            throw new ServiceException("字典数据不存在");
        }
        dictDataMapper.deleteDataById(id);
        dictCache.refresh();
    }

    @Override
    public void refreshCache() {
        dictCache.refresh();
    }

}
