# Build stage
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

COPY src ./src
RUN ./mvnw clean package -DskipTests

# Final Stage
FROM eclipse-temurin:21-jre AS final

ARG UID=1001
ARG GID=1001

RUN groupadd --gid ${GID} spring \
    && useradd --uid ${UID} -g spring \
    --no-create-home --shell /usr/sbin/nologin spring

WORKDIR /app

COPY --from=build \
    --chown=spring:spring \
    --chmod=0444  \
     /app/target/app.jar  /app/app.jar

USER spring:spring

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "app.jar" ]
