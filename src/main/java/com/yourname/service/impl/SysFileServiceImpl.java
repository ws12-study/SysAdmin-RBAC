package com.yourname.service.impl;

import com.yourname.common.PageResult;
import com.yourname.common.ServiceException;
import com.yourname.common.TimeRange;
import com.yourname.entity.SysFile;
import com.yourname.mapper.SysFileMapper;
import com.yourname.service.SysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * 文件服务实现：本地磁盘存储
 *
 * @author yourname
 */
@Service("sysFileServiceImpl")
public class SysFileServiceImpl implements SysFileService {

    /** 本地存储根目录（放在 Tomcat 之外，避免重新部署丢失文件） */
    private static final String UPLOAD_ROOT = "D:/sysadmin-upload";

    @Autowired
    private SysFileMapper fileMapper;

    @Override
    public PageResult<SysFile> page(int pageNum, int pageSize, String module) {
        int num = PageResult.fixPageNum(pageNum);
        int size = PageResult.fixPageSize(pageSize);
        String mod = TimeRange.trim(module);
        List<SysFile> rows = fileMapper.selectPage(PageResult.offset(num, size), size, mod);
        long total = fileMapper.countPage(mod);
        return PageResult.of(rows, total, num, size);
    }

    @Override
    public SysFile selectById(Long id) {
        return id == null ? null : fileMapper.selectById(id);
    }

    @Override
    public SysFile upload(File uploadedFile, String originalName, String contentType, String module, String operator) {
        if (uploadedFile == null || !uploadedFile.exists()) {
            throw new ServiceException("未接收到上传文件");
        }
        if (!StringUtils.hasText(originalName)) {
            originalName = "未命名文件";
        }
        String suffix = "";
        int dot = originalName.lastIndexOf('.');
        if (dot >= 0 && dot < originalName.length() - 1) {
            suffix = originalName.substring(dot).toLowerCase();
        }

        File dir = new File(UPLOAD_ROOT);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new ServiceException("无法创建存储目录：" + UPLOAD_ROOT);
        }
        String storedName = UUID.randomUUID().toString().replace("-", "") + suffix;
        File dest = new File(dir, storedName);
        try {
            Files.move(uploadedFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ServiceException("文件保存失败：" + e.getMessage());
        }

        SysFile file = new SysFile();
        file.setFileName(storedName);
        file.setOriginalName(originalName);
        file.setFilePath(dest.getAbsolutePath());
        file.setFileUrl("/SysAdmin/file_download.action?id=");
        file.setFileSuffix(suffix);
        file.setFileSize(dest.length());
        file.setContentType(contentType);
        file.setStorageType("local");
        file.setModule(TimeRange.trim(module));
        file.setCreateBy(operator);
        fileMapper.insert(file);
        // 回填下载地址
        file.setFileUrl("/SysAdmin/file_download.action?id=" + file.getId());
        return file;
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new ServiceException("文件ID不能为空");
        }
        SysFile file = fileMapper.selectById(id);
        if (file == null) {
            throw new ServiceException("文件不存在");
        }
        fileMapper.deleteById(id);
        // 删除磁盘文件（失败不阻断，仅记录）
        if (StringUtils.hasText(file.getFilePath())) {
            File diskFile = new File(file.getFilePath());
            if (diskFile.exists() && !diskFile.delete()) {
                // 忽略：磁盘清理失败不影响记录删除
            }
        }
    }

}
