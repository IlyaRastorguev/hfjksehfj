FROM adoptopenjdk/openjdk13
ENV APP_HOME transport_app
ENV JAVA_OPTS = ""
ENV ENTRY_JAR_NAME = "transport_app"
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/logs
RUN mkdir $APP_HOME/h2
VOLUME $APP_HOME
WORKDIR $APP_HOME
ARG JAR_FILE
COPY ${JAR_FILE} ${ENTRY_JAR_NAME}.jar
ENTRYPOINT ["java", "-jar", "${ENTRY_JAR_NAME}.jar"]
EXPOSE 8081
# example build - docker build --build-arg JAR_FILE=target/*.jar -t transport_app .
# exambple run - docker run -d -p 8081:8081 -v /transport_app/logs:/transport_app/logs -v /transport_app/h2:/transport_app/h2 --name transport_app transport_app