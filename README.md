# REST Assured API Automation Framework

A Java 8 and Maven framework for reusable REST API checks. It includes an API client, configurable environments, bearer-token support, response/schema/header/status/data-integrity validators, JDBC helpers, and TestNG examples. Example tests use WireMock so they run deterministically without calling a public API.

An equivalent Postman/Newman framework with its own examples and runnable mock API is maintained separately in [postman-api-framework](https://github.com/sambasivg/postman-api-framework).

## Prerequisites

- JDK 8 or newer
- Apache Maven 3.6+

## Run the suite

```powershell
mvn clean test
```

TestNG reports are written under `target/surefire-reports`.

## Configuration

Configuration precedence is JVM system property, environment variable, then `src/main/resources/config.properties`.

| Purpose | JVM property | Environment variable |
| --- | --- | --- |
| API base URL | `api.baseUrl` | `API_BASE_URL` |
| API access token | `api.accessToken` | `API_ACCESS_TOKEN` |
| JDBC URL | `db.url` | `DB_URL` |
| JDBC username | `db.username` | `DB_USERNAME` |
| JDBC password | `db.password` | `DB_PASSWORD` |

Example:

```powershell
$env:API_BASE_URL = "https://api.example.test"
mvn test
```

Do not commit real tokens, passwords, or other secrets.

## Framework usage

`ApiClient` provides `get`, `post`, `put`, `patch`, and `delete` methods. It accepts a Rest Assured `RequestSpecification` for reusable headers, authentication, base URI, and request defaults:

```java
RequestSpecification spec = new RequestSpecBuilder()
        .setBaseUri(ConfigManager.getBaseUrl())
        .setContentType(ContentType.JSON)
        .addHeader("Accept", "application/json")
        .build();

ApiClient client = new ApiClient(spec);
Response response = client.get("/users/1");
StatusCodeValidator.assertStatusCode(response, 200);
```

`TokenManager.authenticate(endpoint, credentials)` posts JSON credentials and returns the non-empty `token` from a successful JSON response. `QueryExecutor` runs parameterized SQL and maps result rows by column label; supply database connection details through the configuration above.

## Included examples

- GET/POST status-code checks and response data integrity
- JSON-schema and response-header validation
- Login followed by an OAuth2 bearer-token request
- 401/404 error handling
- H2-backed parameterized JDBC query example

The WireMock fixtures and lifecycle are in `src/test/java/base/BaseTest.java`; add test classes under `src/test/java/tests`.