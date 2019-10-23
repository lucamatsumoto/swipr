# Create java 8 runtime
FROM openjdk:8-jdk-alpine

# Create a volume to maintain/persist container data
VOLUME /tmp

# Add the jar file to the container
ARG JAR=./target/swipr-0.0.1-SNAPSHOT.jar

COPY ${JAR} /swipr.jar

# expose port 8080 on the container
EXPOSE 8080

# execute the command in the container
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/swipr.jar"]
