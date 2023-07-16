FROM openjdk:19
ENV PORT 8080
EXPOSE 8080
COPY target/*.jar /app.jar
COPY src/main/resources/application.properties /app/application.properties
ENTRYPOINT ["java", "-jar", "app.jar"]