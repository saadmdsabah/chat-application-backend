FROM openjdk:21

WORKDIR /app

COPY ./target/chat-application-backend-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "chat-application-backend-0.0.1-SNAPSHOT.jar"]