package models;

public class Workout {
    private int workoutID;
    private String type;        // e.g., "Squat", "Running"
    private int duration;       // in minutes
    private int sets;
    private int reps;
    private double weight;      // The new field, e.g., in kg
    private double caloriesBurned;

    // Updated Constructor
    public Workout(int workoutID, String type, int duration, int sets, int reps, double weight, double caloriesBurned) {
        this.workoutID = workoutID;
        this.type = type;
        this.duration = duration;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight; // Add this line
        this.caloriesBurned = caloriesBurned;
    }

    // Getters
    public int getWorkoutID() { return workoutID; }
    public String getType() { return type; }
    public int getDuration() { return duration; }
    public int getSets() { return sets; }
    public int getReps() { return reps; }
    public double getWeight() { return weight; } // The new getter
    public double getCaloriesBurned() { return caloriesBurned; }

    // This method is just for console output confirmation, used by the GUI
    public void addWorkout() {
        System.out.println("Workout logged: " + this.type);
    }

    @Override
    public String toString() {
        // Updated toString to include weight for strength exercises
        if (weight > 0) {
            return type + " - Sets: " + sets + ", Reps: " + reps + ", Weight: " + weight + " kg";
        } else {
            return type + " - " + duration + " mins, " + caloriesBurned + " kcal";
        }
    }
}