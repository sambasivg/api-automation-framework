package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import validators.SchemaValidator;

public class SchemaValidationTests extends BaseTest {
    @Test
    public void userResponseMatchesJsonSchema() {
        SchemaValidator.assertMatchesClasspathSchema(apiClient.get("/users/1"), "schemas/user-schema.json");
    }
}
