FROM openjdk:11
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

COPY mapshot-notice/src mapshot-notice/src
COPY mapshot-notice/build.gradle mapshot-notice/build.gradle

COPY mapshot-slack/src mapshot-slack/src
COPY mapshot-slack/build.gradle mapshot-slack/build.gradle

RUN chmod +x ./gradlew
RUN ./gradlew mapshot-notice:bootJar

COPY mapshot-notice/build/libs/mapshot-notice.jar app.jar

CMD [ "java", "-jar", "/app.jar" ]

#RUN mkdir /mapshot-notice
#ADD mapshot-notice/build/libs/mapshot-notice.jar /mapshot-notice
#CMD [ "java", "-jar", "/mapshot-notice/mapshot-notice.jar" ]
