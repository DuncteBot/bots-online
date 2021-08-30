FROM azul/zulu-openjdk-alpine:16 AS builder

WORKDIR /bots-online

COPY gradle ./gradle
COPY gradlew build.gradle.kts settings.gradle.kts ./
RUN ./gradlew --no-daemon dependencies
COPY . .
RUN ./gradlew --no-daemon build

FROM azul/zulu-openjdk-alpine:16-jre

WORKDIR /bots-online
COPY --from=builder /bots-online/build/libs/bots-online-1.0-SNAPSHOT-all.jar ./bots-online.jar

CMD ["java", "-jar", "bots-online.jar"]
