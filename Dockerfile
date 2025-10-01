# Uses the official OpenJDK 17 base image to provide a Java runtime environment
FROM openjdk:17

# Copies the packaged JAR file from the local target directory into the container's /tmp directory
COPY ./target/sem_assessment-0.1.0.2-jar-with-dependencies.jar /tmp
# This assumes the JAR was built using Maven with the maven-assembly-plugin

# Sets the working directory inside the container to /tmp
WORKDIR /tmp
# All subsequent commands will be run from this directory

# Defines the default command to run when the container starts
ENTRYPOINT ["java", "-jar", "sem_assessment-0.1.0.2-jar-with-dependencies.jar"]
# This launches the Java application using the bundled JAR

