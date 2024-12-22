package com.spacECE.spaceceedu.Authentication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.spacECE.spaceceedu.R;

import com.spacECE.spaceceedu.Utils.UsefulFunctions;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.ParseException;

public class RegistrationFinal extends AppCompatActivity {

    private Button b_register;
    private ImageView iv_profile_pic;
    private EditText ev_email, ev_phoneNo, ev_password, ev_re_password, ev_name;
    private boolean imageUpload = false;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private Uri picData = Uri.parse(String.valueOf(R.drawable.default_profilepic));
    private TextView uploadImageError;

    Toolbar toolbar;
    UserLocalStore userLocalStore;

    String TYPE = "customer", LANGUAGE, ADDRESS, FEE, QUALIFICATION, START_TIME, END_TIME, c_available_days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        userLocalStore = new UserLocalStore(getApplicationContext());

        b_register = findViewById(R.id.UserRegistration_Button_Signup);
        iv_profile_pic = findViewById(R.id.UserRegistration_ImageView_ProfilePic);

        ev_email = findViewById(R.id.UserRegistration_editTextText_Email);
        ev_password = findViewById(R.id.UserRegistration_editTextText_Password);
        ev_re_password = findViewById(R.id.UserRegistration_editTextText_Re_Password);
        ev_name = findViewById(R.id.UserRegistration_editTextText_Name);
        ev_phoneNo = findViewById(R.id.UserRegistration_editTextText_PhoneNumber);

        uploadImageError = findViewById(R.id.uploadImageError);

        Intent intent = getIntent();
        TYPE = intent.getStringExtra("Type");
        LANGUAGE = intent.getStringExtra("Language");
        ADDRESS = intent.getStringExtra("Address");
        FEE = intent.getStringExtra("Fee");
        QUALIFICATION = intent.getStringExtra("Qualification");
        START_TIME = intent.getStringExtra("StartTime");
        END_TIME = intent.getStringExtra("EndTime");
        c_available_days = intent.getStringExtra("c_available_days");

        Log.d("RegistrationFinal", "TYPE: " + TYPE);
        Log.d("RegistrationFinal", "LANGUAGE: " + LANGUAGE);
        Log.d("RegistrationFinal", "ADDRESS: " + ADDRESS);
        Log.d("RegistrationFinal", "FEE: " + FEE);
        Log.d("RegistrationFinal", "QUALIFICATION: " + QUALIFICATION);
        Log.d("RegistrationFinal", "START_TIME: " + START_TIME);
        Log.d("RegistrationFinal", "END_TIME: " + END_TIME);
        Log.d("RegistrationFinal", "c_available_days: " + c_available_days);

        Log.d("TAG", "onCreate: " + TYPE + " " + LANGUAGE + " " + ADDRESS + " " + FEE + " " + QUALIFICATION + " " + START_TIME + " " + END_TIME);

