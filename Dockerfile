FROM adoptopenjdk/openjdk11:latestEXPOSE 8080

ARG APP_NAME="co2mission-api-0.0.1-SNAPSHOT"
ARG APP_VERSION=""
ARG ARTIFACT_URL=""
ARG BRANCH_NAME=""

ENV APP_PATH="/${APP_NAME}.jar" \
    SERVER_PORT=8090 \
    JVM_OPTIONS="-XX:+UseG1GC -XX:+PrintGC -XX:MaxGCPauseMillis=200 -XX:InitialRAMPercentage=25.0 -XX:MinRAMPercentage=25.0 -XX:MaxRAMPercentage=50.0"

EXPOSE $SERVER_PORT

#JVM_OPTIONS="-XX:+UseG1GC -XX:+PrintGC -Xms256m -Xmx512m -XX:MaxGCPauseMillis=200"



COPY wrapper.sh /wrapper.sh

RUN chmod 555 /wrapper.sh

# RUN wget -O $APP_PATH
COPY $APP_NAME.jar /$APP_NAME.jar


ENTRYPOINT ["/wrapper.sh"]