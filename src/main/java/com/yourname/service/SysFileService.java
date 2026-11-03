package com.yourname.service;

import com.yourname.common.PageResult;
import com.yourname.entity.SysFile;

import java.io.File;

/**
 * 文件附件 Service
 *
 * @author yourname
 */
public interface SysFileService {

    PageResult<SysFile> page(int pageNum, int pageSize, String module);

    SysFile selectById(Long id);

    /**
     * 保存上传文件到本地存储并记录元数据
     *
     * @param uploadedFile 已接收的临时文件
     * @param originalName 原始文件名
     * @param contentType  MIME 类型
     * @param module       业务模块
     */
    SysFile upload(File uploadedFile, String originalName, String contentType, String module, String operator);

    /**
     * 逻辑删除记录并删除磁盘文件
     */
    void deleteById(Long id);

}
