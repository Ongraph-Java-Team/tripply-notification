FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/notification-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "/app/app.jar"]