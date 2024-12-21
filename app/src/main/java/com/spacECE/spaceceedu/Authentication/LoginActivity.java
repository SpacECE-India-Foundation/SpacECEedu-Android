package com.spacECE.spaceceedu.Authentication;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    EditText et_email;
    EditText et_password;
    Button b_login;
    TextView tv_register;
    TextView tv_invalid;
    ToggleButton is_Consultant;
    TextView tv_forgotPassword;

    String USER;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        userLocalStore = new UserLocalStore(getApplicationContext());

        et_email = findViewById(R.id.editTextText_EmailAddress);
        et_password = findViewById(R.id.editTextText_Password);
        b_login = findViewById(R.id.Button_Login);
        tv_register = findViewById(R.id.TextView_Register);
        tv_invalid = findViewById(R.id.TextView_InvalidCredentials);
        is_Consultant = findViewById(R.id.isConsultant);
        tv_forgotPassword = findViewById(R.id.ForgetPassword);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_Consultant.isChecked()) {
                    USER = "consultant";
                } else {
                    USER = "customer";
                }

                logIn(et_email.getText().toString(), et_password.getText().toString());
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationSelection.class));
            }
        });

        tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPasswordDialog();
            }
        });
    }

    private void showForgotPasswordDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_forgot_password);

        EditText et_email = dialog.findViewById(R.id.editTextEmail);
        EditText et_mobile = dialog.findViewById(R.id.editTextMobile);
        Button btn_verify = dialog.findViewById(R.id.buttonVerify);

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String mobile = et_mobile.getText().toString().trim();

                if (email.isEmpty() || mobile.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                verifyUser(email, mobile, dialog);
            }
        });

        dialog.show();
    }

    private void verifyUser(String email, String mobile, Dialog dialog) {
        String url = "http://43.205.45.96/spacece_auth/api_userVerification.php";

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("u_mob", mobile)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Failed to verify user", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(() -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.has("error")) {
                            Toast.makeText(LoginActivity.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            showUpdatePasswordDialog(email, mobile);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Failed to verify user", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
    }

    private void showUpdatePasswordDialog(String email, String mobile) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update_password);

        EditText et_newPassword = dialog.findViewById(R.id.editTextNewPassword);
        Button btn_updatePassword = dialog.findViewById(R.id.buttonUpdatePassword);

        btn_updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = et_newPassword.getText().toString().trim();

                if (newPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter a new password", Toast.LENGTH_SHORT).show();
                    return;
                }

                updatePassword(email, mobile, newPassword, dialog);
            }
        });

        dialog.show();
    }

    private void updatePassword(String email, String mobile, String newPassword, Dialog dialog) {
        String url = "http://43.205.45.96/spacece_auth/api_updatePassword.php";

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("u_mob", mobile)
                .add("password", newPassword)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(() -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.has("error")) {
                            Toast.makeText(LoginActivity.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
    }

    public void logIn(String email, String password) {

        String login = "http://43.205.45.96/spacece_auth/login_action.php";

        new Thread(new Runnable() {

            JSONObject jsonObject;

            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody fromBody = new FormBody.Builder()
                        .add("email", email)
                        .add("password", password)
                        .add("type", USER)
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
                            Log.d("Login", "onResponse: "+jsonObject);
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

                                        Toast.makeText(LoginActivity.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();

                                    } else if(jsonObject.getString("status").equals("success")) {

                                        JSONObject object = new JSONObject(jsonObject.getString("data"));

                                        Log.d("TAG", "onResponse: "+object);

                                        tv_invalid.setVisibility(View.INVISIBLE);

                                        if(object.getString("current_user_type").equals("consultant")){
                                            Toast.makeText(LoginActivity.this, "Consultant!", Toast.LENGTH_SHORT).show();
                                            userLocalStore.setUserLoggedIn(true, new Account(object.getString("current_user_id"), object.getString("current_user_name"),
                                                    object.getString("current_user_mob"), object.getString("current_user_type").equals("consultant"),
                                                    object.getString("current_user_image"), object.getString("consultant_category"), object.getString("consultant_office "),
                                                    object.getString("consultant_from_time"), object.getString("consultant_to_time"), object.getString("consultant_language"),
                                                    object.getString("consultant_fee"), object.getString("consultant_qualification")));
                                        } else {
                                            userLocalStore.setUserLoggedIn(true, new Account(object.getString("current_user_id"), object.getString("current_user_name"),
                                                    object.getString("current_user_mob"), object.getString("current_user_type").equals("consultant"),
                                                    object.getString("current_user_image"), object.getString("current_user_email")));
                                            System.out.println(object.getString("current_user_image"));
                                        }
                                        MainActivity.ACCOUNT = userLocalStore.getLoggedInAccount();
                                        finish();
                                        Intent goToMainPage = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(goToMainPage);
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

