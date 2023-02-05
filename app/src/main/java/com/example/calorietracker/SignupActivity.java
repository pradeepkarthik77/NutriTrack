package com.example.calorietracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;
import androidx.loader.content.AsyncTaskLoader;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

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
    private TextInputLayout textInputName;
    private MaterialButton google_sign_up;
    private Context context;
    private ProgressBar progressBar;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    private String name="";
    private String email="";

    public void showspinner()
    {
        progressBar = findViewById(R.id.google_signup_progress);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hidespinner()
    {
        progressBar = findViewById(R.id.google_signup_progress);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        this.context = this;

        this.BASE_URL = getString(R.string.BASE_URL);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        this.textInputEmail = findViewById(R.id.user_email_signup);
        this.textInputPassword = findViewById(R.id.user_password_signup);
        this.textInputreenter = findViewById(R.id.user_repassword_signup);
        this.signup_btn = findViewById(R.id.signup_btn_signup);
        this.textInputName = findViewById(R.id.user_name_signup);

        this.google_sign_up = findViewById(R.id.google_signup_btn);

        this.progressBar = findViewById(R.id.google_signup_progress);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        gsc = GoogleSignIn.getClient(this, gso);

        ImageView back_btn = findViewById(R.id.signup_back_btn);

        back_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //this.progressBar = findViewById(R.id.google_signup_progress);

//        this.activityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result)
//                    {
//                        //TODO write logic for the result from takeaphoto
//                        int resultcode = result.getResultCode();
//
//                        if (resultcode == Activity.RESULT_OK)
//                        {
//                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
//
//                            try {
//                                task.getResult(ApiException.class);
//                                finish();
//                                Intent intent = new Intent(context,MainActivity.class);
//                                startActivity(intent);
//                            }
//                            catch(Exception e)
//                            {
//                                Toast.makeText(getApplicationContext(),"Cannot Sign-In using Google",Toast.LENGTH_LONG).show();
//                            }
//                        }
//                        else
//                        {
//                            Toast.makeText(context,"Sign In Unsuccessful",Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });



        this.signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //TODO add the logic to contact the backend and log the user into the system
                if(!validate_signup()){
                    return;
                }

                HashMap<String,String> map = new HashMap<>();

                map.put("name",textInputName.getEditText().getText().toString());
                map.put("email",textInputEmail.getEditText().getText().toString());
                map.put("password",textInputPassword.getEditText().getText().toString());
                map.put("name",textInputName.getEditText().getText().toString());
                map.put("gender",spinner.getSelectedItem().toString());
                map.put("age",age_value+"");

                class longthread extends Thread
                {
                    @Override
                    public void run() {
                        runOnUiThread(SignupActivity.this::showspinner);
                        Call<Void> call = retrofitInterface.executeSignup(map);

                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response)
                            {
                                if(response.code() == 200) {
                                    Toast.makeText(getApplicationContext(), "Sign In Successful", Toast.LENGTH_LONG).show();
                                }
                                else if(response.code() == 400)
                                {
                                    Toast.makeText(getApplicationContext(),"User Already Registered",Toast.LENGTH_LONG).show();
                                }
                                runOnUiThread(SignupActivity.this::hidespinner);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),"Unable to SignUp. Try Again",Toast.LENGTH_LONG).show();
                                runOnUiThread(SignupActivity.this::hidespinner);
                            }
                        });

                        //runOnUiThread(SignupActivity.this::hidespinner);
                    }
                }

                new longthread().start();

            }
        });

        this.google_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                google_sign_up_fn();
            }
        });


//        this.google_sign_up.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                google_sign_up_fn();
//            }
//        });

    }

    private void google_sign_up_fn()
    {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent,1000);
    }

    @Override
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

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
            if(account!=null)
            {
                this.name = account.getDisplayName();
                this.email = account.getEmail();
            }

            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();


        }
        else
        {
            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
        }

    }

    private Boolean validate_signup()
    {
        String email_text  =  this.textInputEmail.getEditText().getText().toString().trim();

        String name_text = this.textInputName.getEditText().getText().toString().trim();

        boolean boole = true;

        if(name_text.isEmpty() || name_text.length() <5)
        {
            this.textInputName.setError("Name cannot be less than 5 characters");
            boole = false;
        }
        else
        {
            this.textInputName.setError(null);
            this.textInputName.setErrorEnabled(false);
        }

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

        String gender_text = spinner.getSelectedItem().toString();

        if(gender_text.equals("Select Your Gender"))
        {
            this.textInputgender.setError("Choose a gender");
            boole = false;
        }
        else
        {
            this.textInputgender.setError(null);
            this.textInputgender.setErrorEnabled(false);
        }

        if(age_value == 0)
        {
            this.textInputage.setError("Choose a valid age");
            boole = false;
        }
        else
        {
            this.textInputage.setError(null);
            this.textInputage.setErrorEnabled(false);
        }

        String name_text = this.textInputName.getEditText().getText().toString().trim();

        if(name_text.isEmpty() || name_text.length() < 5)
        {
            this.textInputName.setError("Name should not be less than 5 characters");
            boole = false;
        }
        else
        {
            this.textInputName.setError(null);
            this.textInputName.setErrorEnabled(false);
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