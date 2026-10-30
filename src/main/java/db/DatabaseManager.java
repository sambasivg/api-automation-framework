package db;

import config.ConfigManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseManager {
    private DatabaseManager() {
    }

    public static Connection getConnection() throws SQLException {
        String url = ConfigManager.getDatabaseUrl();
        if (url.isEmpty()) {
            throw new IllegalStateException("Database URL is not configured. Set DB_URL or db.url.");
        }
        return DriverManager.getConnection(url, ConfigManager.getDatabaseUsername(),
                ConfigManager.getDatabasePassword());
    }
}