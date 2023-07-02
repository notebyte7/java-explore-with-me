FROM amazoncorretto:11-alpine-jdk
COPY ./stats-service-server/target/*.jar stats-service.jar
ENTRYPOINT ["java","-jar","/stats-service.jar"]