FROM openjdk:17-jdk-alpine

# 전체 프로젝트 복사
COPY . /app
WORKDIR /app

# Gradle 빌드 (테스트 생략)
RUN ./gradlew clean build -x test --no-daemon

# ✅ 빌드된 jar 파일을 직접 실행
ENTRYPOINT ["java", "-jar", "build/libs/board-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
