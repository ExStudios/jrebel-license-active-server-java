# JRebel许可证激活服务器 - 快速启动指南

## 🚀 快速开始

### 前提条件
- Docker已安装并运行
- 端口12345可用

### 方法一：一键启动（推荐）

```bash
# 1. 构建并运行
./docker-run.sh --build --run

# 2. 访问服务
# 主页: http://localhost:12345
# 激活接口: http://localhost:12345/jrebel/leases
```

### 方法二：分步执行

```bash
# 1. 构建项目
mvn clean package -DskipTests

# 2. 构建Docker镜像
docker build -t jrebel-license-active-server-java .

# 3. 运行容器
docker run -d \
  --name jrebel-license-active-server-java \
  -p 12345:12345 \
  --restart unless-stopped \
  jrebel-license-active-server-java
```

### 方法三：使用Docker Compose

```bash
# 启动服务
docker-compose up -d

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

## 🔧 常用命令

```bash
# 查看帮助
./docker-run.sh --help

# 查看容器状态
docker ps

# 查看日志
docker logs -f jrebel-license-active-server-java

# 停止容器
docker stop jrebel-license-active-server-java

# 重启容器
docker restart jrebel-license-active-server-java

# 清理资源
./docker-run.sh --clean
```

## ⚙️ 配置选项

### 环境变量

```bash
# 指定工作模式
docker run -e JREBEL_WORK_MODE=1 ...

# 指定离线天数
docker run -e JREBEL_OFFLINE_DAYS=365 ...

# 指定JVM参数
docker run -e JAVA_OPTS="-Xms1g -Xmx2g" ...
```

### 工作模式
- `0`: 自动模式（默认）
- `1`: 强制离线模式
- `2`: 强制在线模式
- `3`: oldGuid模式

## 🧪 测试服务

```bash
# 测试主页
curl http://localhost:12345/

# 测试激活接口
curl -X POST http://localhost:12345/jrebel/leases \
  -d "randomness=test123&username=test@example.com&guid=test-guid" \
  -H "Content-Type: application/x-www-form-urlencoded"

# 测试健康检查
curl http://localhost:12345/test
```

## 🔍 故障排除

### 常见问题

1. **Docker服务未运行**
   ```bash
   # 启动Docker Desktop
   # 或运行测试脚本
   ./test-docker.sh
   ```

2. **端口被占用**
   ```bash
   # 检查端口占用
   lsof -i :12345
   
   # 使用其他端口
   ./docker-run.sh --port 8080 --run
   ```

3. **构建失败**
   ```bash
   # 清理并重新构建
   mvn clean package -DskipTests
   docker build -t jrebel-license-active-server-java .
   ```

4. **容器启动失败**
   ```bash
   # 查看容器日志
   docker logs jrebel-license-active-server-java
   ```

## 📝 使用说明

### JRebel客户端配置

1. 在JRebel中配置许可证服务器地址：
   ```
   http://localhost:12345
   ```

2. 使用任意邮箱地址进行激活

3. 激活成功后会显示许可证信息

### 离线激活

如果配置为离线模式，客户端会获得离线许可证，可以在没有网络连接的情况下使用。

## 🆘 获取帮助

- 查看详细文档：`DOCKER_README.md`
- 运行测试脚本：`./test-docker.sh`
- 查看容器日志：`docker logs jrebel-license-active-server-java` 