package com.example.calorietracker;

public class LoginResult
{
    private String email;

    private String name;

    private int age;

    private String gender;

    private int activity_level;
    private int goal_calorie;
    private int height;
    private int weight;

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }
    public String getName(){return name;}

    public int getActivity_level() {
        return activity_level;
    }

    public int getGoal_calorie() {
        return goal_calorie;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }
}
