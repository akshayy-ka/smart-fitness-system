package models;

public class Nutrition {
    private int nutritionID;
    private String foodItem;
    private double calorieIntake;
    private double protein;
    private double carbs;
    private double fats;

    public Nutrition(int nutritionID, String foodItem, double calorieIntake, double protein, double carbs, double fats) {
        this.nutritionID = nutritionID;
        this.foodItem = foodItem;
        this.calorieIntake = calorieIntake;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
    }

    // Getters
    public int getNutritionID() { return nutritionID; }
    public String getFoodItem() { return foodItem; }
    public double getCalorieIntake() { return calorieIntake; }
    public double getProtein() { return protein; }
    public double getCarbs() { return carbs; }
    public double getFats() { return fats; }

    // This method is just for console output confirmation, used by the GUI
    public void addMeal() {
        System.out.println("Meal logged: " + this.foodItem);
    }

    @Override
    public String toString() {
        return foodItem + " - " + calorieIntake + " kcal (P:" + protein + "g, C:" + carbs + "g, F:" + fats + "g)";
    }
}