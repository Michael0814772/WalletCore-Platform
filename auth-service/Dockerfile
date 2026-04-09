FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml pom.xml
COPY common-lib/pom.xml common-lib/pom.xml
COPY auth-service/pom.xml auth-service/pom.xml

RUN mvn -q -DskipTests dependency:go-offline -pl auth-service -am

COPY common-lib/src common-lib/src
COPY auth-service/src auth-service/src

RUN mvn -q -DskipTests package -pl auth-service -am

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/auth-service/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
