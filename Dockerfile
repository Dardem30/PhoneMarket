FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/PhoneMarket-0.0.1-SNAPSHOT.jar /app

CMD ["java", "-jar", "PhoneMarket-0.0.1-SNAPSHOT.jar"]