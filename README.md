# SysAdmin 系统

## 一、项目简介

SysAdmin 是一套基于经典 Java EE 技术栈开发的管理系统，采用 Struts2 + Spring + MyBatis（SSM 衍生架构）实现后台业务逻辑，前端使用 JSP 视图，数据持久化于 MySQL。本项目已支持 **本地 Tomcat 部署** 与 **Docker 一键部署**，适用于课程作业演示与快速交付。

> 仓库为 Public，可直接 clone 后按本文档部署。

## 二、技术栈

| 分类 | 技术 |
| --- | --- |
| 开发语言 | Java (JDK 8) |
| Web 框架 | Struts2 |
| 核心框架 | Spring |
| 持久层 | MyBatis |
| 数据库 | MySQL 8.0 |
| 连接池 | Druid |
| 构建工具 | Maven |
| 运行容器 | Tomcat 9 |
| 部署方式 | Docker / Docker Compose |

## 三、本地正常部署说明

### 1. 环境准备

- JDK 8
- Maven 3.x
- MySQL 8.0（本地安装或 Docker 均可）
- Tomcat 9（解压版即可）
- 数据库驱动：`com.mysql.cj.jdbc.Driver`（Connector/J 8.x，已随项目打包）

### 2. 创建数据库

登录 MySQL，执行建库语句（**库名统一为小写 `sysadmin`**，避免 Linux 下大小写敏感问题）：

```sql
CREATE DATABASE IF NOT EXISTS sysadmin
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;
```

### 3. 导入 SQL 文件

将项目中的初始化脚本导入到刚创建的数据库（脚本位于 `sql/init.sql`，其中包含建表语句与初始数据）：

```bash
mysql -uroot -p sysadmin < sql/init.sql
```

### 4. 修改数据库连接配置

打开 `src/main/resources/jdbc.properties`，确认数据库地址、账号、密码与本地环境一致：

```properties
jdbc.url=jdbc:mysql://127.0.0.1:3306/sysadmin?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true
jdbc.username=root
jdbc.password=123456
```

> 说明：本文件已改为支持环境变量覆盖（`${JDBC_URL:默认值}`）。本地未设置环境变量时自动使用上面默认值；Docker 部署时由 `docker-compose.yml` 注入变量覆盖，**无需手动修改**。

### 5. Maven 打包

在项目根目录执行（需先安装 Maven）：

```bash
mvn clean package
```

打包成功后会在 `target/` 目录生成 `SysAdmin.war`。

### 6. 部署到 Tomcat 并运行

将 `target/SysAdmin.war` 拷贝到 Tomcat 的 `webapps/` 目录，启动 Tomcat：

```bash
# Windows
cd %CATALINA_HOME%\bin
startup.bat

# Linux / macOS
$CATALINA_HOME/bin/startup.sh
```

Tomcat 启动后会自动解压部署，访问地址：

```
http://localhost:8080/SysAdmin
```

## 四、基于 Docker 的一键部署说明

### 前置条件

- 已安装 Docker 与 Docker Compose（Docker Desktop 自带 Compose）
- 能联网（多阶段 `Dockerfile` 会在**容器内**用 Maven 自动编译 `SysAdmin.war`，首次构建需访问 Maven 中央仓库下载依赖，耗时几分钟）
- 项目根目录存在 `sql/init.sql`（建库 / 建表 / 初始数据已就绪，**无需改动**）

### 关于 `sql/init.sql`

MySQL 官方镜像会在**首次启动**（数据目录为空时）自动执行 `/docker-entrypoint-initdb.d/` 下的 `.sql` 文件。本项目已将其挂载为 `./sql/init.sql`。

该文件已**完整就绪**，包含三部分：
1. 兼容老驱动的认证设置（`ALTER USER ... mysql_native_password`，密码与 `docker-compose.yml` 中 `MYSQL_ROOT_PASSWORD` / `JDBC_PASSWORD` 一致，均为 `123456`）；
2. 全部 13 张表的建表语句（来自 `sql/sysadmin_schema.sql`）；
3. 初始数据：部门、角色、用户（**默认账号 `admin` / `admin123`**）、菜单/权限、字典、公告等（来自 `sql/sysadmin_init_data.sql`）。

