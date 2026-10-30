package tests;

import base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ErrorHandlingTests extends BaseTest {
    @Test
    public void unknownUserReturnsNotFoundWithUsefulError() {
        Response response = apiClient.get("/users/999");

        Assert.assertEquals(response.statusCode(), 404);
        Assert.assertEquals(response.jsonPath().getString("error"), "user not found");
    }

    @Test
    public void protectedEndpointRejectsMissingToken() {
        Response response = apiClient.get("/protected");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("error"), "unauthorized");
    }
}
