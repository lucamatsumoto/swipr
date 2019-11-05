# Two stage Pipeline: Build Stage
FROM maven:3.6.0-jdk-8-alpine AS maven

WORKDIR /swipr 

COPY pom.xml /swipr

RUN mvn dependency:go-offline

COPY src /swipr/src

RUN mvn -f /swipr/pom.xml clean package -DskipTests


# Package
# Create java 8 runtime
FROM openjdk:8-jdk-alpine

# Copy to main path
COPY --from=maven /swipr/target/swipr-0.0.1-SNAPSHOT.jar /swipr/swipr.jar

# Run the jar file. JVM sometimes bottlenecks on random number generation so use .urandom
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar","/swipr/swipr.jar"]  

