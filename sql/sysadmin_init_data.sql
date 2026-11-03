-- =============================================================
--  SysAdmin 初始化数据（用于启动自检）
--  默认账号：admin / admin123
--  初始密码存的是无盐 MD5（兼容旧数据），首次登录后会由系统自动重写为 PBKDF2
-- =============================================================
USE `sysadmin`;

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 部门
-- ----------------------------
INSERT INTO `sys_dept` (`id`, `parent_id`, `ancestors`, `dept_name`, `order_num`, `leader`, `phone`, `email`, `status`, `del_flag`, `create_by`) VALUES
(100, 0, '0', '总公司', 1, 'admin', '15888888888', 'admin@sysadmin.com', '0', '0', 'admin'),
(101, 100, '0,100', '研发部', 1, 'admin', '15888888889', 'dev@sysadmin.com', '0', '0', 'admin'),
(102, 100, '0,100', '运营部', 2, 'admin', '15888888890', 'ops@sysadmin.com', '0', '0', 'admin');

-- ----------------------------
-- 角色
-- ----------------------------
INSERT INTO `sys_role` (`id`, `role_name`, `role_key`, `role_sort`, `data_scope`, `status`, `del_flag`, `create_by`, `remark`) VALUES
(1, '超级管理员', 'admin', 1, '1', '0', '0', 'admin', '拥有系统全部权限'),
(2, '普通角色',   'common', 2, '3', '0', '0', 'admin', '仅可查看本部门数据');

-- ----------------------------
-- 用户（初始为 admin123 的无盐 MD5，登录后自动升级为 pbkdf2$... ）
-- ----------------------------
INSERT INTO `sys_user` (`id`, `dept_id`, `user_name`, `nick_name`, `user_type`, `password`, `real_name`, `email`, `phonenumber`, `sex`, `avatar`, `status`, `del_flag`, `create_by`, `remark`) VALUES
(1, 100, 'admin', '系统管理员', '0', '0192023a7bbd73250516f069df18b500', '管理员', 'admin@sysadmin.com', '15888888888', '0', '', '0', '0', 'admin', '初始超级管理员'),
(2, 101, 'test',  '测试用户',   '0', '0192023a7bbd73250516f069df18b500', '测试员', 'test@sysadmin.com',  '15666666666', '0', '', '0', '0', 'admin', '用于自检');

-- ----------------------------
-- 用户-角色
-- ----------------------------
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `create_by`) VALUES
(1, 1, 'admin'),
(2, 2, 'admin');

-- ----------------------------
-- 菜单
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`) VALUES
(1,  '系统管理', 0,  1, '/system',        '',                    '', 1, 0, 'M', '0', '0', '',                     'tool',       'admin'),
(2,  '用户管理', 1,  1, 'user_list',      'system/user/index',   '', 1, 0, 'C', '0', '0', 'system:user:list',     'user',       'admin'),
(3,  '角色管理', 1,  2, 'role_list',      'system/role/index',   '', 1, 0, 'C', '0', '0', 'system:role:list',     'peoples',    'admin'),
-- status='1' 表示模块尚未实现，不出现在导航与授权树中（实现后改回 '0' 即可）
(4,  '菜单管理', 1,  3, 'menu_list',      'system/menu/index',   '', 1, 0, 'C', '0', '0', 'system:menu:list',     'tree-table', 'admin'),
(5,  '部门管理', 1,  4, 'dept_list',      'system/dept/index',   '', 1, 0, 'C', '0', '0', 'system:dept:list',     'tree',       'admin'),
(6,  '字典管理', 1,  5, 'dict_type_list', 'system/dict/index',   '', 1, 0, 'C', '0', '0', 'system:dict:list',     'dict',       'admin'),
(7,  '操作日志', 1,  6, 'operlog_list',   'system/operlog/index','', 1, 0, 'C', '0', '0', 'system:operlog:list',  'log',        'admin'),
(8,  '登录日志', 1,  7, 'loginlog_list',  'system/loginlog/index','',1, 0, 'C', '0', '0', 'system:loginlog:list', 'log',        'admin'),
(9,  '通知公告', 1,  8, 'notice_list',    'system/notice/index', '', 1, 0, 'C', '0', '0', 'system:notice:list',   'message',    'admin'),
(10, '文件管理', 1,  9, 'file_list',      'system/file/index',   '', 1, 0, 'C', '0', '0', 'system:file:list',     'folder',     'admin');

-- ----------------------------
-- 按钮级权限（F 按钮，用于服务端 @RequiresPermissions 校验）
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`) VALUES
(11, '用户查询', 2, 1, 'F', '1', '0', 'system:user:list', '#', 'admin'),
(12, '用户授权', 2, 2, 'F', '1', '0', 'system:user:edit', '#', 'admin'),
(13, '角色查询', 3, 1, 'F', '1', '0', 'system:role:list', '#', 'admin'),
(14, '角色授权', 3, 2, 'F', '1', '0', 'system:role:edit', '#', 'admin'),
(15, '角色删除', 3, 3, 'F', '1', '0', 'system:role:remove', '#', 'admin'),
(16, '字典查询', 6, 1, 'F', '1', '0', 'system:dict:list', '#', 'admin'),
(17, '字典编辑', 6, 2, 'F', '1', '0', 'system:dict:edit', '#', 'admin'),
(18, '字典删除', 6, 3, 'F', '1', '0', 'system:dict:remove', '#', 'admin');

