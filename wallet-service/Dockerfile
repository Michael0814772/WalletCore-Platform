FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

ARG SERVICE_NAME

COPY pom.xml pom.xml
COPY common-lib/pom.xml common-lib/pom.xml
COPY auth-service/pom.xml auth-service/pom.xml
COPY wallet-service/pom.xml wallet-service/pom.xml
COPY transaction-service/pom.xml transaction-service/pom.xml
COPY notification-service/pom.xml notification-service/pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml

RUN mvn -q -DskipTests dependency:go-offline -pl ${SERVICE_NAME} -am

COPY common-lib/src common-lib/src
COPY ${SERVICE_NAME}/src ${SERVICE_NAME}/src

RUN mvn -q -DskipTests package -pl ${SERVICE_NAME} -am

FROM eclipse-temurin:21-jre-jammy AS runtime

ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -XX:+UseStringDeduplication"

RUN groupadd -g 10001 app && useradd -u 10001 -g app -m -s /usr/sbin/nologin app

WORKDIR /app

COPY --from=build /workspace/${SERVICE_NAME}/target/*.jar /app/app.jar

USER app

EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
