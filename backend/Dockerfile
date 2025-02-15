FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/backend-0.0.1-SNAPSHOT.jar app.jar
ENV TZ=Asia/Seoul
CMD ["java", "-jar", "app.jar"]