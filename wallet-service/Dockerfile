FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml pom.xml
COPY common-lib/pom.xml common-lib/pom.xml
COPY wallet-service/pom.xml wallet-service/pom.xml

RUN mvn -q -DskipTests dependency:go-offline -pl wallet-service -am

COPY common-lib/src common-lib/src
COPY wallet-service/src wallet-service/src

RUN mvn -q -DskipTests package -pl wallet-service -am

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/wallet-service/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
