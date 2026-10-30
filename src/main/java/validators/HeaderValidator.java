package validators;

import io.restassured.response.Response;
import org.testng.Assert;

public final class HeaderValidator {
    private HeaderValidator() {
    }

    public static void assertHeaderContains(Response response, String headerName, String expectedValue) {
        String actualValue = response.getHeader(headerName);
        Assert.assertNotNull(actualValue, "Missing response header: " + headerName);
        Assert.assertTrue(actualValue.contains(expectedValue),
                "Expected header " + headerName + " to contain '" + expectedValue + "' but was '" + actualValue + "'");
    }
}