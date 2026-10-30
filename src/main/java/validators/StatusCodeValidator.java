package validators;

import io.restassured.response.Response;
import org.testng.Assert;

public final class StatusCodeValidator {
    private StatusCodeValidator() {
    }

    public static void assertStatusCode(Response response, int expectedStatusCode) {
        Assert.assertEquals(response.statusCode(), expectedStatusCode, "Unexpected HTTP status code");
    }
}