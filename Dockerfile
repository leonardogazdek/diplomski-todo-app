FROM maven:3.8.3-openjdk-17 AS maven_build
WORKDIR /build
COPY . .
RUN mvn clean package

FROM amazoncorretto:17-alpine3.17-full
RUN mkdir -p /opt/todo
COPY --from=maven_build /build/target/*.jar /opt/todo/app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /opt/todo/app.jar"]
