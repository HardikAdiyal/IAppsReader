# Use the official OpenJDK 17 image as the base image
FROM openjdk:17-jdk-slim

ARG APP_HOME=/opt/IAppsReader/

#create new dir
RUN mkdir ${APP_HOME}

#Copy Jar file from local to image
COPY target/IAppsReader.jar ${APP_HOME}/IAppsReader.jar

#change working dir to get into new folder (same as CD command)
WORKDIR ${APP_HOME}

# Expose the port your application runs on
EXPOSE 8080

#Run Jar application
ENTRYPOINT [ "java" , "-jar", "IAppsReader.jar"]