        // Set onClickListener for profile picture selection
        iv_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check runtime permission for reading external storage
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        // Request permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        // Permission already granted
                        pickImageFromGallery();
                    }
                } else {
                    // System OS is less than marshmallow
                    pickImageFromGallery();
                }
            }
        });

        // Set onClickListener for registration button
        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()) {
                    try {
                        if (validTime(START_TIME, END_TIME)) {
                            sendUserRegistration(ev_name.getText().toString(), ev_email.getText().toString(),
                                    ev_password.getText().toString(), ev_phoneNo.getText().toString(), picData);
                        } else {
                            Toast.makeText(getApplicationContext(), "End Time must be greater than Start Time", Toast.LENGTH_LONG).show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Check Details", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Method to pick an image from the gallery
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    // Handle the result of permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                pickImageFromGallery();
            } else {
                // Permission was denied
                Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Handle the result of image picking
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            // Set image to image view
            b_register.setText("Register");
            picData = data.getData();
            iv_profile_pic.setImageURI(data.getData());
        }
    }

    // Validate if the end time is greater than the start time
    private boolean validTime(String fromTime, String endTime) throws ParseException {
        if (fromTime == null && endTime == null) {
            return true;
        } else {
            return UsefulFunctions.DateFunc.StringToTime(fromTime + ":00").before(UsefulFunctions.DateFunc.StringToTime(endTime + ":00"));
        }
    }

    // Get Bitmap from Uri
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    // Encode Bitmap image to byte array
    public static byte[] encodeBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    // Send user registration data to the server
    private void sendUserRegistration(String name, String email, String password, String phone, Uri image) {
        String register = "http://13.126.66.91/spacece/spacece_auth/register_action.php";

        new Thread(new Runnable() {

            JSONObject jsonObject;
            Bitmap selectedImage;
            byte[] encodedImage = {5};

            @Override
            public void run() {
                try {
                    selectedImage = getBitmapFromUri(image);
                    encodedImage = encodeBase64(selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (encodedImage.length == 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!imageUpload) {
                                Toast.makeText(getApplicationContext(), "Upload Image", Toast.LENGTH_SHORT).show();
                                uploadImageError.setVisibility(View.VISIBLE);
                                b_register.setText("Continue");
                                imageUpload = true;
                            }
                        }
                    });
                    if (!imageUpload) {
                        return;
                    }
                }

                OkHttpClient client = new OkHttpClient();
                RequestBody formBody;

                if (TYPE != null && LANGUAGE != null && ADDRESS != null && FEE != null && QUALIFICATION != null && START_TIME != null && END_TIME != null) {
                    // Split the selected days string into an array
                    String[] selectedDaysArray = c_available_days.split(",");

                    formBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("name", name)
                            .addFormDataPart("email", email)
                            .addFormDataPart("password", password)
                            .addFormDataPart("phone", phone)
                            .addFormDataPart("image", name + ".jpg", RequestBody.create(MediaType.parse("image/*jpg"), encodedImage))
                            .addFormDataPart("type", "consultant")
                            .addFormDataPart("c_categories", TYPE)
                            .addFormDataPart("c_office", ADDRESS)
                            .addFormDataPart("c_from_time", START_TIME)
                            .addFormDataPart("c_to_time", END_TIME)
                            .addFormDataPart("c_language", LANGUAGE)
                            .addFormDataPart("c_fee", FEE)
                            .addFormDataPart("selectedItem", String.join(",", selectedDaysArray))
                            .addFormDataPart("c_qualification", QUALIFICATION)
                            .build();
                } else {
                    formBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("name", name)
                            .addFormDataPart("email", email)
                            .addFormDataPart("password", password)
                            .addFormDataPart("phone", phone)
                            .addFormDataPart("image", name + ".jpg", RequestBody.create(MediaType.parse("image/*jpg"), encodedImage))
                            .addFormDataPart("type", "customer")
                            .build();
                }

                Request request = new Request.Builder()
                        .url(register)
                        .post(formBody)
                        .build();
                Log.d("Registration URL", request.url().toString());

                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("Registration Error API", e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        try {
                            jsonObject = new JSONObject(response.body().string());
                            Log.d("TAG", "onResponse: " + jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.d("TAG", "onResponse: " + jsonObject.getString("status"));
                                    if (jsonObject.getString("status").equals("error")) {
                                        if (jsonObject.getString("message").equals("Email already exists!")) {
                                            ev_email.setError("Email already exists!");
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Please try after some time!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (jsonObject.getString("status").equals("success")) {
                                        Toast.makeText(getApplicationContext(), "Welcome to SpacECE! Login to continue!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
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

    // Validate all input data
    private boolean validateData() {
        return validateName() && validatePhone() && validateEmail() && validatePass() && validateRepass();
    }

    // Validate email input
    private boolean validateEmail() {
        if (ev_email.getText().toString().isEmpty()) {
            ev_email.setError("Field cannot be empty");
            return false;
        } else if (!ev_email.getText().toString().contains("@")) {
            ev_email.setError("Invalid Email address");
            return false;
        }
        return true;
    }

    // Validate name input
    private boolean validateName() {
        if (ev_name.getText().toString().isEmpty()) {
            ev_name.setError("Field cannot be empty");
            return false;
        }
        return true;
    }

    // Validate phone input
    private boolean validatePhone() {
        if (ev_phoneNo.getText().toString().isEmpty()) {
            ev_phoneNo.setError("Field cannot be empty");
            return false;
        }
        return true;
    }

    // Validate password input
    private boolean validatePass() {
        if (ev_password.getText().toString().isEmpty()) {
            ev_password.setError("Field cannot be empty");
            return false;
        }
        return true;
    }

    // Validate re-entered password
    private boolean validateRepass() {
        if (!ev_password.getText().toString().equals(ev_re_password.getText().toString())) {
            ev_re_password.setError("Reentered Password does not match");
            ev_re_password.setText("");
            ev_password.setText("");
            return false;
        } else if (ev_re_password.getText().toString().isEmpty()) {
            ev_re_password.setError("Field cannot be empty");
            return false;
        }
        return true;
    }
}