package database;

import models.Nutrition;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NutritionDAO {

    public NutritionDAO() {}

    public boolean addMeal(Nutrition nutrition, int userId, String mealTime) {
        String sql = "INSERT INTO nutrition (user_id, food_item, calorie_intake, protein, carbs, fats, meal_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, nutrition.getFoodItem());
            stmt.setDouble(3, nutrition.getCalorieIntake());
            stmt.setDouble(4, nutrition.getProtein());
            stmt.setDouble(5, nutrition.getCarbs());
            stmt.setDouble(6, nutrition.getFats());
            stmt.setString(7, mealTime);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding meal: " + e.getMessage());
            return false;
        }
    }

    public List<Nutrition> getMealsByUser(int userId) {
        List<Nutrition> meals = new ArrayList<>();
        String sql = "SELECT * FROM nutrition WHERE user_id = ? ORDER BY meal_date DESC, created_at DESC";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                meals.add(new Nutrition(
                        rs.getInt("nutrition_id"), rs.getString("food_item"), rs.getDouble("calorie_intake"),
                        rs.getDouble("protein"), rs.getDouble("carbs"), rs.getDouble("fats")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting meals: " + e.getMessage());
        }
        return meals;
    }
    
    public double[] getDailyNutritionTotals(int userId) {
        double[] totals = new double[4]; // [calories, protein, carbs, fats]
        String sql = "SELECT SUM(calorie_intake), SUM(protein), SUM(carbs), SUM(fats) FROM nutrition WHERE user_id = ? AND meal_date = CURRENT_DATE";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totals[0] = rs.getDouble(1);
                totals[1] = rs.getDouble(2);
                totals[2] = rs.getDouble(3);
                totals[3] = rs.getDouble(4);
            }
        } catch (SQLException e) {
            System.err.println("Error getting daily nutrition totals: " + e.getMessage());
        }
        return totals;
    }
}