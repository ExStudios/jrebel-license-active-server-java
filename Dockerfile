# 多阶段构建 - 构建阶段
FROM maven:3.9.10-openjdk-17 AS builder

# 设置工作目录
WORKDIR /build

# 复制pom.xml
COPY pom.xml .

# 下载依赖
RUN mvn dependency:go-offline -B

# 复制源代码
COPY src ./src

# 构建应用
RUN mvn clean package -DskipTests

# 运行阶段
FROM openjdk:17-jre-alpine

# 添加标签信息
LABEL org.opencontainers.image.author="Java Implementation" \
    org.opencontainers.image.description="JRebel and XRebel active server - Java Version" \
    org.opencontainers.image.source="https://github.com/your-repo/jrebel-license-active-server" \
    org.opencontainers.image.url="https://github.com/your-repo/jrebel-license-active-server" \
    org.opencontainers.image.title="jrebel-license-active-server-java" \
    org.opencontainers.image.version="1.0.0"

# 设置工作目录
WORKDIR /app

# 设置时区
ENV TZ=Asia/Shanghai

# 创建非root用户
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# 暴露端口
EXPOSE 12345

# 从构建阶段复制JAR文件
COPY --from=builder /build/target/jrebel-license-active-server-1.0.0.jar app.jar

# 更改文件所有者
RUN chown -R appuser:appgroup /app

# 切换到非root用户
USER appuser

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:12345/test || exit 1

# 设置启动命令
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=12345"]