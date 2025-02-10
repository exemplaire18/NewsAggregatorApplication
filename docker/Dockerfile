# Build stage (Spring Boot)
FROM maven:3.8.6-openjdk-17 AS backend-build
WORKDIR /app
COPY ../src ./src
COPY ../pom.xml .
RUN mvn clean package -DskipTests

# Build stage (React)
FROM node:18 AS frontend-build
WORKDIR /app
COPY frontend/package.json frontend/package-lock.json ./
RUN npm install
COPY frontend .
RUN npm run build

# Final image
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar
COPY --from=frontend-build /app/build /app/public
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]