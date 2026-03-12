# Build
FROM gitlab.uliege.be:5050/speam/2025-2026/team8/applego/gradle:9.1.0-jdk21-corretto AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle clean bootJar --no-daemon

# Runtime
FROM gitlab.uliege.be:5050/speam/2025-2026/team8/applego/eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
