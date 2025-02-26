# Build stage (Spring Boot)
FROM maven:3.9.6-amazoncorretto-21 AS backend-build
WORKDIR /app
COPY src ./src
COPY pom.xml .
RUN mvn clean package -DskipTests

# Build stage (React)
FROM node:18 AS frontend-build
WORKDIR /app
COPY news-aggregator-ui/package.json news-aggregator-ui/package-lock.json ./
RUN npm install
COPY news-aggregator-ui .
RUN npm run build

# Final image
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=backend-build /app/target/*.jar news-aggregator-application.jar
COPY --from=frontend-build /app/build /app/public
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "news-aggregator-application.jar"]