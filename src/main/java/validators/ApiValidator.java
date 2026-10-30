package validators;

import io.restassured.response.Response;
import org.testng.Assert;

public final class ApiValidator {
    private ApiValidator() {
    }

    public static void verifySuccess(Response response) {
        Assert.assertTrue(response.statusCode() >= 200 && response.statusCode() < 300,
                "Expected a successful HTTP response but received " + response.statusCode());
    }
}