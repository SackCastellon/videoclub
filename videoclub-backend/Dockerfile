FROM openjdk:8-jre-alpine

ENV APP_USER ktor
RUN adduser -D -g '' $APP_USER

RUN mkdir /app
RUN chown -R $APP_USER /app

USER $APP_USER

COPY ./build/libs/videoclub.jar /app/videoclub.jar
WORKDIR /app

CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:InitialRAMFraction=2", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=2", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "videoclub.jar"]
