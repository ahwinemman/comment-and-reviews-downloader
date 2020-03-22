FROM openjdk:8-jdk-alpine
MAINTAINER github.com/ahwinemman
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/comment-reviews-downloader.jar
COPY ${JAR_FILE} com-rev-dowloader.jar
ENTRYPOINT ["java","-jar","/com-rev-dowloader.jar"]