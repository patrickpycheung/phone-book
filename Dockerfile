# <Build stage>

# Prepare the base container image for building the jar file from the source (Linux OS + Maven 3.6.3)
FROM maven:3.6.3-jdk-8 AS build

# Specify the directory to hold the source in the container, and cd to there
WORKDIR /opt/source/phone-book

# Copy local source to source folder in container
COPY src src

# Copy pom.xml to 
COPY pom.xml .

# Compile the jar file on the container
RUN mvn -f pom.xml clean package

# Save the path to the compiled jar file
#ARG JAR_FILE=/opt/source/phone-book/target/phoneBook-1.0.0-SNAPSHOT.jar


# <Package stage>

# Prepare the base container image for running the application (Linux OS + Java 8 runtime)
FROM openjdk:8-jdk-alpine

# Specify the directory to hold the jar in the container, and cd to there
WORKDIR /opt/app

COPY --from=build /opt/source/phone-book/target/phoneBook-1.0.0-SNAPSHOT.jar app.jar

# Run application in container
ENTRYPOINT ["java","-jar","app.jar"]