package database;

import models.Goal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GoalDAO {

    public GoalDAO() {
        // Constructor is now empty
    }

    public boolean addGoal(Goal goal, int userId) {
        String sql = "INSERT INTO goals (user_id, description, target_value, current_value, status) VALUES (?, ?, ?, ?, ?)";
        // Each method gets its own connection
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, goal.getDescription());
            stmt.setDouble(3, goal.getTargetValue());
            stmt.setDouble(4, goal.getCurrentValue());
            stmt.setString(5, goal.getStatus());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding goal: " + e.getMessage());
            return false;
        }
    }

    public List<Goal> getGoalsByUser(int userId) {
        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT * FROM goals WHERE user_id = ? ORDER BY created_at DESC";
        // Each method gets its own connection
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                goals.add(new Goal(
                        rs.getInt("goal_id"), rs.getString("description"), rs.getDouble("target_value"),
                        rs.getDouble("current_value"), rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting goals: " + e.getMessage());
        }
        return goals;
    }
}