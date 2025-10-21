package database;

import models.User;
import java.sql.*;

public class UserDAO {

    public UserDAO() {}

    public boolean createUser(User user) {
        String sql = "INSERT INTO users (name, age, gender, height, weight, username, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setInt(2, user.getAge());
            stmt.setString(3, user.getGender());
            stmt.setDouble(4, user.getHeight());
            stmt.setDouble(5, user.getWeight());
            stmt.setString(6, user.getUsername());
            stmt.setString(7, user.getPassword());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    public User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"), rs.getString("name"), rs.getInt("age"),
                        rs.getString("gender"), rs.getDouble("height"), rs.getDouble("weight"),
                        rs.getString("username"), rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        }
        return null;
    }
    
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET name = ?, age = ?, gender = ?, height = ?, weight = ? WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setInt(2, user.getAge());
            stmt.setString(3, user.getGender());
            stmt.setDouble(4, user.getHeight());
            stmt.setDouble(5, user.getWeight());
            stmt.setInt(6, user.getUserID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
}