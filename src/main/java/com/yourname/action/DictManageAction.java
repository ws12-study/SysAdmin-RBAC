package com.yourname.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yourname.common.BusinessType;
import com.yourname.common.ServiceException;
import com.yourname.common.annotation.OperLog;
import com.yourname.common.annotation.RequiresPermissions;
import com.yourname.entity.SysDictData;
import com.yourname.entity.SysDictType;
import com.yourname.entity.SysUser;
import com.yourname.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 数据字典管理（字典类型 + 字典数据）
 *
 * @author yourname
 */
public class DictManageAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private SysDictService dictService;

    private List<SysDictType> types;
    private SysDictType dictType;
    private List<SysDictData> datas;
    private SysDictData dictData;
    /** 当前查看的字典类型（字典数据列表用） */
    private String type;
    private Long id;
    private String errorMsg;

    // ================= 字典类型 =================

    @RequiresPermissions("system:dict:list")
    public String typeList() {
        types = dictService.selectTypeList();
        return SUCCESS;
    }

    @RequiresPermissions("system:dict:edit")
    public String typeInput() {
        if (id == null) {
            dictType = new SysDictType();
            dictType.setStatus("0");
        } else {
            dictType = dictService.selectTypeById(id);
            if (dictType == null) {
                errorMsg = "字典不存在";
                return INPUT;
            }
        }
        return SUCCESS;
    }

    @RequiresPermissions("system:dict:edit")
    @OperLog(title = "字典类型", type = BusinessType.UPDATE)
    public String typeSave() {
        try {
            dictService.saveType(dictType, currentUserName());
            flashOk("字典类型保存成功");
            return SUCCESS;
        } catch (ServiceException e) {
            errorMsg = e.getMessage();
            return INPUT;
        }
    }

    @RequiresPermissions("system:dict:remove")
    @OperLog(title = "字典类型", type = BusinessType.DELETE)
    public String typeDelete() {
        try {
            dictService.deleteTypeById(id);
            flashOk("字典类型已删除");
            return SUCCESS;
        } catch (ServiceException e) {
            errorMsg = e.getMessage();
            types = dictService.selectTypeList();
            return INPUT;
        }
    }

    // ================= 字典数据 =================

    @RequiresPermissions("system:dict:list")
    public String dataList() {
        datas = dictService.selectDataList(type);
        return SUCCESS;
    }

    @RequiresPermissions("system:dict:edit")
    public String dataInput() {
        if (id == null) {
            dictData = new SysDictData();
            dictData.setDictType(type);
            dictData.setDictSort(1);
            dictData.setStatus("0");
            dictData.setIsDefault("N");
        } else {
            dictData = dictService.selectDataById(id);
            if (dictData == null) {
                errorMsg = "字典数据不存在";
                return INPUT;
            }
        }
        return SUCCESS;
    }

    @RequiresPermissions("system:dict:edit")
    @OperLog(title = "字典数据", type = BusinessType.UPDATE)
    public String dataSave() {
        try {
            dictService.saveData(dictData, currentUserName());
            type = dictData.getDictType();
            flashOk("字典数据保存成功");
            return SUCCESS;
        } catch (ServiceException e) {
            errorMsg = e.getMessage();
            return INPUT;
        }
    }

    @RequiresPermissions("system:dict:remove")
    @OperLog(title = "字典数据", type = BusinessType.DELETE)
    public String dataDelete() {
        try {
            SysDictData target = dictService.selectDataById(id);
            if (target != null) {
                type = target.getDictType();
                dictService.deleteDataById(id);
            }
            flashOk("字典数据已删除");
            return SUCCESS;
        } catch (ServiceException e) {
            errorMsg = e.getMessage();
            datas = dictService.selectDataList(type);
            return INPUT;
        }
    }

    /**
     * 手动刷新字典缓存
     */
    @RequiresPermissions("system:dict:edit")
    public String refresh() {
        dictService.refreshCache();
        return SUCCESS;
    }

    public List<SysDictType> getTypes() {
        return types;
    }

    public SysDictType getDictType() {
        return dictType;
    }

    public void setDictType(SysDictType dictType) {
        this.dictType = dictType;
    }

    public List<SysDictData> getDatas() {
        return datas;
    }

    public SysDictData getDictData() {
        return dictData;
    }

    public void setDictData(SysDictData dictData) {
        this.dictData = dictData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
