package tests;

import base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import validators.StatusCodeValidator;

public class StatusCodeTests extends BaseTest {
    @Test
    public void getExistingUserReturnsOk() {
        StatusCodeValidator.assertStatusCode(apiClient.get("/users/1"), 200);
    }

    @Test
    public void createUserReturnsCreated() {
        StatusCodeValidator.assertStatusCode(
                apiClient.post("/users", "{\"name\":\"Grace Hopper\",\"email\":\"grace@example.test\"}"), 201);
    }

    @Test
    public void updateAndPatchUserReturnOk() {
        Response putResponse = apiClient.put("/users/1",
                "{\"name\":\"Ada Updated\",\"email\":\"ada@example.test\"}");
        Response patchResponse = apiClient.patch("/users/1", "{\"role\":\"admin\"}");

        StatusCodeValidator.assertStatusCode(putResponse, 200);
        StatusCodeValidator.assertStatusCode(patchResponse, 200);
    }

    @Test
    public void deleteUserReturnsNoContent() {
        Assert.assertEquals(apiClient.delete("/users/1").statusCode(), 204);
    }

    @Test
    public void getCanSendQueryParameters() {
        Response response = apiClient.get("/users",
                java.util.Collections.<String, Object>singletonMap("role", "admin"));

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("[0].role"), "admin");
    }
}
