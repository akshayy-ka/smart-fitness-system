package database;

import models.ProgressTracker;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgressTrackerDAO {

    public ProgressTrackerDAO() {
        // Constructor is now empty
    }

    public boolean addProgressEntry(ProgressTracker progress, int userId) {
        String sql = "INSERT INTO progress_tracker (user_id, weight, bmi) VALUES (?, ?, ?)";
        // Each method gets its own connection
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setDouble(2, progress.getWeight());
            stmt.setDouble(3, progress.getBmi());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding progress entry: " + e.getMessage());
            return false;
        }
    }

    public List<ProgressTracker> getProgressHistory(int userId) {
        List<ProgressTracker> history = new ArrayList<>();
        String sql = "SELECT * FROM progress_tracker WHERE user_id = ? ORDER BY progress_date DESC";
        // Each method gets its own connection
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                history.add(new ProgressTracker(
                        rs.getInt("progress_id"),
                        rs.getDate("progress_date").toString(),
                        rs.getDouble("weight"),
                        rs.getDouble("bmi")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting progress history: " + e.getMessage());
        }
        return history;
    }
}