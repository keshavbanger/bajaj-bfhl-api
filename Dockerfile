## Stage 1: Build the JAR using official Maven + JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
# Pre-download dependencies (layer cached unless pom.xml changes)
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn clean package -DskipTests -q

## Stage 2: Run with slim JRE only
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/bfhl-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
