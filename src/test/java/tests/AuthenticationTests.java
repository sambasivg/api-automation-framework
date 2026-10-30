package tests;

import auth.TokenManager;
import base.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthenticationTests extends BaseTest {
    @Test
    public void loginTokenCanAccessProtectedResource() {
        TokenManager tokenManager = new TokenManager(System.getProperty("api.baseUrl"));
        String token = tokenManager.authenticate("/auth/login",
                "{\"username\":\"demo-user\",\"password\":\"demo-password\"}");

        Response response = RestAssured.given()
                .spec(requestSpec)
                .auth()
                .oauth2(token)
                .when()
                .get("/protected");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("message"), "authenticated");
    }
}
