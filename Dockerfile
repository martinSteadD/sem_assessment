FROM openjdk:17
COPY ./target/sem_assessment-0.1.0.1-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "sem_assessment-0.1.0.1-jar-with-dependencies.jar"]
