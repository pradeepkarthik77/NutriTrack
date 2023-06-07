package com.example.calorietracker;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface
{
    //in this interface we will encode the requests and parameters required for the application

    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String,String> map);

    @POST("/signup")
    Call<Void> executeSignup(@Body HashMap<String,String> map);

    @POST("/senddata")
    Call<Void> executesend(@Body HashMap<String,String> map);

    @POST("/imagesend")
    Call<Void> executeimage(@Body HashMap<String,String> map);

    @POST("/send_signup_details")
    Call<Void> executesignupdetails(@Body HashMap<String,String> map);

    @POST("/send_edit_profile")
    Call<Void> executeeditprofile(@Body HashMap<String,String> map);

    @POST("/send_bug_report")
    Call<Void> reportabug(@Body HashMap<String,String> map);

    @POST("/send_water_log")
    Call<Void> send_water_log(@Body JsonObject jsonObject);

    @POST("/send_goal_activity")
    Call<Void> sendgoalactivity(@Body HashMap<String,String> map);

}
