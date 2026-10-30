package base;

import client.ApiClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public abstract class BaseTest {
    protected WireMockServer mockServer;
    protected RequestSpecification requestSpec;
    protected ApiClient apiClient;
    private String previousBaseUrl;

    @BeforeMethod
    public void setUp() {
        previousBaseUrl = System.getProperty("api.baseUrl");
        mockServer = new WireMockServer(options().dynamicPort());
        mockServer.start();
        configureFor("localhost", mockServer.port());
        System.setProperty("api.baseUrl", "http://localhost:" + mockServer.port());
        stubCommonResponses();

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(ConfigManager.getBaseUrl())
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                .build();
        apiClient = new ApiClient(requestSpec);
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (mockServer != null) {
            mockServer.stop();
        }
        if (previousBaseUrl == null) {
            System.clearProperty("api.baseUrl");
        } else {
            System.setProperty("api.baseUrl", previousBaseUrl);
        }
    }

    private void stubCommonResponses() {
        stubFor(get(urlEqualTo("/users/1"))
                .willReturn(okJson("{\"id\":1,\"name\":\"Ada Lovelace\",\"email\":\"ada@example.test\"}")
                        .withHeader("X-Trace-Id", "test-trace-001")));
        stubFor(get(urlPathEqualTo("/users"))
                .withQueryParam("role", equalTo("admin"))
                .willReturn(okJson("[{\"id\":1,\"name\":\"Ada Lovelace\",\"role\":\"admin\"}]")));
        stubFor(post(urlEqualTo("/users"))
                .willReturn(aResponse().withStatus(201).withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":2,\"name\":\"Grace Hopper\",\"email\":\"grace@example.test\"}")));
        stubFor(put(urlEqualTo("/users/1"))
                .willReturn(okJson("{\"id\":1,\"name\":\"Ada Updated\",\"email\":\"ada@example.test\"}")));
        stubFor(patch(urlEqualTo("/users/1"))
                .willReturn(okJson("{\"id\":1,\"name\":\"Ada Lovelace\",\"role\":\"admin\"}")));
        stubFor(delete(urlEqualTo("/users/1"))
                .willReturn(aResponse().withStatus(204)));
        stubFor(post(urlEqualTo("/auth/login"))
                .willReturn(okJson("{\"token\":\"test-access-token\"}")));
        stubFor(get(urlEqualTo("/protected"))
                .withHeader("Authorization", equalTo("Bearer test-access-token"))
                .willReturn(okJson("{\"message\":\"authenticated\"}")));
        stubFor(get(urlEqualTo("/protected"))
                .atPriority(10)
                .willReturn(aResponse().withStatus(401).withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"unauthorized\"}")));
        stubFor(get(urlEqualTo("/users/999"))
                .willReturn(aResponse().withStatus(404).withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"user not found\"}")));
    }
}
