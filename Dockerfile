FROM openjdk:15-alpine as build
RUN sh mvnw package

FROM openjdk:15-alpine
RUN apk --no-cache add curl
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=build target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]