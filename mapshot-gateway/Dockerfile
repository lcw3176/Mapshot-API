FROM openjdk:11
RUN mkdir /app
ADD build/libs/mapshot-gateway.jar /app
CMD [ "java", "-jar", "/app/mapshot-gateway.jar" ]