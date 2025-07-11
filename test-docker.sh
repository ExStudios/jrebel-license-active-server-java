#!/bin/bash

# Docker配置测试脚本

echo "=== Docker配置测试 ==="

# 检查Docker是否安装
if command -v docker &> /dev/null; then
    echo "✅ Docker已安装"
else
    echo "❌ Docker未安装"
    exit 1
fi

# 检查Docker服务是否运行
if docker info &> /dev/null; then
    echo "✅ Docker服务正在运行"
else
    echo "❌ Docker服务未运行，请启动Docker Desktop"
    exit 1
fi

# 检查Docker Compose是否可用
if command -v docker-compose &> /dev/null; then
    echo "✅ Docker Compose已安装"
else
    echo "⚠️  Docker Compose未安装（可选）"
fi

# 检查端口是否可用
if lsof -i :12345 &> /dev/null; then
    echo "⚠️  端口12345已被占用"
    echo "   占用进程:"
    lsof -i :12345
else
    echo "✅ 端口12345可用"
fi

echo ""
echo "=== 测试完成 ==="
echo ""
echo "如果所有检查都通过，您可以运行以下命令："
echo ""
echo "1. 构建并运行（推荐）："
echo "   ./docker-run.sh --build --run"
echo ""
echo "2. 使用Docker Compose："
echo "   docker-compose up -d"
echo ""
echo "3. 手动构建和运行："
echo "   docker build -t jrebel-license-active-server-java ."
echo "   docker run -d --name jrebel-license-active-server-java -p 12345:12345 jrebel-license-active-server-java" 