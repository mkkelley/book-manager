FROM maven:3.6.3-openjdk-15 as build
COPY pom.xml .
COPY src src
RUN mvn --no-transfer-progress clean package

FROM openjdk:15-alpine
RUN apk --no-cache add curl
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=build target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]