# JRebel License Active Server - Java Implementation

这是JRebel License Active Server的Java实现版本，使用Spring Boot框架开发。

## 功能特性

- ✅ JRebel/XRebel许可证激活服务
- ✅ 支持在线和离线两种激活模式
- ✅ 美观的Web界面
- ✅ 完整的HTTP API接口
- ✅ RSA加密签名
- ✅ 灵活的配置选项
- ✅ Docker容器化支持

## 技术栈

- **Java 17**
- **Spring Boot 3.2.0**
- **Maven**
- **Jackson** (JSON处理)
- **SLF4J** (日志)

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+

### 编译运行

```bash
# 克隆项目
git clone <repository-url>
cd jrebel-license-active-server

# 编译项目
mvn clean package

# 运行项目
java -jar target/jrebel-license-active-server-1.0.0.jar
```

### 自定义配置

```bash
# 自定义端口
java -jar target/jrebel-license-active-server-1.0.0.jar --server.port=5555

# 使用HTTPS协议
java -jar target/jrebel-license-active-server-1.0.0.jar --jrebel.server.export-schema=https --jrebel.server.export-host=jrebel.domain.com
```

## 配置说明

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `server.port` | 12345 | 服务器端口 |
| `jrebel.server.offline-days` | 30 | 离线天数 |
| `jrebel.server.log-level` | 2 | 日志级别 (1-4) |
| `jrebel.server.export-schema` | http | 导出协议 (http/https) |
| `jrebel.server.export-host` | "" | 导出主机 (空为请求IP) |
| `jrebel.server.new-index` | true | 是否使用新索引页面 |
| `jrebel.server.jrebel-work-mode` | 0 | 工作模式 (0:自动, 1:强制离线, 2:强制在线, 3:oldGuid) |

## API接口

### 主要接口

- `GET /` - 主页面，显示激活地址
- `POST /jrebel/leases` - JRebel许可证激活接口
- `POST /jrebel/leases/1` - JRebel许可证接口1
- `POST /jrebel/validate-connection` - 连接验证接口
- `POST /rpc/ping.action` - 心跳检测接口
- `POST /rpc/obtainTicket.action` - 获取票据接口
- `POST /rpc/releaseTicket.action` - 释放票据接口

### 兼容接口

- `POST /agent/leases` - 兼容agent许可证接口
- `POST /agent/leases/1` - 兼容agent许可证接口1

## Docker部署

### 构建镜像

```bash
docker build -t jrebel-license-active-server:java .
```

### 运行容器

```bash
# 简单运行
docker run -p 12345:12345 jrebel-license-active-server:java

# 自定义端口
docker run -p 5555:5555 jrebel-license-active-server:java --server.port=5555
```

### Docker Compose

```yaml
version: '3.8'
services:
  jrebel-license-active-server:
    image: jrebel-license-active-server:java
    container_name: jrebel-license-active-server-java
    ports:
      - "12345:12345"
    environment:
      - SERVER_PORT=12345
      - JREBEL_SERVER_EXPORT_SCHEMA=http
```

## 使用说明

### JRebel 7.1及更早版本

激活地址格式：`http://your-server:12345/{tokenname}`

### JRebel 2018.1及更新版本

激活地址格式：`http://your-server:12345/{guid}`

其中`{guid}`是页面生成的UUID，如：`524f1d03-d1d8-5e94-a099-042736d40bd9`

## 项目结构

```
src/main/java/com/jrebel/
├── JrebelLicenseActiveServerApplication.java  # 主启动类
├── config/
│   ├── ServerConfig.java                      # 服务器配置
│   └── WebConfig.java                         # Web配置
├── controller/
│   └── JRebelController.java                  # HTTP控制器
├── model/
│   ├── JRebelLeasesStruct.java                # 许可证响应结构
│   ├── JrebelLeases1Struct.java               # 许可证响应结构1
│   └── JrebelValidateStruct.java              # 验证响应结构
├── service/
│   └── LicenseService.java                    # 许可证服务
└── util/
    ├── CryptoUtil.java                        # 加密工具
    └── UuidUtil.java                          # UUID工具
```

## 开发说明

### 添加新功能

1. 在`model`包中添加新的数据结构
2. 在`service`包中添加业务逻辑
3. 在`controller`包中添加HTTP接口
4. 更新配置文件（如需要）

### 测试

```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify
```

## 许可证

MIT License

## 贡献

欢迎提交Issue和Pull Request！

##
特别感谢 @YuXiaoyao 大佬开源的项目，本项目为完全翻译的JAVA版本
项目地址：https://github.com/yu-xiaoyao/jrebel-license-active-server

