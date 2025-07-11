# JRebel许可证激活服务器 Docker 部署指南

## 概述

本文档详细介绍了如何使用Docker部署和运行JRebel许可证激活服务器（Java版本）。

## 系统要求

- Docker 20.10+
- Docker Compose 2.0+ (可选)
- 至少 512MB 可用内存
- 至少 1GB 可用磁盘空间

## 快速开始

### 方法一：使用自动化脚本（推荐）

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd jrebel-license-active-server-master
   ```

2. **构建并运行**
   ```bash
   ./docker-run.sh --build --run
   ```

3. **访问服务**
   - 主页: http://localhost:12345
   - 激活接口: http://localhost:12345/jrebel/leases

### 方法二：使用Docker Compose

1. **启动服务**
   ```bash
   docker-compose up -d
   ```

2. **查看日志**
   ```bash
   docker-compose logs -f
   ```

3. **停止服务**
   ```bash
   docker-compose down
   ```

### 方法三：手动Docker命令

1. **构建镜像**
   ```bash
   docker build -t jrebel-license-active-server-java .
   ```

2. **运行容器**
   ```bash
   docker run -d \
     --name jrebel-license-active-server-java \
     -p 12345:12345 \
     -e JREBEL_WORK_MODE=0 \
     -e JREBEL_OFFLINE_DAYS=180 \
     --restart unless-stopped \
     jrebel-license-active-server-java
   ```

## 配置选项

### 环境变量

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| `SERVER_PORT` | 12345 | 服务器端口 |
| `JREBEL_WORK_MODE` | 0 | 工作模式 |
| `JREBEL_OFFLINE_DAYS` | 180 | 离线天数 |
| `JREBEL_SERVER_EXPORT_SCHEMA` | http | 导出协议 |
| `JREBEL_SERVER_NEW_INDEX` | true | 使用新索引页面 |
| `JAVA_OPTS` | -Xms512m -Xmx1024m -XX:+UseG1GC | JVM参数 |

### 工作模式说明

| 模式 | 说明 |
|------|------|
| 0 | 自动模式（根据客户端参数判断） |
| 1 | 强制离线模式 |
| 2 | 强制在线模式 |
| 3 | oldGuid模式 |

## 自动化脚本使用

### 基本用法

```bash
# 显示帮助信息
./docker-run.sh --help

# 构建镜像
./docker-run.sh --build

# 运行容器
./docker-run.sh --run

# 构建并运行
./docker-run.sh --build --run

# 指定端口运行
./docker-run.sh --port 8080 --run

# 指定工作模式运行
./docker-run.sh --mode 1 --run

# 使用docker-compose
./docker-run.sh --compose
```

### 高级用法

```bash
# 指定端口、工作模式和离线天数
./docker-run.sh --port 8080 --mode 1 --days 365 --run

# 查看容器日志
./docker-run.sh --logs

# 停止容器
./docker-run.sh --stop

# 清理所有资源
./docker-run.sh --clean
```

## 生产环境部署

### 1. 使用Docker Compose（推荐）

创建 `docker-compose.prod.yml`:

```yaml
version: '3.8'

services:
  jrebel-license-active-server:
    build: .
    container_name: jrebel-license-active-server-java
    ports:
      - "12345:12345"
    environment:
      - JREBEL_WORK_MODE=1
      - JREBEL_OFFLINE_DAYS=365
      - JAVA_OPTS=-Xms1g -Xmx2g -XX:+UseG1GC
    volumes:
      - ./logs:/app/logs
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "wget", "--no-verbose", "--tries=1", "--spider", "http://localhost:12345/test"]
      interval: 30s
      timeout: 10s
      retries: 3
    networks:
      - jrebel-network

networks:
  jrebel-network:
    driver: bridge
```

启动：
```bash
docker-compose -f docker-compose.prod.yml up -d
```

### 2. 使用反向代理

#### Nginx配置示例

```nginx
server {
    listen 80;
    server_name jrebel.your-domain.com;
    
    location / {
        proxy_pass http://localhost:12345;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

#### Traefik配置

在 `docker-compose.yml` 中添加标签：

```yaml
labels:
  - "traefik.enable=true"
  - "traefik.http.routers.jrebel.rule=Host(`jrebel.your-domain.com`)"
  - "traefik.http.services.jrebel.loadbalancer.server.port=12345"
```

### 3. 使用Docker Swarm

```bash
# 初始化Swarm
docker swarm init

# 部署服务
docker stack deploy -c docker-compose.yml jrebel
```

## 监控和维护

### 健康检查

容器内置健康检查，每30秒检查一次服务状态：

```bash
# 查看健康状态
docker ps

# 查看健康检查日志
docker inspect jrebel-license-active-server-java | grep -A 10 Health
```

### 日志管理

```bash
# 查看实时日志
docker logs -f jrebel-license-active-server-java

# 查看最近100行日志
docker logs --tail 100 jrebel-license-active-server-java

# 导出日志
docker logs jrebel-license-active-server-java > jrebel.log
```

### 性能监控

```bash
# 查看容器资源使用情况
docker stats jrebel-license-active-server-java

# 查看容器详细信息
docker inspect jrebel-license-active-server-java
```

## 故障排除

### 常见问题

1. **端口被占用**
   ```bash
   # 检查端口占用
   lsof -i :12345
   
   # 停止占用端口的进程
   sudo kill -9 <PID>
   ```

2. **容器启动失败**
   ```bash
   # 查看容器日志
   docker logs jrebel-license-active-server-java
   
   # 检查镜像是否存在
   docker images | grep jrebel
   ```

3. **内存不足**
   ```bash
   # 调整JVM内存参数
   docker run -e JAVA_OPTS="-Xms256m -Xmx512m" ...
   ```

4. **网络连接问题**
   ```bash
   # 检查容器网络
   docker network ls
   docker network inspect bridge
   ```

### 调试模式

```bash
# 以交互模式运行容器
docker run -it --rm -p 12345:12345 jrebel-license-active-server-java /bin/sh

# 进入运行中的容器
docker exec -it jrebel-license-active-server-java /bin/sh
```

## 安全建议

1. **使用非root用户运行**
   - 容器已配置为非root用户运行

2. **限制资源使用**
   ```bash
   docker run --memory=1g --cpus=1 ...
   ```

3. **网络安全**
   - 使用防火墙限制访问
   - 配置HTTPS反向代理
   - 定期更新基础镜像

4. **数据持久化**
   ```bash
   # 挂载日志目录
   docker run -v ./logs:/app/logs ...
   ```

## 更新和升级

### 更新镜像

```bash
# 拉取最新代码
git pull

# 重新构建镜像
./docker-run.sh --build

# 重启服务
./docker-run.sh --stop
./docker-run.sh --run
```

### 版本回滚

```bash
# 使用特定版本的镜像
docker run jrebel-license-active-server-java:v1.0.0
```

## 支持

如果遇到问题，请：

1. 查看容器日志
2. 检查系统资源使用情况
3. 验证网络连接
4. 提交Issue到项目仓库

## 许可证

本项目遵循原项目的许可证条款。 