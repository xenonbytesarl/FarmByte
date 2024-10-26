#Build stage
FROM maven:3.9.8-amazoncorretto-21 AS build

VOLUME /root/farmbyte
WORKDIR /build
COPY . .
RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests=true

#Runtime Stage
FROM amazoncorretto:21

ARG PROFILE=local
ARG APP_VERSION=1.0-SNAPSHOT
ARG DS_DB_SERVER=missing_db_server
ARG DS_DB_NAME=missiong_db_name
ARG DS_DB_USER=missing_db_user
ARG DS_DB_PASSWORD=missing_db_password

WORKDIR /app
COPY --from=build /build/bootstrap/target/bootstrap-*.jar /app/

# Extract the JAR version

ENV APP_VERSION=${APP_VERSION}
ENV ACTIVE_PROFILE=local
ENV DB_USER=dev
ENV DB_PASSWORD=019142fc-f35d-7cf1-b893-24095a02405d
ENV DB_NAME=devb_db
ENV DB_SERVER=farmbyte-postgresql
ENV DB_PORT=5432
ENV APP_PORT=8080

EXPOSE ${APP_PORT}

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=jdbc:postgresql://${DB_SERVER}:${DB_PORT}/${DB_NAME} \
    -Dspring.datasource.username=${DB_USER} -Dspring.datasource.password=${DB_PASSWORD} \
    bootstrap-${APP_VERSION}.jar

