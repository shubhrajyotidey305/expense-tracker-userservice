# User Service
Spring Boot 3.5 service (Java 21) that stores and returns user profile data for the expense-tracker system. Data is persisted in MySQL via Spring Data JPA, and user events can also arrive through Kafka.

## What it does
- REST endpoints to create/update and fetch user profiles (`/user/v1/createUpdate`, `/user/v1/getUser`)
- JPA persistence of user records (userId, firstName, lastName, phoneNumber, email, optional profilePic)
- Kafka consumer for user events using a custom JSON deserializer

## Prerequisites
- Java 21
- Access to MySQL (default `mysql:3306`, database `userservice`, user `root`/`password`)
- Access to Kafka (default broker `kafka:9092`)
- Linux/macOS shell with the supplied Gradle wrapper (`./gradlew`)

## Configuration
Key properties (override via environment variables):
- `MYSQL_HOST` (default `mysql`)
- `MYSQL_PORT` (default `3306`)
- `MYSQL_DB` (default `userservice`)
- `spring.datasource.username` / `spring.datasource.password` (default `root` / `password`)
- `spring.kafka.topic-json.name` (default `user_service`)
- `spring.kafka.consumer.group-id` (default `userinfo-consumer-group`)
- Runs on port `9810`; JPA DDL auto is set to `create`, so schemas/tables are recreated on startup.

## Running locally
1) Ensure MySQL and Kafka are reachable with the configuration above.  
2) Start the service: `./gradlew bootRun`  
   - Build only: `./gradlew build` (artifacts land in `build/libs/`).  
3) Tests: `./gradlew test` (uses in-memory H2 and test Kafka settings).

## HTTP API
- `POST /user/v1/createUpdate`  
  Request body:  
  ```json
  {
    "userId": "auth-123",
    "firstName": "Ada",
    "lastName": "Lovelace",
    "phoneNumber": 1234567890,
    "email": "ada@example.com",
    "profilePic": "https://example.com/avatar.png"
  }
  ```  
  Response: `200 OK` with the stored user payload; `404` if an error occurs while saving.

- `GET /user/v1/getUser`  
  Request body (sent as JSON despite being a GET):  
  ```json
  { "userId": "auth-123" }
  ```  
  Response: `200 OK` with the matching user; `404` if not found.

## Kafka consumer
- Listener: `AuthServiceConsumer.listen`  
- Topic: `${spring.kafka.topic-json.name}` (default `user_service`)  
- Group: `${spring.kafka.consumer.group-id}` (default `userinfo-consumer-group`)  
- Payload: JSON matching `UserInfoDto` (fields shown above), deserialized by `UserInfoDeserializer`.

## Useful paths
- Application config: `src/main/resources/application.properties`
- Test config (H2): `src/test/resources/application.properties`
