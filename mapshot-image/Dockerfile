FROM openjdk:11
#RUN mkdir /mapshot-image
#ADD mapshot-image/build/libs/mapshot-image.jar /mapshot-image
#CMD [ "java", "-jar", "/mapshot-image/mapshot-image.jar" ]

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY mapshot-image/src mapshot-image/src
COPY mapshot-image/build.gradle mapshot-image/build.gradle

COPY mapshot-slack/src mapshot-slack/src
COPY mapshot-slack/build.gradle mapshot-slack/build.gradle

RUN chmod +x ./gradlew
RUN ./gradlew run --args="install"
RUN ./gradlew run --args="install-deps"
RUN ./gradlew mapshot-image:bootJar


COPY mapshot-image/build/libs/mapshot-image.jar app.jar

CMD [ "java", "-jar", "/app.jar" ]
