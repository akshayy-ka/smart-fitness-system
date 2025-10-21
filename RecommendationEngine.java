package models;

import database.UserDAO;
import database.WorkoutDAO;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate; // Assuming your Workout object will eventually have a date

/**
 * A sophisticated engine for generating personalized fitness and diet recommendations.
 * It uses user data and workout history to create tailored, text-based plans
 * while remaining compatible with the existing application structure.
 */
public class RecommendationEngine {

    // --- Constants for Diet Calculations ---
    private static final int WEIGHT_LOSS_CALORIE_DEFICIT = 400;
    private static final int MUSCLE_GAIN_CALORIE_SURPLUS = 300;
    private static final double PROTEIN_PER_KG_BODYWEIGHT = 1.6;

    /**
     * Internal enum to make goal-based logic safer and cleaner.
     */
    private enum FitnessGoal {
        WEIGHT_LOSS,
        MUSCLE_GAIN,
        GENERAL_FITNESS
    }

    private final User user;
    private final FitnessGoal goal;
    private final WorkoutDAO workoutDAO;

    /**
     * Constructs a RecommendationEngine for a specific user and their goal.
     *
     * @param user The currently logged-in User object. Cannot be null.
     * @param goalDescription A simple string describing the user's goal (e.g., "build muscle").
     */
    public RecommendationEngine(User user, String goalDescription) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        this.user = user;
        this.goal = determineGoalFromString(goalDescription);
        this.workoutDAO = new WorkoutDAO();
    }

    /**
     * Suggests a workout plan based on the user's goal.
     *
     * @return A String containing the workout recommendation.
     */
    public String suggestWorkoutPlan() {
        switch (goal) {
            case WEIGHT_LOSS:
                return "For weight loss, prioritize cardiovascular exercise (running, cycling) 3-4 times a week to maximize calorie burn. Complement this with 2 days of full-body strength training to preserve muscle mass.";
            case MUSCLE_GAIN:
                return "To build muscle, focus on a strength training split (e.g., Push/Pull/Legs) 4-5 times a week. Emphasize compound lifts like squats, deadlifts, and bench presses with progressive overload.";
            case GENERAL_FITNESS:
            default:
                return "For general fitness, a balanced routine is optimal. Aim for 3 days of total-body strength training and 2 days of moderate-intensity cardio (like jogging or swimming) to improve overall health.";
        }
    }

    /**
     * Suggests a diet plan with personalized calorie targets.
     *
     * @return A String containing the diet recommendation.
     */
    public String suggestDietPlan() {
        int maintenanceCalories = calculateBMR(user);
        int targetProtein = (int) (user.getWeight() * PROTEIN_PER_KG_BODYWEIGHT);

        switch (goal) {
            case WEIGHT_LOSS:
                int deficitCalories = maintenanceCalories - WEIGHT_LOSS_CALORIE_DEFICIT;
                return String.format("To lose weight, aim for a consistent calorie deficit of around %d kcal per day. Prioritize high-protein foods (approx. %dg/day) and fiber to promote satiety.", deficitCalories, targetProtein);
            case MUSCLE_GAIN:
                int surplusCalories = maintenanceCalories + MUSCLE_GAIN_CALORIE_SURPLUS;
                return String.format("For muscle gain, a slight calorie surplus is needed. Aim for %d kcal per day, with a high protein intake of about %dg to fuel muscle repair and growth.", surplusCalories, targetProtein);
            case GENERAL_FITNESS:
            default:
                return String.format("For general wellness, focus on a balanced diet of whole foods at maintenance calories (approx. %d kcal/day). Ensure a consistent protein intake of around %dg/day.", maintenanceCalories, targetProtein);
        }
    }

    /**
     * Analyzes the user's progress based on workout frequency and total calories burned.
     *
     * @return A String with a more insightful progress analysis.
     */
    public String analyzeProgress() {
        List<Workout> workouts = workoutDAO.getWorkoutsByUser(user.getUserID());

        if (workouts.isEmpty()) {
            return "Log some workouts to see your progress analysis here!";
        }
        
        // This part would be more accurate if the Workout object had a date.
        // For now, we analyze the whole list.
        long workoutCount = workouts.size();
        double totalCaloriesBurned = workouts.stream()
                .mapToDouble(Workout::getCaloriesBurned)
                .sum();
                
        String frequencyFeedback;
        if (workoutCount < 3) {
            frequencyFeedback = "Consistency is key; try to schedule at least 3 sessions per week.";
        } else if (workoutCount <= 5) {
            frequencyFeedback = "You're showing great consistency. Keep up the momentum!";
        } else {
            frequencyFeedback = "Incredible dedication! Ensure you are also scheduling adequate rest and recovery.";
        }

        return String.format("You've logged %d workout(s), burning a total of %.0f calories. %s", workoutCount, totalCaloriesBurned, frequencyFeedback);
    }

    /**
     * Determines the FitnessGoal enum from a user-provided string.
     * This internal method makes the public-facing logic cleaner.
     */
    private FitnessGoal determineGoalFromString(String description) {
        if (description == null) {
            return FitnessGoal.GENERAL_FITNESS;
        }
        String desc = description.toLowerCase();
        if (desc.contains("lose weight") || desc.contains("cardio") || desc.contains("burn fat")) {
            return FitnessGoal.WEIGHT_LOSS;
        } else if (desc.contains("build muscle") || desc.contains("strength") || desc.contains("bulk")) {
            return FitnessGoal.MUSCLE_GAIN;
        } else {
            return FitnessGoal.GENERAL_FITNESS;
        }
    }

    /**
     * Calculates the Basal Metabolic Rate (BMR) using the Mifflin-St Jeor equation.
     * BMR is the number of calories your body needs at rest.
     */
    private int calculateBMR(User u) {
        // BMR = 10 * weight (kg) + 6.25 * height (cm) - 5 * age (y) + s
        // (s is +5 for males, -161 for females)
        double bmr = (10 * u.getWeight()) + (6.25 * u.getHeight()) - (5 * u.getAge());
        if ("male".equalsIgnoreCase(u.getGender())) {
            bmr += 5;
        } else {
            bmr -= 161;
        }
        return (int) bmr;
    }
}