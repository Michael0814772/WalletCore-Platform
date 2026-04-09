FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml pom.xml
COPY common-lib/pom.xml common-lib/pom.xml
COPY notification-service/pom.xml notification-service/pom.xml

RUN mvn -q -DskipTests dependency:go-offline -pl notification-service -am

COPY common-lib/src common-lib/src
COPY notification-service/src notification-service/src

RUN mvn -q -DskipTests package -pl notification-service -am

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/notification-service/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
