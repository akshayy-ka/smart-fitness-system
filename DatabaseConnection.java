package database;

import java.sql.*;
import java.util.Properties;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/smart_fitness_app";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root"; // Change to your password

    private DatabaseConnection() {} // Private constructor

    /**
     * Provides a new database connection for each call.
     * This is the standard, safe approach for database operations.
     */
    public static Connection getConnection() throws SQLException {
        // This line explicitly loads the driver to prevent "No suitable driver" errors.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }

        Properties props = new Properties();
        props.setProperty("user", USERNAME);
        props.setProperty("password", PASSWORD);
        props.setProperty("useSSL", "false");
        props.setProperty("serverTimezone", "UTC");
        props.setProperty("allowPublicKeyRetrieval", "true");

        // Return a new connection instance directly
        return DriverManager.getConnection(URL, props);
    }
}