# 第一阶段：用Maven镜像打包（生成jar包）
FROM maven:3.9.8-eclipse-temurin-21 AS builder

# 工作目录
WORKDIR /app

# 复制pom.xml和源码
COPY pom.xml .
COPY src ./src

# 执行Maven打包（跳过测试，加快速度）
RUN mvn clean package -DskipTests

# 第二阶段：用轻量JRE运行jar包（最终镜像）
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 从第一阶段复制打包好的jar包到当前镜像
COPY --from=builder /app/target/*.jar app.jar

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]