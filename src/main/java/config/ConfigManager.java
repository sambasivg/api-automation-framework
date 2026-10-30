package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {
    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = loadProperties();

    private ConfigManager() {
    }

    public static String getBaseUrl() {
        return getValue("api.baseUrl", "API_BASE_URL", "https://jsonplaceholder.typicode.com");
    }

    public static String getAccessToken() {
        return getValue("api.accessToken", "API_ACCESS_TOKEN", "");
    }

    public static String getDatabaseUrl() {
        return getValue("db.url", "DB_URL", "");
    }

    public static String getDatabaseUsername() {
        return getValue("db.username", "DB_USERNAME", "");
    }

    public static String getDatabasePassword() {
        return getValue("db.password", "DB_PASSWORD", "");
    }

    private static String getValue(String propertyName, String environmentName, String defaultValue) {
        String value = System.getProperty(propertyName);
        if (isSet(value)) {
            return value.trim();
        }
        value = System.getenv(environmentName);
        if (isSet(value)) {
            return value.trim();
        }
        value = PROPERTIES.getProperty(propertyName);
        return isSet(value) ? value.trim() : defaultValue;
    }

    private static boolean isSet(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load " + CONFIG_FILE, exception);
        }
        return properties;
    }
}