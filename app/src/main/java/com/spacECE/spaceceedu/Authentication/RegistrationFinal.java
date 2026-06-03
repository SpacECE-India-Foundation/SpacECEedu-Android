//package com.spacECE.spaceceedu.Authentication;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.*;
//
//import androidx.activity.result.ActivityResult;
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.spacECE.spaceceedu.MainActivity;
//import com.spacECE.spaceceedu.R;
//
//import com.spacECE.spaceceedu.Utils.UsefulFunctions;
//import okhttp3.*;
//import org.jetbrains.annotations.NotNull;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.io.FileDescriptor;
//import java.io.IOException;
//import java.text.ParseException;
//
//public class RegistrationFinal extends AppCompatActivity {
//
//    private Button b_register;
//    private ImageView iv_profile_pic;
//    private EditText ev_email, ev_phoneNo, ev_password, ev_re_password, ev_name;
//    private TextView tv_login;
//    private boolean imageUpload = false;
//    private static final int PERMISSION_CODE = 1001;
//    private Uri picData = null;
//    Toolbar toolbar;
//    UserLocalStore userLocalStore;
//
//    String TYPE = "customer", LANGUAGE, ADDRESS, FEE,
//            QUALIFICATION, START_TIME, END_TIME;
//
//    ActivityResultLauncher<Intent> imagePickLauncher;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_registration);
//
//        userLocalStore = new UserLocalStore(getApplicationContext());
//
//        b_register = findViewById(R.id.UserRegistration_Button_Signup);
//        iv_profile_pic = findViewById(R.id.UserRegistration_ImageView_ProfilePic);
//
//        ev_email = findViewById(R.id.UserRegistration_editTextText_Email);
//        ev_password = findViewById(R.id.UserRegistration_editTextText_Password);
//        ev_re_password = findViewById(R.id.UserRegistration_editTextText_Re_Password);
//        ev_name = findViewById(R.id.UserRegistration_editTextText_Name);
//        ev_phoneNo = findViewById(R.id.UserRegistration_editTextText_PhoneNumber);
//        tv_login = findViewById(R.id.TextView_Register);
//
//        imagePickLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                            Intent data = result.getData();
//                            b_register.setText("Register");
//                            picData = data.getData();
//                            iv_profile_pic.setImageURI(picData);
//                            imageUpload = true;
//                        }
//                    }
//                }
//        );
//
//        Intent intent = getIntent();
//        if (intent.hasExtra("Type")) {
//            TYPE = intent.getStringExtra("Type");
//            LANGUAGE = intent.getStringExtra("Language");
//            ADDRESS = intent.getStringExtra("Address");
//            FEE = intent.getStringExtra("Fee");
//            QUALIFICATION = intent.getStringExtra("Qualification");
//            START_TIME = intent.getStringExtra("StartTime");
//            END_TIME = intent.getStringExtra("EndTime");
//        }
//
//        iv_profile_pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                            == PackageManager.PERMISSION_DENIED) {
//                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
//                        requestPermissions(permissions, PERMISSION_CODE);
//                    } else {
//                        pickImageFromGallery();
//                    }
//                } else {
//                    pickImageFromGallery();
//                }
//
//            }
//        });
//
//        b_register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (validateData()) {
//                    try {
//                        if (validTime(START_TIME, END_TIME)) {
//                            sendUserRegistration(ev_name.getText().toString(), ev_email.getText().toString(),
//                                    ev_password.getText().toString(), ev_phoneNo.getText().toString(), picData);
//                        } else {
//                            Toast.makeText(getApplicationContext(), "End Time must be greater than Start Time", Toast.LENGTH_LONG).show();
//                        }
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), "Check Details", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//        if (tv_login != null) {
//            tv_login.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(RegistrationFinal.this, LoginActivity.class));
//                    finish();
//                }
//            });
//        }
//
//        View contactUs = findViewById(R.id.CantactUs);
//        if (contactUs != null) {
//            contactUs.setOnClickListener(v -> openEmail());
//        }
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
//    private void pickImageFromGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        imagePickLauncher.launch(intent);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                pickImageFromGallery();
//            } else {
//                Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private boolean validTime(String fromTime, String endTime) throws ParseException {
//        if (fromTime == null || endTime == null) {
//            return true;
//        } else {
//            return UsefulFunctions.DateFunc.StringToTime(fromTime + ":00").before(UsefulFunctions.DateFunc.StringToTime(endTime + ":00"));
//        }
//    }
//
//    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
//        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
//        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
//        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
//        parcelFileDescriptor.close();
//        return image;
//    }
//
//    public static byte[] encodeBase64(Bitmap image) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
//        return byteArrayOutputStream.toByteArray();
//    }
//
//    private void sendUserRegistration(String name, String email, String password, String phone, Uri image) {
//        String register = "https://hustle-7c68d043.mileswebhosting.com/spacece/spacece_auth/register_action.php";
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                byte[] encodedImage = null;
//                if (image != null) {
//                    try {
//                        Bitmap selectedImage = getBitmapFromUri(image);
//                        encodedImage = encodeBase64(selectedImage);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                OkHttpClient client = UsefulFunctions.getOkHttpClient();
//                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                        .addFormDataPart("name", name)
//                        .addFormDataPart("email", email)
//                        .addFormDataPart("password", password)
//                        .addFormDataPart("phone", phone);
//
//                if (encodedImage != null) {
//                    builder.addFormDataPart("image", name + ".jpg",
//                            RequestBody.create(encodedImage, MediaType.parse("image/jpeg")));
//                }
//
//                if (TYPE != null && TYPE.equalsIgnoreCase("consultant")) {
//                    builder.addFormDataPart("type", "consultant")
//                            .addFormDataPart("c_categories", TYPE)
//                            .addFormDataPart("c_office", ADDRESS != null ? ADDRESS : "")
//                            .addFormDataPart("c_from_time", START_TIME != null ? START_TIME : "")
//                            .addFormDataPart("c_to_time", END_TIME != null ? END_TIME : "")
//                            .addFormDataPart("c_language", LANGUAGE != null ? LANGUAGE : "")
//                            .addFormDataPart("c_fee", FEE != null ? FEE : "")
//                            .addFormDataPart("c_available_from", "Monday")
//                            .addFormDataPart("c_available_to", "Tuesday")
//                            .addFormDataPart("c_qualification", QUALIFICATION != null ? QUALIFICATION : "");
//                } else {
//                    builder.addFormDataPart("type", "customer");
//                }
//
//                Request request = new Request.Builder()
//                        .url(register)
//                        .post(builder.build())
//                        .build();
//
//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                        Log.e("Registration", "Error: " + e.getMessage());
//                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Network error: " + e.getMessage(), Toast.LENGTH_LONG).show());
//                    }
//
//                    @Override
//                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                        final String responseData = response.body() != null ? response.body().string() : null;
//                        runOnUiThread(() -> {
//                            try {
//                                if (responseData == null || !response.isSuccessful()) {
//                                    Toast.makeText(getApplicationContext(), "Server error: " + response.code(), Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                Log.d("Registration", "Response: " + responseData);
//
//                                // Clean the response string if it contains HTML or extra spaces
//                                String cleanJson = responseData.trim();
//
//                                // Find the first { and last } to extract JSON even if there's surrounding garbage
//                                int jsonStart = cleanJson.indexOf("{");
//                                int jsonEnd = cleanJson.lastIndexOf("}");
//                                if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
//                                    cleanJson = cleanJson.substring(jsonStart, jsonEnd + 1);
//                                }
//
//                                if (!cleanJson.startsWith("{")) {
//                                    Toast.makeText(getApplicationContext(), "Server error: Invalid response format", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//
//                                JSONObject jsonObject = new JSONObject(cleanJson);
//
//                                if (jsonObject.optString("status").equals("error")) {
//                                    if (jsonObject.optString("message").equals("Email already exists!")) {
//                                        ev_email.setError("Email already exist!");
//                                    } else {
//                                        Toast.makeText(getApplicationContext(), jsonObject.optString("message", "Registration failed"), Toast.LENGTH_SHORT).show();
//                                    }
//                                } else if (jsonObject.optString("status").equals("success")) {
//                                    Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
//
//                                    // Save user data locally so header doesn't show "Hello null"
//                                    JSONObject data = jsonObject.optJSONObject("data");
//                                    String userId = (data != null) ? data.optString("current_user_id", "") : "";
//                                    String userName = (data != null) ? data.optString("current_user_name", name) : name;
//                                    Account account = new Account(userId, userName, phone, TYPE.equalsIgnoreCase("consultant"), "");
//                                    userLocalStore.setUserLoggedIn(true, account);
//                                    MainActivity.ACCOUNT = account;
//
//                                    // Redirect to MainActivity after successful registration
//                                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
//                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(mainIntent);
//                                    finish();
//                                }
//                            } catch (JSONException e) {
//                                Log.e("Registration", "JSON Error: " + e.getMessage() + " Response: " + responseData);
//                                Toast.makeText(getApplicationContext(), "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                });
//            }
//        }).start();
//    }
//
//    private boolean validateData() {
//        boolean vName = validateName();
//        boolean vPhone = validatePhone();
//        boolean vEmail = validateEmail();
//        boolean vPass = validatePass();
//        boolean vRepass = validateRepass();
//        return vName && vPhone && vEmail && vPass && vRepass;
//    }
//
//    private boolean validateEmail() {
//        String email = ev_email.getText().toString().trim();
//        if (email.isEmpty()) {
//            ev_email.setError("Field cannot be empty");
//            return false;
//        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            ev_email.setError("Invalid Email address");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateName() {
//        if (ev_name.getText().toString().trim().isEmpty()) {
//            ev_name.setError("Field cannot be empty");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validatePhone() {
//        if (ev_phoneNo.getText().toString().trim().isEmpty()) {
//            ev_phoneNo.setError("Field cannot be empty");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validatePass() {
//        if (ev_password.getText().toString().isEmpty()) {
//            ev_password.setError("Field cannot be empty");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateRepass() {
//        String pass = ev_password.getText().toString();
//        String rePass = ev_re_password.getText().toString();
//        if (rePass.isEmpty()) {
//            ev_re_password.setError("Field cannot be empty");
//            return false;
//        } else if (!pass.equals(rePass)) {
//            ev_re_password.setError("Passwords do not match");
//            return false;
//        }
//        return true;
//    }
//}
package com.spacECE.spaceceedu.Authentication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.*;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.activity.result.*;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.io.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationFinal extends AppCompatActivity {

    private Button b_register;
    private ImageView iv_profile_pic;
    private EditText ev_email, ev_phoneNo, ev_password, ev_re_password, ev_name;
    private TextView tv_login;
    private static final int PERMISSION_CODE = 1001;
    private Uri picData = null;
    UserLocalStore userLocalStore;

    String TYPE = "customer", LANGUAGE, ADDRESS, FEE,
            QUALIFICATION, START_TIME, END_TIME;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage storage;

    ActivityResultLauncher<Intent> imagePickLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        mAuth = FirebaseAuth.getInstance();
        // Force Firestore to use the correct project database
//        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                .build();
//        db = FirebaseFirestore.getInstance();
//        db.setFirestoreSettings(settings);
        // Explicitly initialize with project ID
        FirebaseApp app = FirebaseApp.getInstance();
        Log.d("REG_DEBUG", "FirebaseApp name: " + app.getName());
        Log.d("REG_DEBUG", "FirebaseApp projectId: " + app.getOptions().getProjectId());
        Log.d("REG_DEBUG", "FirebaseApp appId: " + app.getOptions().getApplicationId());
        db = FirebaseFirestore.getInstance();
        Log.d("REG_DEBUG", "Firestore instance: " + db.toString());
        storage = FirebaseStorage.getInstance();
        userLocalStore = new UserLocalStore(getApplicationContext());

        b_register = findViewById(R.id.UserRegistration_Button_Signup);
        iv_profile_pic = findViewById(R.id.UserRegistration_ImageView_ProfilePic);
        ev_email = findViewById(R.id.UserRegistration_editTextText_Email);
        ev_password = findViewById(R.id.UserRegistration_editTextText_Password);
        ev_re_password = findViewById(R.id.UserRegistration_editTextText_Re_Password);
        ev_name = findViewById(R.id.UserRegistration_editTextText_Name);
        ev_phoneNo = findViewById(R.id.UserRegistration_editTextText_PhoneNumber);
        tv_login = findViewById(R.id.TextView_Register);

        imagePickLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        picData = result.getData().getData();
                        iv_profile_pic.setImageURI(picData);
                    }
                }
        );

        Intent intent = getIntent();
        if (intent.hasExtra("Type")) {
            TYPE = intent.getStringExtra("Type");
            LANGUAGE = intent.getStringExtra("Language");
            ADDRESS = intent.getStringExtra("Address");
            FEE = intent.getStringExtra("Fee");
            QUALIFICATION = intent.getStringExtra("Qualification");
            START_TIME = intent.getStringExtra("StartTime");
            END_TIME = intent.getStringExtra("EndTime");
        }

        iv_profile_pic.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_CODE);
                } else {
                    pickImageFromGallery();
                }
            } else {
                pickImageFromGallery();
            }
        });

        b_register.setOnClickListener(v -> {
            if (validateData()) {
                try {
                    if (validTime(START_TIME, END_TIME)) {
                        registerUser(
                                ev_name.getText().toString().trim(),
                                ev_email.getText().toString().trim(),
                                ev_password.getText().toString(),
                                ev_phoneNo.getText().toString().trim()
                        );
                    } else {
                        Toast.makeText(this, "End time must be after start time", Toast.LENGTH_LONG).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Please check your details", Toast.LENGTH_LONG).show();
            }
        });

        if (tv_login != null)
            tv_login.setOnClickListener(v -> {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            });

        View contactUs = findViewById(R.id.CantactUs);
        if (contactUs != null)
            contactUs.setOnClickListener(v -> openEmail());
    }

    private void registerUser(String name, String email, String password, String phone) {
        b_register.setEnabled(false);
        b_register.setText("Registering...");

        Log.d("REG_DEBUG", "Step 1: Starting Auth registration for " + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("REG_DEBUG", "Step 1 FAILED: " + task.getException().getMessage());
                        b_register.setEnabled(true);
                        b_register.setText("Register");
                        String msg = task.getException() != null
                                ? task.getException().getMessage() : "Registration failed";
                        if (msg != null && msg.contains("email address is already in use")) {
                            ev_email.setError("Email already exists!");
                        } else {
                            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                        }
                        return;
                    }

                    String uid = mAuth.getCurrentUser().getUid();
                    Log.d("REG_DEBUG", "Step 1 SUCCESS: UID = " + uid);
                    Log.d("REG_DEBUG", "Step 2: picData = " + picData);
                    Log.d("REG_DEBUG", "Step 2: name=" + name + " phone=" + phone + " TYPE=" + TYPE);

                    if (picData != null) {
                        Log.d("REG_DEBUG", "Step 2: Going to uploadImageAndSave");
                        uploadImageAndSave(uid, name, email, phone);
                    } else {
                        Log.d("REG_DEBUG", "Step 2: Going directly to saveUserToFirestore");
                        saveUserToFirestore(uid, name, email, phone, "");
                    }
                });
    }

    private void uploadImageAndSave(String uid, String name, String email, String phone) {
        StorageReference ref = storage.getReference()
                .child("profile_pics/" + uid + ".jpg");

        ref.putFile(picData)
                .addOnSuccessListener(taskSnapshot ->
                        ref.getDownloadUrl()
                                .addOnSuccessListener(uri ->
                                        saveUserToFirestore(uid, name, email, phone, uri.toString()))
                                .addOnFailureListener(e -> {
                                    // Image URL failed — save without it
                                    Log.w("Storage", "URL fetch failed: " + e.getMessage());
                                    saveUserToFirestore(uid, name, email, phone, "");
                                }))
                .addOnFailureListener(e -> {
                    // Upload failed — still save user, just without image
                    Log.w("Storage", "Upload failed: " + e.getMessage());
                    saveUserToFirestore(uid, name, email, phone, "");
                });
    }

    private void saveUserToFirestore(String uid, String name, String email,
                                     String phone, String imageUrl) {
        Log.d("REG_DEBUG", "Step 3: Entered saveUserToFirestore for UID: " + uid);

        boolean isConsultant = TYPE != null && TYPE.equalsIgnoreCase("consultant");
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("email", email);
        userData.put("phone", phone);
        userData.put("profilePic", imageUrl);
        userData.put("isConsultant", isConsultant);
        userData.put("createdAt", FieldValue.serverTimestamp());

        if (isConsultant) {
            userData.put("category", TYPE);
            userData.put("office", ADDRESS != null ? ADDRESS : "");
            userData.put("fromTime", START_TIME != null ? START_TIME : "");
            userData.put("toTime", END_TIME != null ? END_TIME : "");
            userData.put("language", LANGUAGE != null ? LANGUAGE : "");
            userData.put("fee", FEE != null ? FEE : "");
            userData.put("qualification", QUALIFICATION != null ? QUALIFICATION : "");
        }

        Log.d("REG_DEBUG", "Step 3: userData built, calling Firestore write...");

        Log.d("REG_DEBUG", "Step 3: Calling Firestore set() now...");
        db.collection("users").document(uid)
                .set(userData)
                .addOnCompleteListener(task -> {
                    // This fires whether success or failure
                    Log.d("REG_DEBUG", "Step 3 COMPLETE: success=" + task.isSuccessful());
                    if (!task.isSuccessful() && task.getException() != null) {
                        Log.e("REG_DEBUG", "Step 3 ERROR: " + task.getException().getMessage());
                        Log.e("REG_DEBUG", "Step 3 ERROR class: " + task.getException().getClass().getName());
                    }
                })
                .addOnSuccessListener(unused -> {
                    Log.d("REG_DEBUG", "Step 3 SUCCESS: Firestore write done!");
                    Account account = isConsultant
                            ? new Account(uid, name, phone, true, imageUrl,
                            TYPE, ADDRESS, START_TIME, END_TIME, LANGUAGE, FEE, QUALIFICATION)
                            : new Account(uid, name, email, false, imageUrl);

                    userLocalStore.setUserLoggedIn(true, account);
                    MainActivity.ACCOUNT = account;

                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(this, MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("REG_DEBUG", "Step 3 FAILED: " + e.getMessage());
                    Log.e("REG_DEBUG", "Step 3 FAILED class: " + e.getClass().getName());
                    b_register.setEnabled(true);
                    b_register.setText("Register");
                    Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });}    // ---- All original helper methods below — unchanged ----

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validTime(String fromTime, String endTime) throws ParseException {
        if (fromTime == null || endTime == null) return true;
        return com.spacECE.spaceceedu.Utils.UsefulFunctions.DateFunc
                .StringToTime(fromTime + ":00")
                .before(com.spacECE.spaceceedu.Utils.UsefulFunctions.DateFunc
                        .StringToTime(endTime + ":00"));
    }

    private boolean validateData() {
        boolean vName = validateName();
        boolean vPhone = validatePhone();
        boolean vEmail = validateEmail();
        boolean vPass = validatePass();
        boolean vRepass = validateRepass();
        return vName && vPhone && vEmail && vPass && vRepass;
    }

    private boolean validateEmail() {
        String email = ev_email.getText().toString().trim();
        if (email.isEmpty()) { ev_email.setError("Field cannot be empty"); return false; }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ev_email.setError("Invalid email address"); return false;
        }
        return true;
    }

    private boolean validateName() {
        if (ev_name.getText().toString().trim().isEmpty()) {
            ev_name.setError("Field cannot be empty"); return false;
        }
        return true;
    }

    private boolean validatePhone() {
        if (ev_phoneNo.getText().toString().trim().isEmpty()) {
            ev_phoneNo.setError("Field cannot be empty"); return false;
        }
        return true;
    }

    private boolean validatePass() {
        if (ev_password.getText().toString().isEmpty()) {
            ev_password.setError("Field cannot be empty"); return false;
        }
        return true;
    }

    private boolean validateRepass() {
        String pass = ev_password.getText().toString();
        String rePass = ev_re_password.getText().toString();
        if (rePass.isEmpty()) { ev_re_password.setError("Field cannot be empty"); return false; }
        if (!pass.equals(rePass)) { ev_re_password.setError("Passwords do not match"); return false; }
        return true;
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