-- ----------------------------
-- 角色-菜单（超级管理员拥有全部菜单）
-- ----------------------------
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_by`) VALUES
(1, 1, 'admin'), (1, 2, 'admin'), (1, 3, 'admin'), (1, 4, 'admin'), (1, 5, 'admin'),
(1, 6, 'admin'), (1, 7, 'admin'), (1, 8, 'admin'), (1, 9, 'admin'), (1, 10, 'admin'),
(1, 11, 'admin'), (1, 12, 'admin'), (1, 13, 'admin'), (1, 14, 'admin'), (1, 15, 'admin'),
(1, 16, 'admin'), (1, 17, 'admin'), (1, 18, 'admin'),
(2, 1, 'admin'), (2, 2, 'admin');

-- ----------------------------
-- 角色-部门（data_scope = 2 自定义时生效）
-- ----------------------------
INSERT INTO `sys_role_dept` (`role_id`, `dept_id`, `create_by`) VALUES
(2, 101, 'admin');

-- ----------------------------
-- 数据字典
-- ----------------------------
INSERT INTO `sys_dict_type` (`id`, `dict_name`, `dict_type`, `status`, `create_by`, `remark`) VALUES
(1, '用户性别',   'sys_user_sex',        '0', 'admin', '用户性别列表'),
(2, '菜单状态',   'sys_show_hide',       '0', 'admin', '菜单状态列表'),
(3, '系统开关',   'sys_normal_disable',  '0', 'admin', '系统开关列表'),
(4, '系统是否',   'sys_yes_no',          '0', 'admin', '系统是否列表'),
(5, '任务状态',   'sys_job_status',      '0', 'admin', '任务状态列表');

INSERT INTO `sys_dict_data` (`id`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `list_class`, `is_default`, `status`, `create_by`, `remark`) VALUES
(1, 1, '男',   '0', 'sys_user_sex', 'primary', 'N', '0', 'admin', ''),
(2, 2, '女',   '1', 'sys_user_sex', 'danger',  'N', '0', 'admin', ''),
(3, 3, '未知', '2', 'sys_user_sex', 'info',    'N', '0', 'admin', ''),

(4, 1, '显示', '0', 'sys_show_hide', 'success', 'Y', '0', 'admin', ''),
(5, 2, '隐藏', '1', 'sys_show_hide', 'info',    'N', '0', 'admin', ''),

(6, 1, '正常', '0', 'sys_normal_disable', 'success', 'Y', '0', 'admin', ''),
(7, 2, '停用', '1', 'sys_normal_disable', 'danger',  'N', '0', 'admin', ''),

(8, 1, '是',   'Y', 'sys_yes_no', 'success', 'Y', '0', 'admin', ''),
(9, 2, '否',   'N', 'sys_yes_no', 'danger',  'N', '0', 'admin', ''),

(10, 1, '正常', '0', 'sys_job_status', 'success', 'Y', '0', 'admin', ''),
(11, 2, '暂停', '1', 'sys_job_status', 'danger',  'N', '0', 'admin', '');

-- ----------------------------
-- 通知公告示例
-- ----------------------------
INSERT INTO `sys_notice` (`id`, `notice_title`, `notice_type`, `notice_content`, `status`, `create_by`, `remark`) VALUES
(1, '欢迎使用 SysAdmin', '2', '系统骨架已初始化完成，请尽快修改默认账号密码。', '0', 'admin', '');

SET FOREIGN_KEY_CHECKS = 1;
