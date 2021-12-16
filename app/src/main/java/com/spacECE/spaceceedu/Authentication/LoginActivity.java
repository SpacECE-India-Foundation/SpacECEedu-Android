package com.spacECE.spaceceedu.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.spacECE.spaceceedu.UsefulFunctions;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

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

        //what is this?
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        userLocalStore = new UserLocalStore(this);

        et_email = findViewById(R.id.editTextText_EmailAddress);
        et_password = findViewById(R.id.editTextText_Password);
        b_login = findViewById(R.id.Button_Login);
        tv_register = findViewById(R.id.TextView_Register);
        tv_invalid=findViewById(R.id.TextView_InvalidCredentials);



//        SignInButton G_signInButton = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    private void logIn(String email, String password) {

        String login = "http://spacefoundation.in/test/SpacECE-4460/spacece_auth/login_action.php";

        new Thread(new Runnable() {

            JSONObject jsonObject;

            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody fromBody = new FormBody.Builder()
                        .add("email", email)
                        .add("password", password)
                        .add("type", "customer")
                        .add("isAPI", "true")
                        .build();

                Request request = new Request.Builder()
                        .url(login)
                        .post(fromBody)
                        .build();


                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("Registration Error ApI " + e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                        try {
                            jsonObject = new JSONObject(response.body().string());
                            Log.d("Login", "onResponse: "+jsonObject.getString("status"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if(jsonObject.getString("status").equals("error")) {

                                        Log.i("Authentication:: ", "Rejected.....");
                                        et_email.setText("");
                                        et_email.setError("");
                                        et_password.setError("");
                                        et_password.setText("");
                                        tv_invalid.setVisibility(View.VISIBLE);

                                        Toast.makeText(getApplicationContext(), "Invalid email or password!", Toast.LENGTH_SHORT).show();

                                    } else if(jsonObject.getString("status").equals("success")) {

                                        JSONObject object = new JSONObject(jsonObject.getString("data"));

                                        Log.d("TAG", "onResponse: "+object);

                                        MainActivity.ACCOUNT= new Account(object.getString("current_user_id"), object.getString("current_user_name"),
                                                object.getString("current_user_mob"), object.getString("current_user_type").equals("consultant"),
                                                object.getString("current_user_image"));

                                        userLocalStore.storeUserData(MainActivity.ACCOUNT);
                                        tv_invalid.setVisibility(View.INVISIBLE);
                                        userLocalStore.setUserLoggedIn(true);
                                        Intent goToMainPage = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(goToMainPage);

                                        Toast.makeText(getApplicationContext(), "Welcome to SpacECE!", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }
}