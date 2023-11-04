FROM maven:3.9-eclipse-temurin-17 AS builder
ADD ./pom.xml /petclinic-src/pom.xml
ADD ./src /petclinic-src/src
ADD ./mvnw /petclinic-src/mvnw
WORKDIR /petclinic-src
RUN mvn clean package -DskipTests=true

FROM eclipse-temurin:17-jre
COPY --from=builder /petclinic-src/target/spring-petclinic-3.1.0-SNAPSHOT.jar /petclinic.jar

EXPOSE 8080
CMD ["java", "-jar", "/petclinic.jar"]
