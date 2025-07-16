FROM openjdk:21-slim
WORKDIR /app
COPY */shop-ttcs-0.0.1-SNAPSHOT.jar app.jar
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java", "-jar", "app.jar"]