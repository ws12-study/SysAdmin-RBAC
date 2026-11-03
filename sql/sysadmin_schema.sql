-- =============================================================
--  SysAdmin 通用后台用户权限与数据字典系统 —— 数据库脚本
--  MySQL 8.0 / InnoDB / utf8mb4
-- =============================================================
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS `sysadmin`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;
USE `sysadmin`;

-- ----------------------------
-- 1、部门表
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `parent_id`     BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '父部门ID（顶级为0）',
  `ancestors`     VARCHAR(500)    NOT NULL DEFAULT '' COMMENT '祖级路径（如 0,100,101）',
  `dept_name`     VARCHAR(30)     NOT NULL DEFAULT '' COMMENT '部门名称',
  `order_num`     INT             NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `leader`        VARCHAR(20)     DEFAULT NULL COMMENT '负责人',
  `phone`         VARCHAR(11)     DEFAULT NULL COMMENT '联系电话',
  `email`         VARCHAR(50)     DEFAULT NULL COMMENT '邮箱',
  `status`        CHAR(1)         NOT NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
  `create_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '更新者',
  `update_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark`        VARCHAR(500)    DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_dept_parent_id` (`parent_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 100 DEFAULT CHARSET = utf8mb4 COMMENT = '部门表';

-- ----------------------------
-- 2、用户表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id`       BIGINT UNSIGNED DEFAULT NULL COMMENT '部门ID',
  `user_name`     VARCHAR(30)     NOT NULL DEFAULT '' COMMENT '登录账号',
  `nick_name`     VARCHAR(30)     NOT NULL DEFAULT '' COMMENT '用户昵称',
  `user_type`     CHAR(1)         NOT NULL DEFAULT '0' COMMENT '用户类型（0系统用户 1普通会员）',
  `password`      VARCHAR(200)    NOT NULL DEFAULT '' COMMENT '登录密码（PBKDF2 哈希串）',
  `real_name`     VARCHAR(30)     DEFAULT NULL COMMENT '真实姓名',
  `email`         VARCHAR(50)     DEFAULT NULL COMMENT '邮箱',
  `phonenumber`   VARCHAR(11)     DEFAULT NULL COMMENT '手机号码',
  `sex`           CHAR(1)         DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar`        VARCHAR(255)    DEFAULT NULL COMMENT '头像路径',
  `status`        CHAR(1)         NOT NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
  `login_ip`      VARCHAR(128)    DEFAULT NULL COMMENT '最后登录IP',
  `login_date`    DATETIME        DEFAULT NULL COMMENT '最后登录时间',
  `create_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '更新者',
  `update_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark`        VARCHAR(500)    DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_name` (`user_name`),
  KEY `idx_user_dept_id` (`dept_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 100 DEFAULT CHARSET = utf8mb4 COMMENT = '用户表';

-- ----------------------------
-- 3、角色表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name`     VARCHAR(30)     NOT NULL DEFAULT '' COMMENT '角色名称',
  `role_key`      VARCHAR(100)    NOT NULL DEFAULT '' COMMENT '角色权限字符串',
  `role_sort`     INT             NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `data_scope`    CHAR(1)         NOT NULL DEFAULT '1' COMMENT '数据权限范围（1全部 2自定义 3本部门 4本部门及以下 5仅本人）',
  `status`        CHAR(1)         NOT NULL DEFAULT '0' COMMENT '角色状态（0正常 1停用）',
  `del_flag`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
  `create_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '更新者',
  `update_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark`        VARCHAR(500)    DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE = InnoDB AUTO_INCREMENT = 100 DEFAULT CHARSET = utf8mb4 COMMENT = '角色表';

-- ----------------------------
-- 4、菜单/权限表
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name`     VARCHAR(50)     NOT NULL DEFAULT '' COMMENT '菜单名称',
  `parent_id`     BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '父菜单ID（顶级为0）',
  `order_num`     INT             NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `path`          VARCHAR(200)    DEFAULT NULL COMMENT '路由地址',
  `component`     VARCHAR(255)    DEFAULT NULL COMMENT '组件路径',
  `query`         VARCHAR(255)    DEFAULT NULL COMMENT '路由参数',
  `is_frame`      TINYINT         NOT NULL DEFAULT 1 COMMENT '是否外链（0是 1否）',
  `is_cache`      TINYINT         NOT NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type`     CHAR(1)         NOT NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible`       CHAR(1)         NOT NULL DEFAULT '0' COMMENT '显示状态（0显示 1隐藏）',
  `status`        CHAR(1)         NOT NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms`         VARCHAR(100)    DEFAULT NULL COMMENT '权限标识',
  `icon`          VARCHAR(100)    DEFAULT '#' COMMENT '菜单图标',
  `create_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '更新者',
  `update_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark`        VARCHAR(500)    DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_menu_parent_id` (`parent_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1000 DEFAULT CHARSET = utf8mb4 COMMENT = '菜单权限表';

-- ----------------------------
-- 5、用户角色关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id`       BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
  `role_id`       BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '角色ID',
  `create_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '更新者',
  `update_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
  KEY `idx_user_role_role_id` (`role_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '用户角色关联表';

-- ----------------------------
-- 6、角色菜单关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id`       BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '角色ID',
  `menu_id`       BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '菜单ID',
  `create_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '更新者',
  `update_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`),
  KEY `idx_role_menu_menu_id` (`menu_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '角色菜单关联表';

-- ----------------------------
-- 7、角色部门关联表（数据权限）
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id`       BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '角色ID',
  `dept_id`       BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '部门ID',
  `create_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '更新者',
  `update_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_dept` (`role_id`, `dept_id`),
  KEY `idx_role_dept_dept_id` (`dept_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '角色部门关联表（数据权限）';

-- ----------------------------
-- 8、字典类型表
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name`     VARCHAR(100)    NOT NULL DEFAULT '' COMMENT '字典名称',
  `dict_type`     VARCHAR(100)    NOT NULL DEFAULT '' COMMENT '字典类型（唯一）',
  `status`        CHAR(1)         NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
  `create_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '更新者',
  `update_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark`        VARCHAR(500)    DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dict_type` (`dict_type`)
) ENGINE = InnoDB AUTO_INCREMENT = 100 DEFAULT CHARSET = utf8mb4 COMMENT = '字典类型表';

-- ----------------------------
-- 9、字典数据表
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort`     INT             NOT NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label`    VARCHAR(100)    NOT NULL DEFAULT '' COMMENT '字典标签',
  `dict_value`    VARCHAR(100)    NOT NULL DEFAULT '' COMMENT '字典键值',
  `dict_type`     VARCHAR(100)    NOT NULL DEFAULT '' COMMENT '字典类型',
  `list_class`    VARCHAR(100)    DEFAULT NULL COMMENT '表格回显样式',
  `is_default`    CHAR(1)         NOT NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status`        CHAR(1)         NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
  `create_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '更新者',
  `update_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark`        VARCHAR(500)    DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_dict_data_type` (`dict_type`)
) ENGINE = InnoDB AUTO_INCREMENT = 100 DEFAULT CHARSET = utf8mb4 COMMENT = '字典数据表';

-- ----------------------------
-- 10、操作日志表
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
  `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title`          VARCHAR(50)     NOT NULL DEFAULT '' COMMENT '模块标题',
  `business_type`  TINYINT         NOT NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除 4授权 5导出 6导入 7强退 8生成代码 9清空数据）',
  `method`         VARCHAR(200)    DEFAULT NULL COMMENT '方法名称',
  `request_method` VARCHAR(10)     DEFAULT NULL COMMENT '请求方式',
  `operator_type`  TINYINT         NOT NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name`      VARCHAR(50)     NOT NULL DEFAULT '' COMMENT '操作人员',
  `oper_url`       VARCHAR(255)    DEFAULT NULL COMMENT '请求URL',
  `oper_ip`        VARCHAR(128)    NOT NULL DEFAULT '' COMMENT '主机地址',
  `oper_location`  VARCHAR(255)    NOT NULL DEFAULT '' COMMENT '操作地点',
  `oper_param`     VARCHAR(2000)   DEFAULT NULL COMMENT '请求参数',
  `json_result`    VARCHAR(2000)   DEFAULT NULL COMMENT '返回结果',
  `status`         TINYINT         NOT NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg`      VARCHAR(2000)   DEFAULT NULL COMMENT '错误消息',
  `oper_time`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `create_by`      VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`      VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '更新者',
  `update_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_oper_log_time` (`oper_time`),
  KEY `idx_oper_log_name` (`oper_name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '操作日志记录';

-- ----------------------------
-- 11、登录日志表
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
  `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `login_name`     VARCHAR(50)     NOT NULL DEFAULT '' COMMENT '登录账号',
  `ip_address`     VARCHAR(128)    NOT NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` VARCHAR(255)    NOT NULL DEFAULT '' COMMENT '登录地点',
  `browser`        VARCHAR(50)     NOT NULL DEFAULT '' COMMENT '浏览器类型',
  `os`             VARCHAR(50)     NOT NULL DEFAULT '' COMMENT '操作系统',
  `status`         CHAR(1)         NOT NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg`            VARCHAR(255)    NOT NULL DEFAULT '' COMMENT '提示消息',
  `login_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `create_by`      VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`      VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '更新者',
  `update_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_login_log_time` (`login_time`),
  KEY `idx_login_log_name` (`login_name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '系统登录日志记录';

-- ----------------------------
-- 12、文件/附件表
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `file_name`     VARCHAR(255)    NOT NULL DEFAULT '' COMMENT '存储文件名',
  `original_name` VARCHAR(255)    NOT NULL DEFAULT '' COMMENT '原始文件名',
  `file_path`     VARCHAR(500)    NOT NULL DEFAULT '' COMMENT '存储路径',
  `file_url`      VARCHAR(500)    NOT NULL DEFAULT '' COMMENT '访问URL',
  `file_suffix`   VARCHAR(20)     NOT NULL DEFAULT '' COMMENT '文件后缀',
  `file_size`     BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '文件大小（字节）',
  `content_type`  VARCHAR(100)    DEFAULT NULL COMMENT 'MIME 类型',
  `storage_type`  VARCHAR(20)     NOT NULL DEFAULT 'local' COMMENT '存储方式（local 本地 oss 对象存储）',
  `module`        VARCHAR(50)     DEFAULT NULL COMMENT '业务模块',
  `business_id`   BIGINT UNSIGNED DEFAULT NULL COMMENT '关联业务ID',
  `del_flag`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
  `create_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '上传者',
  `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`     VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '更新者',
  `update_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark`        VARCHAR(500)    DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_file_module` (`module`, `business_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '文件附件表';

-- ----------------------------
-- 13、通知公告表
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title`   VARCHAR(50)     NOT NULL DEFAULT '' COMMENT '公告标题',
  `notice_type`    CHAR(1)         NOT NULL DEFAULT '1' COMMENT '公告类型（1通知 2公告）',
  `notice_content` TEXT            COMMENT '公告内容',
  `status`         CHAR(1)         NOT NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by`      VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`      VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '更新者',
  `update_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark`         VARCHAR(500)    DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_notice_type` (`notice_type`)
) ENGINE = InnoDB AUTO_INCREMENT = 100 DEFAULT CHARSET = utf8mb4 COMMENT = '通知公告表';

SET FOREIGN_KEY_CHECKS = 1;
