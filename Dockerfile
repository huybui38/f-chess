FROM maven:3.5.2-jdk-8-alpine AS MAVEN_BUILD



COPY pom.xml /build/
WORKDIR /build/

RUN mvn dependency:go-offline

COPY src /build/src/

# build for release
RUN mvn package  -Dmaven.test.skip


FROM openjdk:8-jre-alpine

WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/FChess.jar /app/

ENTRYPOINT ["java", "-jar", "FChess.jar"]

EXPOSE 8888
EXPOSE 9092

