# syntax=docker/dockerfile:1
FROM openjdk:11
WORKDIR /app
COPY ESUS-ENSEA-1-SNAPSHOT.jar ./
CMD ["java", "-jar", "ESUS-ENSEA-1-SNAPSHOT.jar"]