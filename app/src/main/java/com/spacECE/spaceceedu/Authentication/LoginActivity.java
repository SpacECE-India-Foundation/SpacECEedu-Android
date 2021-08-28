package com.spacECE.spaceceedu.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.spacECE.spaceceedu.UsefulFunctions;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    public static Account ACCOUNT = null;
    EditText et_email;
    EditText et_password;
    Button b_login;
    TextView tv_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.editTextText_EmailAddress);
        et_password = findViewById(R.id.editTextText_Password);
        b_login = findViewById(R.id.Button_Login);
        tv_register = findViewById(R.id.TextView_Register);

        ACCOUNT= null;

        SignInButton G_signInButton = findViewById(R.id.sign_in_button);
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

//        tv_register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //startActivity(new Intent(this,RegisterChoice.class));
//            }
//        });

        G_signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    private void logIn(String email, String password) {

        Log.i("Authentication : Input Credentials : ", email + " / " + password);

        final JSONObject[] apiCall = {null};
        final boolean[] authenticationComplete = {false};

        Thread thread = new Thread(new Runnable() {


            @Override
            public void run() {
                TextView tv_invalidCredentials = findViewById(R.id.TextView_InvalidCredentials);

                try {
                    apiCall[0] = UsefulFunctions.UsingGetAPI("http://3.109.14.4/ConsultUs/api_getalluser?user=" + email);
                    Log.i("Object Obtained: ", apiCall[0].get("status").toString());
                    Log.i("Object Obtained: ", apiCall[0].toString());

                    try {
                        if (apiCall[0].get("status").toString().equalsIgnoreCase("success")) {
                            Log.i("Authentication: ", "User Found.....");
                            if (apiCall[0].getJSONArray("data").getJSONObject(0).get("password").toString().equals(password)) {
                                Log.i("Authentication: ", "Approved!!");

                                ACCOUNT = new Account(apiCall[0].getJSONObject("data").getString("email"),
                                        apiCall[0].getJSONObject("data").getString("name"),
                                        apiCall[0].getJSONObject("data").getString("phone")
                                        , false, apiCall[0].getJSONObject("data").getString("img")
                                        , apiCall[0].getString("UID"));
                                Log.i(" Data: ", ACCOUNT.toString());

                                authenticationComplete[0] = true;
                            } else {
                                et_email.setText("");
                                et_password.setText("");
                                authenticationComplete[0] = true;
                            }
                        } else {
                            et_email.setText("");
                            et_password.setText("");
                            authenticationComplete[0] = true;
                        }

                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                        Log.i("JSON: ", "Not Found or Invalid Credentials!!");
                        et_email.setText("");
                        et_password.setText("");
                        authenticationComplete[0] = true;
                    }
                } catch(RuntimeException e){
                    Log.i("Runtime Exception :::", "Connection error Or Server took too long to respond");
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getBaseContext(), "text", Toast.LENGTH_LONG).show();
                        }
                    });                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("JSON: ", "Not Found or Invalid Credentials!!");
                    et_email.setText("");
                    et_password.setText("");
                    authenticationComplete[0] = true;
                }
            }
        });

        thread.start();
        while (!authenticationComplete[0]) {
            Log.i("Authentication:: ", "Waiting......");
        }
        Log.i("Authentication:: ", "Completed......");

        if (ACCOUNT != null) {
            Log.i("Authentication:: ", "Approved Credentials received ...");
            Log.i("Authenticated User:: ", LoginActivity.ACCOUNT.toString());
            Intent goToMainPage = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(goToMainPage);
            return;
        }
        Log.i("Authentication:: ", "Rejected.....");
        return;
    }

    public void goToRegister(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View customToastLayout = inflater.inflate(R.layout.registration_selection_toast, (ViewGroup) findViewById(R.id.Registration_Selection_LinearLayout));

    }


}