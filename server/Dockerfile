FROM openjdk:21-jdk-slim AS build

RUN apt-get update && apt-get install -y maven
WORKDIR /server

COPY pom.xml ./
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -Pdebug

FROM openjdk:21-jdk-slim

COPY --from=build /server/target/*.jar server.jar

EXPOSE 5005
EXPOSE 8080

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005", "-jar", "/server.jar"]


