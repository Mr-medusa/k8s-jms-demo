# zipkin 下载地址
# http://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/
# docker build -t  hub.medusa.com/library/zipkin-server:2.12.9 .
FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY zipkin-server-2.12.9-exec.jar app.jar
EXPOSE 4001
ENTRYPOINT ["java", "-XX:MetaspaceSize=200m", "-XX:MaxMetaspaceSize=200m", "-Xms50m", "-Xmx500m", "-Xss256k", "-XX:SurvivorRatio=8", "-XX:+UseSerialGC", "-jar", "/app.jar"]