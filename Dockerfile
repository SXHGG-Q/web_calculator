# 基础镜像：Java 21 轻量级运行环境
FROM eclipse-temurin:21-jre-alpine

# 工作目录
WORKDIR /app

# 复制打包好的jar包到容器
COPY target/*.jar app.jar

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]