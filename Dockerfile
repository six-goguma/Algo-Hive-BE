# 1. JDK 21을 포함한 이미지를 사용하여 Java 애플리케이션을 실행할 수 있는 환경을 준비합니다.
FROM openjdk:21-jdk-slim

# 2. 작업 디렉토리를 설정합니다.
WORKDIR /app

# 3. Gradle 빌드 후 생성된 JAR 파일을 컨테이너 내의 /app 디렉토리로 복사합니다.
# Gradle 빌드 결과물이 `build/libs` 디렉토리에 저장됩니다.
COPY build/libs/your-app-name.jar /app/your-app-name.jar

# 4. 애플리케이션을 실행하는 명령어를 설정합니다.
CMD ["java", "-jar", "your-app-name.jar"]

# 5. 애플리케이션이 사용하는 포트를 설정합니다.
EXPOSE 8080
