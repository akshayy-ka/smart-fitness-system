package models;

public class Goal {
    private int goalID;
    private String description;
    private double targetValue;
    private double currentValue;
    private String status;

    public Goal(int goalID, String description, double targetValue, double currentValue, String status) {
        this.goalID = goalID;
        this.description = description;
        this.targetValue = targetValue;
        this.currentValue = currentValue;
        this.status = status;
    }

    // Getters
    public int getGoalID() { return goalID; }
    public String getDescription() { return description; }
    public double getTargetValue() { return targetValue; }
    public double getCurrentValue() { return currentValue; }
    public String getStatus() { return status; }

    // This method is just for console output confirmation, used by the GUI
    public void setGoal() {
        System.out.println("Goal set: " + this.description);
    }
    
    // This method is called by the GUI to calculate progress
    public double checkProgress() {
        if (targetValue <= 0) return 0;
        double remaining = Math.abs(targetValue - currentValue);
        return (remaining / targetValue) * 100;
    }

    @Override
    public String toString() {
        return description + " (" + status + ")";
    }
}