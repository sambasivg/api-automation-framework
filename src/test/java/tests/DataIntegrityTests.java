package tests;

import base.BaseTest;
import db.QueryExecutor;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import validators.DataIntegrityValidator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class DataIntegrityTests extends BaseTest {
    @Test
    public void userResponseContainsExpectedIdentityFields() {
        Response response = apiClient.get("/users/1");

        DataIntegrityValidator.assertJsonFieldEquals(response, "id", 1);
        DataIntegrityValidator.assertJsonFieldEquals(response, "name", "Ada Lovelace");
        DataIntegrityValidator.assertJsonFieldNotNull(response, "email");
    }

    @Test
    public void databaseQueryUsesBoundParametersAndMapsRows() throws Exception {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:integrity;DB_CLOSE_DELAY=-1");
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY, name VARCHAR(100))");
            statement.execute("DELETE FROM users");
            statement.execute("INSERT INTO users (id, name) VALUES (1, 'Ada Lovelace')");

            QueryExecutor queryExecutor = new QueryExecutor(connection);
            List<Map<String, Object>> rows = queryExecutor.query(
                    "SELECT id, name FROM users WHERE id = ?", 1);

            Assert.assertEquals(rows.size(), 1);
            Assert.assertEquals(((Number) rows.get(0).get("ID")).intValue(), 1);
            Assert.assertEquals(rows.get(0).get("NAME"), "Ada Lovelace");
        }
    }
}
