FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/car-service-0.0.1-SNAPSHOT.jar /app/car-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "car-service-0.0.1-SNAPSHOT.jar"]