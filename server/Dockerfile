FROM openjdk:21-jdk-slim AS build

RUN apt-get update && apt-get install -y maven
WORKDIR /server

COPY pom.xml ./
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean install compile -Pdebug

FROM openjdk:21-jdk-slim

COPY --from=build /server/target/*.jar server.jar

EXPOSE 5001
EXPOSE 8080

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=debug", "/server.jar"]
CMD ["mvn", "spring-boot:run", "-f", "/server/pom.xml", "-Pdebug"]

#docker build -t app1 .
#docker run -p 8080:8080 -p 5005:5005  --rm --name llllllll   app1

#mvn spring-boot:run -f .\pom.xml