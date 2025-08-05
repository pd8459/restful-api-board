FROM openjdk:17-jdk-alpine
COPY build/libs/board-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8080
