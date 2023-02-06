package com.example.calorietracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity
{

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private MaterialButton log_in_btn;
    
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = ""; //TODO UPDATE THIS VALUE AFTER SETTING UP BACKEND

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        this.BASE_URL = getString(R.string.BASE_URL);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        this.textInputEmail = findViewById(R.id.user_email_login);
        this.textInputPassword = findViewById(R.id.user_password_login);
        this.log_in_btn = findViewById(R.id.login_btn_login);

        ImageButton back_btn = findViewById(R.id.login_back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.log_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //TODO add the logic to contact the backend and log the user into the system
                if(!validate_login()) {
                    //Toast.makeText(getApplicationContext(),"All validated",Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String,String> map = new HashMap<>();

                map.put("email",textInputEmail.getEditText().getText().toString());
                map.put("password",textInputPassword.getEditText().getText().toString());

                Call<LoginResult> call = retrofitInterface.executeLogin(map);

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response)
                    {
                        //TODO code this after coding the server side

                        if(response.code() == 200)
                        {
                            LoginResult result = response.body();

                            SharedPreferences pref = getApplicationContext().getSharedPreferences("Login",0);

                            SharedPreferences.Editor editor = pref.edit();

                            editor.putBoolean("isLoggedin",true);
                            editor.putString("email",result.getEmail());
                            editor.putString("name",result.getName());
                            editor.putString("age",result.getAge());
                            editor.putString("gender",result.getGender());
                            editor.commit();

                            Toast.makeText(getApplicationContext(),"Log In Succesful!!!",Toast.LENGTH_LONG).show();
                            Intent newintent = new Intent(getApplicationContext(),HomeActivity.class);
                            newintent.putExtra("email",result.getEmail());
                            newintent.putExtra("name",result.getName());
                            newintent.putExtra("age",result.getAge());
                            newintent.putExtra("gender",result.getGender());
                            newintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(newintent);
                        }
                        else if(response.code() == 404)
                        {
                            Toast.makeText(getApplicationContext(),"Wrong Application Credentials",Toast.LENGTH_LONG).show();
                        }
                        else if(response.code() == 400)
                        {
                            Toast.makeText(getApplicationContext(),"This email is not registered with us yet!",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t)
                    {
                        Toast.makeText(getApplicationContext(),"Unable to Login. Try Again",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private Boolean validate_login()
    {
        String email_text  =  this.textInputEmail.getEditText().getText().toString().trim();

        Boolean  boole = true;

        if(email_text.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email_text).matches())
        {
            this.textInputEmail.setError("Enter a Valid Email-Id");
            boole = false;
        }
        else
        {
            this.textInputEmail.setError(null);
            this.textInputEmail.setErrorEnabled(false);
        }

        String password_text = this.textInputPassword.getEditText().getText().toString().trim();

        if(password_text.isEmpty() || password_text.length() <8)
        {
            this.textInputPassword.setError("Password length should not be less than 8 characters");
            boole = false;
        }
        else
        {
            this.textInputPassword.setError(null);
            this.textInputPassword.setErrorEnabled(false);
        }

        return boole;

    }
}






