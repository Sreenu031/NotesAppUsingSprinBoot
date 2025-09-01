FROM ubuntu:latest
LABEL authors="medis"

FROM openjdk:23

WORKDIR /app
COPY target/My-app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java","-jar","app.jar"]


#ENTRYPOINT ["top", "-b"]