> 说明：MySQL 只在数据库首次初始化时执行 `init.sql`。若需重新执行，请先 `docker compose down -v` 删掉数据卷再 `up`。

### 一键启动

在项目根目录执行（首次会自动拉取基础镜像、在容器内编译 war、初始化数据库，请耐心等待几分钟）：

```bash
docker compose up -d --build
```

启动完成后访问：

```
http://localhost:8080/SysAdmin
```

> 默认管理员账号：`admin`，密码：`admin123`（首次登录后系统会自动把密码升级为更强的哈希）。

### 关键：Web 服务如何连上 MySQL

Docker 容器之间不能通过 `localhost` 互相访问，必须使用 **Compose 服务名 `mysql`** 作为数据库主机地址。本项目通过环境变量覆盖 `jdbc.properties` 实现：

- `docker-compose.yml` 中 `web` 服务设置了 `JDBC_URL=jdbc:mysql://mysql:3306/sysadmin?...`（主机是 `mysql`，即 mysql 服务名）。
- 应用使用的 Spring 配置 `<context:property-placeholder location="classpath:jdbc.properties"/>`（Spring 3.1+ 的 `PropertySourcesPlaceholderConfigurer`）会自动读取容器内的 **操作系统环境变量**，因此 `${JDBC_URL}` 会被成功解析为上面的地址。
- 因此：**无需在Docker里改 `jdbc.properties`**，只要保证 `src/main/resources/jdbc.properties` 中保留 `${JDBC_URL:默认值}` 这种写法即可（本项目已配置好）。

### 一键启动

在项目根目录执行：

```bash
# 构建镜像并后台启动全部服务（首次会自动打包/拉取镜像、初始化数据库）
docker compose up -d --build
```

启动完成后访问：

```
http://localhost:8080/SysAdmin
```

> 说明：`web` 服务配置了 `depends_on: mysql: condition: service_healthy`，会等 MySQL 健康检查通过后再启动，避免应用因库未就绪而连接失败。

### 常用命令

```bash
docker compose ps                 # 查看服务状态
docker compose logs -f web        # 实时查看 web 应用日志
docker compose logs -f mysql      # 实时查看 MySQL 日志
docker compose down              # 停止并删除容器（数据卷 mysql_data 仍保留）
docker compose down -v           # 停止并删除容器及数据卷（清空数据库）
```

### 注意事项

1. **数据库名大小写**：Linux 下 MySQL 库名默认区分大小写，请统一使用小写 `sysadmin`，且 `init.sql`、`MYSQL_DATABASE`、`JDBC_URL` 三处保持一致。
2. **密码一致性**：`docker-compose.yml` 中 `mysql` 的 `MYSQL_ROOT_PASSWORD` 与 `web` 的 `JDBC_PASSWORD` 必须相同。
3. **老驱动兼容**：已通过 `command: --default-authentication-plugin=mysql_native_password` 处理，确保老版本 Connector/J 也能连接。
4. **端口占用**：宿主机 8080 / 3306 被占用时，可在 `docker-compose.yml` 中修改左侧映射端口（如 `"9090:8080"`）。

## 五、常见问题（FAQ）

- **访问 404**：确认 `target/SysAdmin.war` 已生成，且访问路径为 `/SysAdmin`（上下文路径由 war 文件名决定）。
- **连接不上 MySQL**：检查 `JDBC_URL` 主机是否为 `mysql`（不是 `localhost`）；确认 `web` 与 `mysql` 在同一网络 `sysadmin-net`；查看 `docker compose logs mysql` 是否初始化成功。
- **密码错误**：确认 `init.sql` 中 `ALTER USER ... BY 'xxx'` 的密码与 `docker-compose.yml` 的 `JDBC_PASSWORD` 完全一致。
- **SQL 未执行/表不存在**：`/docker-entrypoint-initdb.d/` 下的脚本**仅在数据库数据目录为空（首次启动）时执行**；若已初始化过，请删除数据卷 `docker compose down -v` 后重新 `up`。
