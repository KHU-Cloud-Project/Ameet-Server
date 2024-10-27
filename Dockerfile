FROM openjdk:17

ARG JAR_FILE=/build/libs/A-meet-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} /A-meet.jar

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod", "/A-meet.jar"]