FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY  target/bug-0.0.1.jar app.jar
EXPOSE 0.0.0.0:8004:8004
ENTRYPOINT ["java","-jar","/app.jar"]