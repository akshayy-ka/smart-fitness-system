package database;

import models.Workout;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutDAO {

    public WorkoutDAO() {}

    public boolean addWorkout(Workout workout, int userId) {
        String sql = "INSERT INTO workouts (user_id, type, duration, sets_count, reps_count, weight, calories_burned) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, workout.getType());
            stmt.setInt(3, workout.getDuration());
            stmt.setInt(4, workout.getSets());
            stmt.setInt(5, workout.getReps());
            stmt.setDouble(6, workout.getWeight()); // Correctly saves weight
            stmt.setDouble(7, workout.getCaloriesBurned());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding workout: " + e.getMessage());
            return false;
        }
    }

    public List<Workout> getWorkoutsByUser(int userId) {
        List<Workout> workouts = new ArrayList<>();
        String sql = "SELECT * FROM workouts WHERE user_id = ? ORDER BY workout_date DESC, created_at DESC";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                workouts.add(new Workout(
                        rs.getInt("workout_id"), rs.getString("type"), rs.getInt("duration"),
                        rs.getInt("sets_count"), rs.getInt("reps_count"),
                        rs.getDouble("weight"), // Correctly reads weight
                        rs.getDouble("calories_burned")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting workouts: " + e.getMessage());
        }
        return workouts;
    }

    public List<Workout> getTodaysWorkouts(int userId) {
        List<Workout> workouts = new ArrayList<>();
        String sql = "SELECT * FROM workouts WHERE user_id = ? AND workout_date = CURRENT_DATE";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                 workouts.add(new Workout(
                        rs.getInt("workout_id"), rs.getString("type"), rs.getInt("duration"),
                        rs.getInt("sets_count"), rs.getInt("reps_count"),
                        rs.getDouble("weight"), // Correctly reads weight
                        rs.getDouble("calories_burned")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting today's workouts: " + e.getMessage());
        }
        return workouts;
    }
}