package client;

import config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class ApiClient {
    private final RequestSpecification requestSpecification;

    public ApiClient() {
        this(createDefaultSpecification());
    }

    public ApiClient(RequestSpecification requestSpecification) {
        if (requestSpecification == null) {
            throw new IllegalArgumentException("Request specification must not be null");
        }
        this.requestSpecification = requestSpecification;
    }

    public Response get(String endpoint) {
        return request().when().get(endpoint);
    }

    public Response get(String endpoint, Map<String, ?> queryParameters) {
        return request().queryParams(queryParameters).when().get(endpoint);
    }

    public Response post(String endpoint, Object body) {
        return request().body(body).when().post(endpoint);
    }

    public Response put(String endpoint, Object body) {
        return request().body(body).when().put(endpoint);
    }

    public Response patch(String endpoint, Object body) {
        return request().body(body).when().patch(endpoint);
    }

    public Response delete(String endpoint) {
        return request().when().delete(endpoint);
    }

    private RequestSpecification request() {
        return RestAssured.given().spec(requestSpecification);
    }

    private static RequestSpecification createDefaultSpecification() {
        return RestAssured.given()
                .baseUri(ConfigManager.getBaseUrl())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }
}