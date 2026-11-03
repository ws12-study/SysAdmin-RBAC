package com.yourname.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件附件表 sys_file
 *
 * @author yourname
 */
@Data
public class SysFile implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /** 存储文件名（UUID + 后缀） */
    private String fileName;
    /** 原始文件名 */
    private String originalName;
    /** 存储路径（本地绝对路径） */
    private String filePath;
    /** 访问 URL */
    private String fileUrl;
    /** 文件后缀 */
    private String fileSuffix;
    /** 文件大小（字节） */
    private Long fileSize;
    /** MIME 类型 */
    private String contentType;
    /** 存储方式（local 本地） */
    private String storageType;
    /** 业务模块 */
    private String module;
    /** 关联业务ID */
    private Long businessId;
    private String delFlag;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String remark;

}
