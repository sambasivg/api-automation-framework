package auth;

import config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class TokenManager {
    private final String baseUrl;

    public TokenManager() {
        this(ConfigManager.getBaseUrl());
    }

    public TokenManager(String baseUrl) {
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Base URL must not be blank");
        }
        this.baseUrl = baseUrl;
    }

    public String authenticate(String endpoint, Object credentials) {
        Response response = RestAssured.given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(credentials)
                .when()
                .post(endpoint);

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("Authentication failed with HTTP status "
                    + response.statusCode() + ": " + response.asString());
        }
        String token = response.jsonPath().getString("token");
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalStateException("Authentication response did not contain a non-empty 'token'");
        }
        return token;
    }

    public static String configuredAccessToken() {
        String token = ConfigManager.getAccessToken();
        if (token.isEmpty()) {
            throw new IllegalStateException("No API access token is configured. Set API_ACCESS_TOKEN or api.accessToken.");
        }
        return token;
    }
}