FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml pom.xml
COPY common-lib/pom.xml common-lib/pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml

RUN mvn -q -DskipTests dependency:go-offline -pl api-gateway -am

COPY common-lib/src common-lib/src
COPY api-gateway/src api-gateway/src

RUN mvn -q -DskipTests package -pl api-gateway -am

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/api-gateway/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
