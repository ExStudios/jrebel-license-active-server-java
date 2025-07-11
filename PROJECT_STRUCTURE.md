# 项目结构说明

## 目录结构

```
jrebel-license-active-server-master/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── jrebel/
│   │   │           ├── JrebelLicenseActiveServerApplication.java  # 主启动类
│   │   │           ├── config/
│   │   │           │   ├── ServerConfig.java                      # 服务器配置
│   │   │           │   └── WebConfig.java                         # Web配置
│   │   │           ├── controller/
│   │   │           │   └── JRebelController.java                  # HTTP控制器
│   │   │           ├── model/
│   │   │           │   ├── JRebelLeasesStruct.java                # 许可证响应结构
│   │   │           │   ├── JrebelLeases1Struct.java               # 许可证响应结构1
│   │   │           │   └── JrebelValidateStruct.java              # 验证响应结构
│   │   │           ├── service/
│   │   │           │   └── LicenseService.java                    # 许可证服务
│   │   │           └── util/
│   │   │               ├── CryptoUtil.java                        # 加密工具
│   │   │               └── UuidUtil.java                          # UUID工具
│   │   └── resources/
│   │       └── application.yml                                    # 配置文件
│   └── test/
│       └── java/
│           └── com/
│               └── jrebel/
│                   └── JrebelLicenseActiveServerApplicationTests.java  # 测试类
├── pom.xml                                                         # Maven配置
├── Dockerfile                                                      # Docker配置
├── docker-compose.yml                                              # Docker Compose配置
├── run.sh                                                          # Linux/Mac启动脚本
├── run.bat                                                         # Windows启动脚本
├── README.md                                                       # 项目说明
├── PROJECT_STRUCTURE.md                                            # 项目结构说明
└── .gitignore                                                      # Git忽略文件
```

## 核心文件说明

### 1. 主启动类
- **JrebelLicenseActiveServerApplication.java**: Spring Boot应用程序入口点

### 2. 配置类
- **ServerConfig.java**: 服务器配置，对应原Go项目的Config结构
- **WebConfig.java**: Web服务器配置，设置端口等

### 3. 控制器类
- **JRebelController.java**: 处理所有HTTP请求，包括：
  - 主页面显示
  - JRebel许可证激活接口
  - 验证连接接口
  - RPC接口（ping、obtainTicket、releaseTicket）

### 4. 模型类
- **JRebelLeasesStruct.java**: 主要的许可证响应数据结构
- **JrebelLeases1Struct.java**: 备用许可证响应结构
- **JrebelValidateStruct.java**: 验证响应结构

### 5. 服务类
- **LicenseService.java**: 核心业务逻辑，包括：
  - 许可证生成
  - 加密签名
  - 参数验证
  - 响应构建

### 6. 工具类
- **CryptoUtil.java**: 加密工具，实现RSA签名功能
- **UuidUtil.java**: UUID生成工具

### 7. 配置文件
- **application.yml**: Spring Boot配置文件
- **pom.xml**: Maven项目配置和依赖管理

## 功能模块

### 1. HTTP接口模块
- 处理JRebel客户端的各种请求
- 生成相应的JSON和XML响应
- 记录请求日志

### 2. 许可证生成模块
- 根据客户端参数生成许可证
- 支持在线和离线模式
- 计算有效期

### 3. 加密签名模块
- RSA私钥签名
- SHA1和MD5算法支持
- Base64编码

### 4. 配置管理模块
- 服务器端口配置
- 工作模式配置
- 导出协议配置

### 5. Web界面模块
- 美观的HTML界面
- 显示激活地址
- 响应式设计

## 技术特点

1. **Spring Boot框架**: 现代化的Java Web框架
2. **RESTful API**: 标准的HTTP接口设计
3. **配置外部化**: 支持多种配置方式
4. **日志系统**: 使用SLF4J进行日志记录
5. **加密安全**: 使用Java内置的加密库
6. **容器化支持**: 完整的Docker配置
7. **跨平台**: 支持Windows、Linux、Mac

## 与原Go版本对比

| 特性 | Go版本 | Java版本 |
|------|--------|----------|
| 语言 | Go | Java 17 |
| 框架 | 标准库 | Spring Boot |
| 构建工具 | go build | Maven |
| 依赖管理 | go.mod | pom.xml |
| 配置 | 命令行参数 | application.yml |
| 日志 | 自定义 | SLF4J |
| 加密 | crypto包 | JCE |
| 部署 | 二进制文件 | JAR文件 |
| 容器化 | Docker | Docker |
| 开发体验 | 简单 | 丰富 |

## 开发建议

1. **IDE推荐**: IntelliJ IDEA 或 Eclipse
2. **Java版本**: 必须使用JDK 17+
3. **Maven版本**: 建议使用3.6+
4. **测试**: 使用JUnit 5进行单元测试
5. **代码风格**: 遵循Java编码规范
6. **文档**: 使用JavaDoc注释 