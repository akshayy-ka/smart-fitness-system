package models;

public class ProgressTracker {
    private int progressID;
    private String date;
    private double weight;
    private double bmi;

    public ProgressTracker(int progressID, String date, double weight, double bmi) {
        this.progressID = progressID;
        this.date = date;
        this.weight = weight;
        this.bmi = bmi;
    }

    // Getters for all properties
    public int getProgressID() { return progressID; }
    public String getDate() { return date; }
    public double getWeight() { return weight; }
    public double getBmi() { return bmi; }

    // Setters can be useful if you need to modify the object after creation
    public void setProgressID(int progressID) { this.progressID = progressID; }
    public void setDate(String date) { this.date = date; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setBmi(double bmi) { this.bmi = bmi; }

    @Override
    public String toString() {
        return "Date: " + date + ", Weight: " + weight + "kg, BMI: " + String.format("%.2f", bmi);
    }
}