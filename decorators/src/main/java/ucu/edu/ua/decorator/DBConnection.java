package ucu.edu.ua.decorator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBConnection {
    private static final String DB_URL = "jdbc:sqlite:cache.db";
    private static DBConnection instance;

    // Singleton pattern
    private DBConnection() {
        initializeDatabase();
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    private void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            // Create the cache table if it doesn't exist
            String createTableQuery = """
                    CREATE TABLE IF NOT EXISTS cache (
                        gcsPath TEXT PRIMARY KEY,
                        content TEXT NOT NULL
                    );
                    """;
            connection.createStatement().execute(createTableQuery);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public String getDocument(String gcsPath) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT content FROM cache WHERE gcsPath = ?")) {
            statement.setString(1, gcsPath);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("content");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to query cache", e);
        }
        return null; // Cache miss
    }

    public void createDocument(String gcsPath, String content) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO cache (gcsPath, content) VALUES (?, ?)")) {
            statement.setString(1, gcsPath);
            statement.setString(2, content);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Failed to store document in cache", e);
        }
    }
}
