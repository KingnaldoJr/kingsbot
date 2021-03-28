# King's BOT
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=KingnaldoJr_kingsbot&metric=alert_status)](https://sonarcloud.io/dashboard?id=KingnaldoJr_kingsbot)
![fluxo de trabalho de exemplo](https://github.com/KingnaldoJr/kingsbot/actions/workflows/gradle.yml/badge.svg)

King's BOT is a multi proposal Discord BOT.
This BOT still in development, developed with [Discord4J](https://discord4j.com/) and [Spring Boot](https://spring.io/projects/spring-boot).

## Installation
Use **JDK 11** or higher to build.

Building the jar:
```gradle
gradlew bootJar
```

Set this environment variables:

| Variable                   | Value                                                                                     |
|----------------------------|-------------------------------------------------------------------------------------------|
| KINGSBOT_DISCORD_BOT_TOKEN | Discord BOT Token available on [Discord Developer Portal](https://discord.com/developers) |

Running the BOT:
```cmd
java -jar kingsbot.jar
```

## Commands
### Utils
| Command | Description            |
|---------|------------------------|
| ping    | A simple Ping command. |