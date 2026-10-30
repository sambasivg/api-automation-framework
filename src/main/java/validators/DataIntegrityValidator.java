package validators;

import io.restassured.response.Response;
import org.testng.Assert;

public final class DataIntegrityValidator {
    private DataIntegrityValidator() {
    }

    public static void assertJsonFieldEquals(Response response, String jsonPath, Object expectedValue) {
        Object actualValue = response.jsonPath().get(jsonPath);
        Assert.assertEquals(actualValue, expectedValue,
                "Data integrity check failed at JSON path: " + jsonPath);
    }

    public static void assertJsonFieldNotNull(Response response, String jsonPath) {
        Assert.assertNotNull(response.jsonPath().get(jsonPath),
                "Expected a non-null value at JSON path: " + jsonPath);
    }
}