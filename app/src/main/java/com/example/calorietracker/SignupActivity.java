package com.example.calorietracker;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    private String BASE_URL = "";//"http://14.139.187.130:3000"; //
    private TextInputLayout textInputgender;
    private TextInputLayout textInputage;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private TextInputLayout textInputName;
    private Spinner spinner;

    //private MaterialButton google_sign_up;
    private Context context;

    private MaterialButton age_btn;

    private Dialog dialog;

    private int age_value = 0;

//    private GoogleSignInOptions gso;
//    private GoogleSignInClient gsc;


    private void create_dialog()
    {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.age_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button apply_btn = (Button) dialog.findViewById(R.id.apply_btn);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);

        NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.age_picker);

        String numval = "0";

        numberPicker.setMaxValue(1);
        numberPicker.setMinValue(0);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(100);

        numberPicker.setTextColor(Color.WHITE);

        ImageButton back_btn = findViewById(R.id.display_back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                int diff = value;
                return "" + diff;
            }
        };
        numberPicker.setFormatter(formatter);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                age_value = newVal;
                MaterialButton age_btn = findViewById(R.id.choose_age_btn);
                age_btn.setText("Choose Your Age: "+age_value);
            }
        });

        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.create();

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
        this.textInputgender = findViewById(R.id.gender_select);
        this.textInputage = findViewById(R.id.age_select);

        spinner = (Spinner) findViewById(R.id.gender_signup);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.age_btn = findViewById(R.id.choose_age_btn);

        this.age_btn.setText(this.age_btn.getText().toString()+": "+age_value+" yrs");

        create_dialog();

        this.age_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

//        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.age_picker);
//
//        String numval = "0";
//
//        numberPicker.setMaxValue(1);
//        numberPicker.setMinValue(0);
////        numberPicker2.setValue(0);
////
////        String[] vals = {"ml",""};
////
////        numberPicker2.setFormatter(new NumberPicker.Formatter() {
////            @Override
////            public String format(int value) {
////                // TODO Auto-generated method stub
////                return vals[value];
////            }
////        });
//
//        numberPicker.setMinValue(0);
//        numberPicker.setMaxValue(100);
//
//        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
//            @Override
//            public String format(int value) {
//                int diff = value * 10;
//                return "" + diff;
//            }
//        };
//        numberPicker.setFormatter(formatter);
//

//        AdapterView.OnItemSelectedListener OnCatSpinnerCL = new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
////                ((TextView) parent.getChildAt(0)).setTextSize(5);
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        };

//        this.google_sign_up = findViewById(R.id.google_signup_btn);
//
//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//
//        gsc = GoogleSignIn.getClient(this, gso);

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
                map.put("name",textInputName.getEditText().getText().toString());
                map.put("gender",spinner.getSelectedItem().toString());
                map.put("age",age_value+"");

                Call<Void> call = retrofitInterface.executeSignup(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response)
                    {
                        if(response.code() == 200)
                        {
                            Toast.makeText(getApplicationContext(),"SignUp Successful",Toast.LENGTH_LONG).show();
                            Intent newintent = new Intent(context,LoginActivity.class);
                            newintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(newintent);
                        }
                        else if(response.code() == 400){
                            Toast.makeText(context,"User Already Registered",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Unable to SignUp",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Unable to SignUp. Try Again",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


//        this.google_sign_up.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                google_sign_up_fn();
//            }
//        });

    }

//    private void google_sign_up_fn()
//    {
//        Intent intent = gsc.getSignInIntent();
//        startActivityForResult(intent,1000);
//    }

//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == 1000)
//        {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//
//            try {
//                task.getResult(ApiException.class);
//
//                HashMap<String,String> map = new HashMap<>();
//
//                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);
//
//                if(acct!=null) {
//
//                    String email = acct.getEmail();
//                    String name = acct.getDisplayName();
//
//                    map.put("email", email);
//                    map.put("name", name);
//
//                    Call<Void> call = retrofitInterface.executeSignup(map);
//
//                    call.enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, Response<Void> response)
//                        {
//                            if(response.code() == 200)
//                            {
//                                Toast.makeText(getApplicationContext(),"Sign In Sucessful",Toast.LENGTH_LONG).show();
//                                Intent newintent = new Intent(getApplicationContext(),MainActivity.class);
//                                startActivity(newintent);
//                            }
//                            else if(response.code() == 400)
//                            {
//                                Toast.makeText(getApplicationContext(),"User Already Registered",Toast.LENGTH_LONG).show();
//                            }
//                            else {
//                                Toast.makeText(getApplicationContext(),"Unable to SignUp",Toast.LENGTH_LONG).show();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//                            Toast.makeText(getApplicationContext(),"Unable to SignUp. Try Again",Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//                else
//                {
//                    throw new Exception("Hi");
//                }
//            }
//            catch(Exception e)
//            {
//                //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(),"Unable to Sign-In",Toast.LENGTH_LONG).show();
//            }
//        }
//        else
//        {
//            Toast.makeText(getApplicationContext(),"Unable to Sign-in",Toast.LENGTH_SHORT).show();
//        }
//
//    }

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






