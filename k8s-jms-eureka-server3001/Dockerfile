#FROM java:8
FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
EXPOSE 10086
ENTRYPOINT ["java", "-XX:MetaspaceSize=200m", "-XX:MaxMetaspaceSize=200m", "-Xms50m", "-Xmx500m", "-Xss256k", "-XX:SurvivorRatio=8", "-XX:+UseSerialGC", "-jar", "/app.jar"]