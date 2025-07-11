#!/bin/bash

# JRebel许可证激活服务器 Docker 运行脚本
# 作者: Java Implementation
# 版本: 1.0.0

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 默认配置
IMAGE_NAME="jrebel-license-active-server-java"
CONTAINER_NAME="jrebel-license-active-server-java"
PORT="12345"
WORK_MODE="0"
OFFLINE_DAYS="180"

# 打印帮助信息
print_help() {
    echo -e "${BLUE}JRebel许可证激活服务器 Docker 运行脚本${NC}"
    echo ""
    echo "用法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  -h, --help              显示此帮助信息"
    echo "  -p, --port PORT         指定端口 (默认: 12345)"
    echo "  -m, --mode MODE         工作模式 (默认: 0)"
    echo "                          0: 自动模式"
    echo "                          1: 强制离线模式"
    echo "                          2: 强制在线模式"
    echo "                          3: oldGuid模式"
    echo "  -d, --days DAYS         离线天数 (默认: 180)"
    echo "  -b, --build             构建镜像"
    echo "  -r, --run               运行容器"
    echo "  -s, --stop              停止容器"
    echo "  -l, --logs              查看日志"
    echo "  -c, --clean             清理容器和镜像"
    echo "  --compose               使用docker-compose运行"
    echo ""
    echo "示例:"
    echo "  $0 --build --run                    # 构建并运行"
    echo "  $0 --port 8080 --mode 1 --run       # 指定端口和工作模式运行"
    echo "  $0 --compose                        # 使用docker-compose运行"
    echo ""
}

# 构建镜像
build_image() {
    echo -e "${YELLOW}正在构建Docker镜像...${NC}"
    docker build -t $IMAGE_NAME .
    echo -e "${GREEN}镜像构建完成！${NC}"
}

# 运行容器
run_container() {
    echo -e "${YELLOW}正在启动容器...${NC}"
    
    # 检查容器是否已存在
    if docker ps -a --format 'table {{.Names}}' | grep -q "^$CONTAINER_NAME$"; then
        echo -e "${YELLOW}容器已存在，正在停止并删除...${NC}"
        docker stop $CONTAINER_NAME >/dev/null 2>&1 || true
        docker rm $CONTAINER_NAME >/dev/null 2>&1 || true
    fi
    
    # 运行容器
    docker run -d \
        --name $CONTAINER_NAME \
        -p $PORT:12345 \
        -e JREBEL_WORK_MODE=$WORK_MODE \
        -e JREBEL_OFFLINE_DAYS=$OFFLINE_DAYS \
        -e JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC" \
        --restart unless-stopped \
        $IMAGE_NAME
    
    echo -e "${GREEN}容器启动成功！${NC}"
    echo -e "${BLUE}访问地址: http://localhost:$PORT${NC}"
    echo -e "${BLUE}激活地址: http://localhost:$PORT/jrebel/leases${NC}"
}

# 停止容器
stop_container() {
    echo -e "${YELLOW}正在停止容器...${NC}"
    docker stop $CONTAINER_NAME >/dev/null 2>&1 || true
    echo -e "${GREEN}容器已停止！${NC}"
}

# 查看日志
show_logs() {
    echo -e "${YELLOW}正在查看容器日志...${NC}"
    docker logs -f $CONTAINER_NAME
}

# 清理容器和镜像
clean_all() {
    echo -e "${YELLOW}正在清理容器和镜像...${NC}"
    docker stop $CONTAINER_NAME >/dev/null 2>&1 || true
    docker rm $CONTAINER_NAME >/dev/null 2>&1 || true
    docker rmi $IMAGE_NAME >/dev/null 2>&1 || true
    echo -e "${GREEN}清理完成！${NC}"
}

# 使用docker-compose运行
run_compose() {
    echo -e "${YELLOW}正在使用docker-compose启动服务...${NC}"
    docker-compose up -d
    echo -e "${GREEN}服务启动成功！${NC}"
    echo -e "${BLUE}访问地址: http://localhost:$PORT${NC}"
    echo -e "${BLUE}激活地址: http://localhost:$PORT/jrebel/leases${NC}"
}

# 检查Docker是否安装
check_docker() {
    if ! command -v docker &> /dev/null; then
        echo -e "${RED}错误: Docker未安装或不在PATH中${NC}"
        exit 1
    fi
    
    if ! docker info &> /dev/null; then
        echo -e "${RED}错误: Docker服务未运行${NC}"
        exit 1
    fi
}

# 主函数
main() {
    check_docker
    
    # 解析命令行参数
    while [[ $# -gt 0 ]]; do
        case $1 in
            -h|--help)
                print_help
                exit 0
                ;;
            -p|--port)
                PORT="$2"
                shift 2
                ;;
            -m|--mode)
                WORK_MODE="$2"
                shift 2
                ;;
            -d|--days)
                OFFLINE_DAYS="$2"
                shift 2
                ;;
            -b|--build)
                build_image
                ;;
            -r|--run)
                run_container
                ;;
            -s|--stop)
                stop_container
                ;;
            -l|--logs)
                show_logs
                ;;
            -c|--clean)
                clean_all
                ;;
            --compose)
                run_compose
                ;;
            *)
                echo -e "${RED}未知选项: $1${NC}"
                print_help
                exit 1
                ;;
        esac
    done
    
    # 如果没有指定任何操作，显示帮助
    if [[ $# -eq 0 ]]; then
        print_help
    fi
}

# 运行主函数
main "$@" 