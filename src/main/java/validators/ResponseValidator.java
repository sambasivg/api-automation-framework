package validators;

import io.restassured.response.Response;
import org.testng.Assert;

public final class ResponseValidator {
    private ResponseValidator() {
    }

    public static void assertJsonFieldEquals(Response response, String jsonPath, Object expectedValue) {
        Assert.assertEquals(response.jsonPath().get(jsonPath), expectedValue,
                "Unexpected JSON value at path: " + jsonPath);
    }

    public static void assertJsonFieldPresent(Response response, String jsonPath) {
        Assert.assertNotNull(response.jsonPath().get(jsonPath), "Missing JSON field at path: " + jsonPath);
    }
}