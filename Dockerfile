# ===================== 第一阶段：用 Maven 编译 war 包 =====================
FROM maven:3.9-eclipse-temurin-8 AS build
WORKDIR /app
# 先拷贝 pom.xml 与源码（利用 Docker 层缓存：仅改源码时不会重新下载依赖）
COPY pom.xml .
COPY src ./src
# 打包（跳过测试，加快构建；如需跑测试去掉 -DskipTests）
RUN mvn -B clean package -DskipTests

# ===================== 第二阶段：Tomcat 运行 war 包 =====================
FROM tomcat:9.0-jdk8

# 移除 Tomcat 自带的示例 / 管理应用（ROOT、docs、examples、host-manager、manager）
# 既保持镜像精简，也避免默认 ROOT 应用与我们的 SysAdmin 冲突
RUN rm -rf /usr/local/tomcat/webapps/*

# 从构建阶段拷贝编译好的 SysAdmin.war 到 webapps 目录
# Tomcat 启动时会自动解压部署，上下文路径为 /SysAdmin
COPY --from=build /app/target/SysAdmin.war /usr/local/tomcat/webapps/SysAdmin.war

# 暴露端口
EXPOSE 8080

# 以前台方式启动 Tomcat（容器主进程，便于 docker 管理生命周期）
CMD ["catalina.sh", "run"]
