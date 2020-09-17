FROM openjdk:14-jdk-alpine
RUN addgroup -S application && adduser -S application -G application
USER application:application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]