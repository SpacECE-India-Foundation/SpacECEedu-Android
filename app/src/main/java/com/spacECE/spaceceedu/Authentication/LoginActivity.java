package com.spacECE.spaceceedu.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.spacECE.spaceceedu.UsefulFunctions;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    public Account ACCOUNT = null;
    EditText et_email;
    EditText et_password;
    Button b_login;
    TextView tv_register;
    TextView tv_invalid;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        userLocalStore = new UserLocalStore(this);

        et_email = findViewById(R.id.editTextText_EmailAddress);
        et_password = findViewById(R.id.editTextText_Password);
        b_login = findViewById(R.id.Button_Login);
        tv_register = findViewById(R.id.TextView_Register);
        tv_invalid=findViewById(R.id.TextView_InvalidCredentials);

        ACCOUNT= null;

//        SignInButton G_signInButton = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Input Values: ", et_email.getText() + "/" + et_password.getText().toString());
                logIn(et_email.getText().toString(), et_password.getText().toString());
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegistrationSelection.class));
            }
        });

//        G_signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }
//                    apiCall[0] = UsefulFunctions.UsingGetAPI("http://3.109.14.4/ConsultUs/api_getalluser?user=" + email);

    private void logIn(String email, String password) {

        Log.i("Authentication : Input Credentials : ", email + " / " + password);
        final boolean[] COMPLETED = {false};
        final JSONObject[] apiCall = {null};

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try{
                    apiCall[0] = UsefulFunctions.UsingGetAPI("http://3.109.14.4/ConsultUs/api_getalluser?user=" + email);
                    Log.i("Object Obtained::::::: ", apiCall[0].get("status").toString());

                    try {
                        if (apiCall[0].get("status").toString().equalsIgnoreCase("success")) {
                            Log.i("Authentication:::: ", "User Found.....3209e903uje93");
                            if (apiCall[0].getJSONArray("data").getJSONObject(0).get("password").toString().equals(password)) {
                                Log.i("Authentication:::::: ", "Approved!! 2390e93jej");

                                MainActivity.ACCOUNT = new Account(apiCall[0].getJSONArray("data").getJSONObject(0).getString("email"),
                                        apiCall[0].getJSONArray("data").getJSONObject(0).getString("name"),
                                        apiCall[0].getJSONArray("data").getJSONObject(0).getString("phone")
                                        , false, apiCall[0].getJSONArray("data").getJSONObject(0).getString("img")
                                        , apiCall[0].getJSONArray("data").getJSONObject(0).getString("UID"),apiCall[0].getJSONArray("data").getJSONObject(0).getString("token"));
                                Log.i(" Data::::: ", ACCOUNT.toString());

                                COMPLETED[0] = true;
                            } else {
                                COMPLETED[0] = true;
                            }
                        } else {
                            COMPLETED[0] = true;
                        }

                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                        Log.i("JSON: ", "Not Found or Invalid Credentials!!");
                        COMPLETED[0] = true;
                    }
                } catch(RuntimeException e){
                    Log.i("Runtime Exception :::", "Connection error Or Server took too long to respond");
                    COMPLETED[0]=true;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("JSON: ", "Not Found or Invalid Credentials!!");
                    COMPLETED[0] = true;
                }
            }
        });

        thread.start();
        while (!COMPLETED[0]) {
            Log.i("Authentication:: ", "Waiting......");
        }
        Log.i("Authentication:: ", "Completed......");

        if(MainActivity.ACCOUNT != null) {
            userLocalStore.storeUserData(MainActivity.ACCOUNT);
            tv_invalid.setVisibility(View.INVISIBLE);
            userLocalStore.setUserLoggedIn(true);
            Log.i("Authentication:: ", "Approved Credentials received ...");
            Log.i("Authenticated User:: ", MainActivity.ACCOUNT.toString());
            Intent goToMainPage = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(goToMainPage);
        }else{
            Log.i("Authentication:: ", "Rejected.....");
            et_email.setText("");
            et_email.setError("");
            et_password.setError("");
            et_password.setText("");
            tv_invalid.setVisibility(View.VISIBLE);
        }
    }

    public void goToRegister(View view) {
       startActivity(new Intent(this, RegistrationSelection.class));
    }


}