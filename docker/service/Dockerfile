#Build statge
FROM maven:3.9.8-amazoncorretto-21 AS build
WORKDIR /build
COPY . .
RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests

#Runtime Stage
FROM amazoncorretto:21
ARG PROFILE=local
ARG APP_VERSION=1.0-SNAPSHOT
ARG DS_DB_SERVER=farmbyte-postgresql
ARG DS_DB_PORT=5432
ARG DS_DB_NAME=farmbyte_db
ARG DS_DB_USER=farmbyte
ARG DS_DB_PASSWORD=01913eb8-019b-7bbc-9c32-3ab303ea4c9e
ARG APP_PORT=8090

WORKDIR /app
COPY --from=build /build/bootstrap/target/bootstrap-*.jar /app/

# Extract the JAR version
RUN APP_VERSION=$(ls /app | grep *.jar | awk 'NR==2{split($0,a,"-"); print a[3]}' | awk '{sub(/.jar$/,"")}1')\
    && echo "Building container with BSN v-$APP_VERSION"

ENV PACKAGE_VERSION=${APP_VERSION}
ENV ACTIVE_PROFILE=${PROFILE}
ENV DB_URL=jdbc:postgresql://${DS_DB_SERVER}:${DS_DB_PORT}/${DS_DB_NAME}
ENV DB_USER=${DS_DB_USER}
ENV DB_PASSWORD=${DS_DB_PASSWORD}
ENV EXPOSED_PORT=${APP_PORT}

EXPOSE ${EXPOSED_PORT}

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} \
    -Dspring.datasource.username=${DB_USER} -Dspring.datasource.password=${DB_PASSWORD} \
    bootstrap-${PACKAGE_VERSION}.jar

