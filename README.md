# Bloodchain

Represents the backend application for Bloodchain, a platform where donors can schedule appointments and transfusion
centers can manage them, in a secure manner, using the Blockchain technology.

Created through [Spring Initializr](https://start.spring.io/).

## Local Setup

Prerequisites:
- Java programming language: JDK 21
- Build tool: Gradle
- Database: Postgres (when running the app with a profile other than `local`)

## Profiles

* `local`: uses the H2 in-memory database with sample data, for quick start up and testing
* `default` / none: uses the Postgres database, for permanent storage

## API

The list of all endpoints is available in [Swagger](http://localhost:8081/swagger-ui/index.html). Change the port if
the `server.port` property has a different value.





