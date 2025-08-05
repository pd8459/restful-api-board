FROM openjdk:17-jdk-alpine

# Gradle wrapper 포함시키기
COPY . /app
WORKDIR /app

# Gradle 빌드 실행
RUN ./gradlew clean build --no-daemon

# 빌드된 jar 복사
COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
