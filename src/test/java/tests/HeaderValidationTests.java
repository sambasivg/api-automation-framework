package tests;

import base.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import validators.HeaderValidator;

public class HeaderValidationTests extends BaseTest {
    @Test
    public void responseIncludesJsonContentTypeAndTraceId() {
        Response response = apiClient.get("/users/1");

        HeaderValidator.assertHeaderContains(response, "Content-Type", "application/json");
        HeaderValidator.assertHeaderContains(response, "X-Trace-Id", "test-trace-001");
    }
}
