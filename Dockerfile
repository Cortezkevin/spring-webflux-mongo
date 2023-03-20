FROM openjdk:17-alpine
EXPOSE 4000
ARG JAR_FILE=target/webflux-mongo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]