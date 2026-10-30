package validators;

import io.restassured.response.Response;
import org.testng.Assert;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public final class SchemaValidator {
    private SchemaValidator() {
    }

    public static void assertMatchesClasspathSchema(Response response, String schemaPath) {
        try {
            response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath));
        } catch (AssertionError error) {
            Assert.fail("Response did not match schema '" + schemaPath + "'", error);
        }
    }
}