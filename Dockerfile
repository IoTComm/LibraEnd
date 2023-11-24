FROM openjdk:18-ea-jdk-slim
VOLUME /tmp
COPY build/libs/libra-end-0.0.1-SNAPSHOT.jar libra-end.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","libra-end.jar"]