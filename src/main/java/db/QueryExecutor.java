package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QueryExecutor {
    private final Connection connection;

    public QueryExecutor(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Database connection must not be null");
        }
        this.connection = connection;
    }

    public List<Map<String, Object>> query(String sql, Object... parameters) throws SQLException {
        try (PreparedStatement statement = prepare(sql, parameters);
             ResultSet resultSet = statement.executeQuery()) {
            ResultSetMetaData metadata = resultSet.getMetaData();
            int columnCount = metadata.getColumnCount();
            List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
            while (resultSet.next()) {
                Map<String, Object> row = new LinkedHashMap<String, Object>();
                for (int column = 1; column <= columnCount; column++) {
                    row.put(metadata.getColumnLabel(column), resultSet.getObject(column));
                }
                rows.add(row);
            }
            return rows;
        }
    }

    public int executeUpdate(String sql, Object... parameters) throws SQLException {
        try (PreparedStatement statement = prepare(sql, parameters)) {
            return statement.executeUpdate();
        }
    }

    private PreparedStatement prepare(String sql, Object... parameters) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int index = 0; index < parameters.length; index++) {
            statement.setObject(index + 1, parameters[index]);
        }
        return statement;
    }
}