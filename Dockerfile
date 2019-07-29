FROM openjdk:8-jdk-alpine
COPY target/enki-*-jar-with-dependencies.jar /app/enki.jar
ENTRYPOINT ["java","-jar","/app/enki.jar"]
