//package com.spacECE.spaceceedu.Authentication;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.*;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.app.AppCompatDelegate;
//
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.spacECE.spaceceedu.MainActivity;
//import com.spacECE.spaceceedu.R;
//
//import com.spacECE.spaceceedu.Utils.UsefulFunctions;
//import okhttp3.*;
//import org.jetbrains.annotations.NotNull;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//
//public class LoginActivity extends AppCompatActivity {
//
//    EditText et_email;
//    EditText et_password;
//    Button b_login;
//    TextView tv_register;
//    TextView tv_invalid;
//    ToggleButton is_Consultant;
//    TextView tv_forgotPassword;
//    View menu_icon;
//    View contact_us_footer;
//
//    String USER;
//
//    UserLocalStore userLocalStore;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//
//        userLocalStore = new UserLocalStore(getApplicationContext());
//
//        et_email = findViewById(R.id.editTextText_EmailAddress);
//        et_password = findViewById(R.id.editTextText_Password);
//        b_login = findViewById(R.id.Button_Login);
//        tv_register = findViewById(R.id.TextView_Register);
//        tv_invalid = findViewById(R.id.TextView_InvalidCredentials);
//        is_Consultant = findViewById(R.id.isConsultant);
//        tv_forgotPassword = findViewById(R.id.TextView_ForgotPassword);
//        menu_icon = findViewById(R.id.menu_icon);
//        contact_us_footer = findViewById(R.id.contact_us_footer);
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        b_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(is_Consultant != null && is_Consultant.isChecked()){
//                    USER = "consultant";
//                } else {
//                    USER = "customer";
//                }
//                String email = et_email.getText().toString().trim();
//                String password = et_password.getText().toString().trim();
//                if (email.isEmpty() || password.isEmpty()) {
//                    Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                logIn(email, password);
//            }
//        });
//
//        tv_register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(),RegistrationSelection.class));
//            }
//        });
//
//        tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                 try {
//                     Intent intent = new Intent();
//                     intent.setClassName(getPackageName(), "com.spacece.milestonetracker.ui.activity.ForgotPasswordActivity");
//                     startActivity(intent);
//                 } catch (Exception e) {
//                     Toast.makeText(LoginActivity.this, "Reset Password screen unavailable", Toast.LENGTH_SHORT).show();
//                 }
//            }
//        });
//
//        if (menu_icon != null) {
//            menu_icon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    openEmail();
//                }
//            });
//        }
//
//        if (contact_us_footer != null) {
//            contact_us_footer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    openEmail();
//                }
//            });
//        }
//    }
//
//    private void openUrl(String url) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse(url));
//        startActivity(intent);
//    }
//
//    private void openEmail() {
//        Intent intent = new Intent(Intent.ACTION_SENDTO);
//        intent.setData(Uri.parse("mailto:contact@spacece.in"));
//        try {
//            startActivity(intent);
//        } catch (Exception e) {
//            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void logIn(String email, String password) {
//        String login = "https://hustle-7c68d043.mileswebhosting.com/spacece/spacece_auth/login_action.php";
//        OkHttpClient client = UsefulFunctions.getOkHttpClient();
//        RequestBody fromBody = new FormBody.Builder()
//                .add("email", email)
//                .add("password", password)
//                .add("type", USER)
//                .add("isAPI", "true")
//                .build();
//        Request request = new Request.Builder()
//                .url(login)
//                .post(fromBody)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.e("Login", "API Error: " + e.getMessage());
//                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Network error: " + e.getMessage(), Toast.LENGTH_LONG).show());
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                final String responseData = response.body() != null ? response.body().string() : null;
//                runOnUiThread(() -> {
//                    try {
//                        if (responseData == null || !response.isSuccessful()) {
//                            Toast.makeText(LoginActivity.this, "Server error: " + response.code(), Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        Log.d("Login", "Response: " + responseData);
//
//                        String cleanJson = responseData.trim();
//                        int jsonStart = cleanJson.indexOf("{");
//                        int jsonEnd = cleanJson.lastIndexOf("}");
//                        if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
//                            cleanJson = cleanJson.substring(jsonStart, jsonEnd + 1);
//                        }
//
//                        if (!cleanJson.startsWith("{")) {
//                            Toast.makeText(LoginActivity.this, "Error: Invalid server response", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        JSONObject jsonObject = new JSONObject(cleanJson);
//                        if(jsonObject.optString("status").equals("error")) {
//                            et_password.setText("");
//                            tv_invalid.setVisibility(View.VISIBLE);
//                            Toast.makeText(LoginActivity.this, jsonObject.optString("message", "Invalid email or password!"), Toast.LENGTH_SHORT).show();
//                        } else if(jsonObject.optString("status").equals("success")) {
//                            JSONObject object = jsonObject.optJSONObject("data");
//                            if (object == null) {
//                                Toast.makeText(LoginActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            tv_invalid.setVisibility(View.INVISIBLE);
//                            if(object.optString("current_user_type").equals("consultant")){
//                                userLocalStore.setUserLoggedIn(true, new Account(object.optString("current_user_id"), object.optString("current_user_name"),
//                                        object.optString("current_user_mob"), object.optString("current_user_type").equals("consultant"),
//                                        object.optString("current_user_image"), object.optString("consultant_category"), object.optString("consultant_office"),
//                                        object.optString("consultant_from_time"), object.optString("consultant_to_time"), object.optString("consultant_language"),
//                                        object.optString("consultant_fee"), object.optString("consultant_qualification")));
//                            } else {
//                                userLocalStore.setUserLoggedIn(true, new Account(object.optString("current_user_id"), object.optString("current_user_name"),
//                                        object.optString("current_user_mob"), object.optString("current_user_type").equals("consultant"),
//                                        object.optString("current_user_image")));
//                            }
//                            MainActivity.ACCOUNT = userLocalStore.getLoggedInAccount();
//                            Intent goToMainPage = new Intent(getApplicationContext(), MainActivity.class);
//                            goToMainPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(goToMainPage);
//                            finish();
//                        } else {
//                            Toast.makeText(LoginActivity.this, "Unexpected response from server", Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (JSONException e) {
//                        Log.e("Login", "JSON Error: " + e.getMessage() + " Response: " + responseData);
//                        Toast.makeText(LoginActivity.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//    }
//}

package com.spacECE.spaceceedu.Authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;

public class LoginActivity extends AppCompatActivity {

    EditText et_email, et_password;
    Button b_login;
    TextView tv_register, tv_invalid, tv_forgotPassword;
    ToggleButton is_Consultant;
    View menu_icon, contact_us_footer;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userLocalStore = new UserLocalStore(getApplicationContext());

        et_email = findViewById(R.id.editTextText_EmailAddress);
        et_password = findViewById(R.id.editTextText_Password);
        b_login = findViewById(R.id.Button_Login);
        tv_register = findViewById(R.id.TextView_Register);
        tv_invalid = findViewById(R.id.TextView_InvalidCredentials);
        is_Consultant = findViewById(R.id.isConsultant);
        tv_forgotPassword = findViewById(R.id.TextView_ForgotPassword);
        menu_icon = findViewById(R.id.menu_icon);
        contact_us_footer = findViewById(R.id.contact_us_footer);

        b_login.setOnClickListener(v -> {
            String email = et_email.getText().toString().trim();
            String password = et_password.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }
            logIn(email, password);
        });

        tv_register.setOnClickListener(v ->
                startActivity(new Intent(this, RegistrationSelection.class)));

        tv_forgotPassword.setOnClickListener(v -> {
            String email = et_email.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Enter your email first", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(this, "Reset email sent to " + email, Toast.LENGTH_LONG).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
        });

        if (menu_icon != null)
            menu_icon.setOnClickListener(v -> openEmail());
        if (contact_us_footer != null)
            contact_us_footer.setOnClickListener(v -> openEmail());
    }

    @Override
    protected void onStart() {
        super.onStart();
        // If already logged in, skip login screen
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            fetchUserAndGo(currentUser.getUid());
        }
    }

    private void logIn(String email, String password) {
        b_login.setEnabled(false);
        b_login.setText("Logging in...");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        fetchUserAndGo(uid);
                    } else {
                        b_login.setEnabled(true);
                        b_login.setText("Login");
                        et_password.setText("");
                        tv_invalid.setVisibility(View.VISIBLE);
                        String msg = task.getException() != null
                                ? task.getException().getMessage()
                                : "Invalid email or password";
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Fetch user document from Firestore, rebuild Account, then go to MainActivity
    private void fetchUserAndGo(String uid) {
        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    Account account = buildAccountFromDoc(doc, uid);
                    userLocalStore.setUserLoggedIn(true, account);
                    MainActivity.ACCOUNT = account;

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    b_login.setEnabled(true);
                    b_login.setText("Login");
                    Toast.makeText(this, "Failed to load user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private Account buildAccountFromDoc(DocumentSnapshot doc, String uid) {
        String name = doc.getString("name");
        String phone = doc.getString("phone");
        String image = doc.getString("profilePic");
        Boolean isConsultant = doc.getBoolean("isConsultant");
        boolean consultant = isConsultant != null && isConsultant;

        if (consultant) {
            return new Account(
                    uid,
                    name,
                    phone,
                    true,
                    image,
                    doc.getString("category"),
                    doc.getString("office"),
                    doc.getString("fromTime"),
                    doc.getString("toTime"),
                    doc.getString("language"),
                    doc.getString("fee"),
                    doc.getString("qualification")
            );
        } else {
            return new Account(uid, name, phone, false, image);
        }
    }

    private void openEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:contact@spacece.in"));
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }
}
