FROM openjdk:8-jdk-alpine
MAINTAINER harpal.rtu@gmail.com
COPY news-app 0.0.1-SNAPSHOT.jar /app.jar
CMD ["java", "-jar", "-Dspring.profiles.active=default", "/app.jar"]