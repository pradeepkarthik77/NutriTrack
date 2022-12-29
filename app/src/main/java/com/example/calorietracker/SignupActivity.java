package com.example.calorietracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity
{

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputreenter;
    private MaterialButton signup_btn;
    private String BASE_URL = "http://10.0.2.2:3000";

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private MaterialButton google_sign_up;
    private Context context;

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        this.context = this;

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        this.textInputEmail = findViewById(R.id.user_email_signup);
        this.textInputPassword = findViewById(R.id.user_password_signup);
        this.textInputreenter = findViewById(R.id.user_repassword_signup);
        this.signup_btn = findViewById(R.id.signup_btn_signup);

        this.google_sign_up = findViewById(R.id.google_signup_btn);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        gsc = GoogleSignIn.getClient(this, gso);

        this.signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //TODO add the logic to contact the backend and log the user into the system
                if(!validate_signup()){
                    return;
                }

                HashMap<String,String> map = new HashMap<>();

                map.put("email",textInputEmail.getEditText().getText().toString());
                map.put("password",textInputPassword.getEditText().getText().toString());

                Call<Void> call = retrofitInterface.executeSignup(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response)
                    {
                        Toast.makeText(getApplicationContext(),"Sign In Sucessful",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Unable to SignUp. Try Again",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


        this.google_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                google_sign_up_fn();
            }
        });

    }

    private void google_sign_up_fn()
    {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent,1000);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
            }
            catch(ApiException e)
            {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }

            Intent newintent = new Intent(context,MainActivity.class);
            startActivity(newintent);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
        }

    }

    private Boolean validate_signup()
    {
        String email_text  =  this.textInputEmail.getEditText().getText().toString().trim();

        boolean boole = true;

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

        String repass = this.textInputreenter.getEditText().getText().toString().trim();

        if(!repass.equals(password_text))
        {
            this.textInputreenter.setError("Passwords are not Matching");
            boole = false;
        }
        else
        {
            this.textInputreenter.setError(null);
            this.textInputreenter.setErrorEnabled(false);
        }

        return boole;

    }
}






