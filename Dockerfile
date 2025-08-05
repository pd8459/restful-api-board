FROM openjdk:17-jdk-alpine

COPY . /app
WORKDIR /app

# ✅ 테스트 생략하고 빌드
RUN ./gradlew clean build -x test --no-daemon

COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
