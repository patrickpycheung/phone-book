# Prepare the base container image for the application (Linux OS + Java 8 runtime)
FROM openjdk:8-jdk-alpine

# Add built jar location on local machine as an argument
ARG JAR_FILE=target/phoneBook-1.0.0-SNAPSHOT.jar

# Specify the location to store and run the application on the container, and cd to there
WORKDIR /opt/app

# Copy the built jar from local machine to container
COPY ${JAR_FILE} app.jar

# Run application on container
ENTRYPOINT ["java","-jar","app.jar"]