FROM openjdk:8-jre-slim
MAINTAINER MTI


ADD target/*.jar /app.jar
ADD *.yml /app/
ADD *.xml /app/
WORKDIR /app
EXPOSE 8080
ENV PARAMS=""

ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT ["nohup", "java", "-jar","./app.jar", "--spring.profiles.active=prd", "&"]