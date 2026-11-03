package com.yourname.action;

import com.yourname.common.BusinessType;
import com.yourname.common.PageResult;
import com.yourname.common.ServiceException;
import com.yourname.common.annotation.OperLog;
import com.yourname.common.annotation.RequiresPermissions;
import com.yourname.entity.SysFile;
import com.yourname.service.SysFileService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * 文件管理
 *
 * @author yourname
 */
public class FileAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private SysFileService fileService;

    private PageResult<SysFile> page;
    private Integer pageNum;
    private Integer pageSize;
    private String module;
    private Long id;
    private String errorMsg;

    // Struts2 文件上传接收字段（xxxContentType / xxxFileName 为约定命名）
    private File file;
    private String fileContentType;
    private String fileFileName;

    // 下载输出
    private transient InputStream inputStream;
    private String downloadFileName;

    @RequiresPermissions("system:file:list")
    public String list() {
        page = fileService.page(PageResult.fixPageNum(pageNum), PageResult.fixPageSize(pageSize), module);
        return SUCCESS;
    }

    @RequiresPermissions("system:file:list")
    @OperLog(title = "文件管理", type = BusinessType.INSERT)
    public String upload() {
        try {
            fileService.upload(file, fileFileName, fileContentType, module, currentUserName());
            flashOk("文件上传成功");
            return SUCCESS;
        } catch (ServiceException e) {
            flashErr(e.getMessage());
            return INPUT;
        }
    }

    @RequiresPermissions("system:file:list")
    @OperLog(title = "文件管理", type = BusinessType.DELETE)
    public String delete() {
        try {
            fileService.deleteById(id);
            flashOk("文件已删除");
            return SUCCESS;
        } catch (ServiceException e) {
            flashErr(e.getMessage());
            return INPUT;
        }
    }

    /** 下载：读取磁盘文件流输出 */
    @RequiresPermissions("system:file:list")
    public String download() {
        try {
            SysFile f = fileService.selectById(id);
            if (f == null) {
                flashErr("文件不存在");
                return ERROR;
            }
            File diskFile = new File(f.getFilePath());
            if (!diskFile.exists()) {
                flashErr("磁盘文件已丢失");
                return ERROR;
            }
            inputStream = new FileInputStream(diskFile);
            downloadFileName = URLEncoder.encode(f.getOriginalName(), "UTF-8").replace("+", "%20");
            return "download";
        } catch (Exception e) {
            flashErr("文件下载失败：" + e.getMessage());
            return ERROR;
        }
    }

    public PageResult<SysFile> getPage() {
        return page;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
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

    public void setFile(File file) {
        this.file = file;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }

    /** 供分页链接复用：翻页时保留搜索条件 */
    public String getQueryString() {
        StringBuilder sb = new StringBuilder();
        com.yourname.common.QueryStringBuilder.append(sb, "module", module);
        return sb.toString();
    }

}
