FROM openjdk:11
EXPOSE 8081
ADD target/webflux-examen-devops-automation.jar webflux-examen-devops-automation.jar
ENTRYPOINT ["java", "-jar", "/webflux-examen-devops-automation.jar"]