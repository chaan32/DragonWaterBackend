# -------- Stage 1: Build --------
FROM --platform=$BUILDPLATFORM gradle:8.10.2-jdk21 AS builder
WORKDIR /app

# gradle 캐시 최적화
COPY build.gradle settings.gradle ./
COPY gradlew ./
COPY gradle gradle
RUN ./gradlew dependencies --no-daemon || true

# 소스 복사 후 빌드
COPY . .
RUN ./gradlew bootJar --no-daemon


# -------- Stage 2: Run --------
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]