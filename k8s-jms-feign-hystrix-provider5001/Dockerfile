FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
EXPOSE 5001
ENTRYPOINT ["java", "-XX:MetaspaceSize=200m", "-XX:MaxMetaspaceSize=200m", "-Xms50m", "-Xmx500m", "-Xss256k", "-XX:SurvivorRatio=8", "-XX:+UseSerialGC", "-jar", "/app.jar"]