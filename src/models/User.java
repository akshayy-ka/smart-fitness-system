package models;

public class User {
    private int userID;
    private String name;
    private int age;
    private String gender;
    private double height; // in cm
    private double weight; // in kg
    private String username;
    private String password;

    public User(int userID, String name, int age, String gender,
                double height, double weight, String username, String password) {
        this.userID = userID;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.username = username;
        this.password = password;
    }

    // Getters for all properties
    public int getUserID() { return userID; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public double getHeight() { return height; }
    public double getWeight() { return weight; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    // Business logic method to calculate BMI
    public double calculateBMI() {
        if (height <= 0) return 0;
        double heightInMeters = height / 100.0;
        return weight / (heightInMeters * heightInMeters);
    }
}