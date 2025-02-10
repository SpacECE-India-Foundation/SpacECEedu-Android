package com.spacECE.spaceceedu.Authentication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.spacECE.spaceceedu.R;

import com.spacECE.spaceceedu.Utils.ConfigUtils;
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
    private Uri picData; //= Uri.parse(String.valueOf(R.drawable.default_profilepic));
    private TextView uploadImageError;
    private Toast currentToast;
    private Bitmap defaultProfilePic;
    private ImageView editProfileIcon;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    //textView to show strong password text
    private TextView passwordStrengthIndicator;

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
        editProfileIcon = findViewById(R.id.edit_profile_icon);

        // id for password indicator
        passwordStrengthIndicator = findViewById(R.id.passwordStrengthIndicator);

        ev_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String password = s.toString();
                int strength = checkPasswordStrength(password);
                updatePasswordStrengthIndicator(strength);
            }
        });

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


        //load default profile image
        defaultProfilePic = BitmapFactory.decodeResource(getResources(), R.drawable.default_profilepic);
        iv_profile_pic.setImageBitmap(defaultProfilePic);

        //set the imagePickerLauncher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent1 = result.getData();
                        if (intent1 != null && intent1.getData() != null) {
                            picData = intent1.getData();
                            try {
                                Bitmap bitmap = getBitmapFromUri(picData);
                                Bitmap roundedBitmap = getRoundedBitmap(bitmap);
                                iv_profile_pic.setImageBitmap(roundedBitmap);
                                editProfileIcon.setVisibility(View.VISIBLE);
                            } catch (IOException e) {
                                showToast("Error loading image: " + e.getMessage());
                            }
                        }
                    }
                });

        // Set onClickListener for profile picture selection
        iv_profile_pic.setOnClickListener(v -> {
            Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
            intent2.setType("image/*");
            imagePickerLauncher.launch(intent2);
        });

        /*iv_profile_pic.setOnClickListener(new View.OnClickListener() {
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
        }); */

        b_register.setOnClickListener(view -> {
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
        });
    }
    // Set onClickListener for registration button
        /*b_register.setOnClickListener(new View.OnClickListener() {
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
    } */

    // Method to pick an image from the gallery
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    // Handle the result of permission request
  /*  @Override
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
    }*/


    // method to check strong password
    private int checkPasswordStrength(String password) {
        if (password.length() < 8) return 1; // Weak
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }

        int strength = 2; // Medium
        if (hasUpper && hasLower && hasDigit && hasSpecial) strength = 3; // Strong

        return strength;
    }

    //method for update strength indicator
    private void updatePasswordStrengthIndicator(int strength) {
        switch (strength) {
            case 1:
                passwordStrengthIndicator.setText("Weak password!!");
                passwordStrengthIndicator.setTextColor(Color.RED);
                break;
            case 2:
                passwordStrengthIndicator.setText("Medium password!!");
                passwordStrengthIndicator.setTextColor(Color.RED);
                break;
            case 3:
                passwordStrengthIndicator.setText("Strong password.");
                passwordStrengthIndicator.setTextColor(Color.GREEN);
                break;
            default:
                passwordStrengthIndicator.setText("");
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
        /*ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;*/
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
            parcelFileDescriptor.close();
            return image;
        }catch (IOException e){
            Log.e("Image Loading Error", e.getMessage());
            throw e;
        }catch (OutOfMemoryError e) {
            Log.e("Image Loading Error", "Out of memory error loading image.");
            throw new IOException("Out of memory error loading image.");
        }
    }

    private Bitmap getRoundedBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null; // Handle null bitmap gracefully
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = Math.min(width, height); // Use the smaller dimension
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, size, size);
        RectF rectF = new RectF(rect);
        float roundPx = size / 2f;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0); // transparent canvas
        paint.setColor(Color.WHITE); //If you want a colored background
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint); // draw the circle

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)); //Sets the Xfermode
        canvas.drawBitmap(bitmap, rect, rect, paint); // draw the image into the circle

        return output;
    }

    // Encode Bitmap image to byte array
    public static byte[] encodeBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    // Send user registration data to the server
    private void sendUserRegistration(String name, String email, String password, String phone, Uri image) {
        try {
            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                String baseUrl= config.getString("BASE_URL");
                String authRegistrationUrl = config.getString("AUTH_REGISTRATION");
                String register = baseUrl+authRegistrationUrl;

                new Thread(() -> {
                    byte[] encodedImage;
                    Bitmap selectedImage;

                    if (image != null) {
                        try {
                            selectedImage = getBitmapFromUri(image);
                            encodedImage = encodeBase64(selectedImage);
                        } catch (Exception e) {
                            Log.e("Image Encoding Error", "Error encoding image: " + e.getMessage());
                            selectedImage = defaultProfilePic;
                            encodedImage = encodeBase64(selectedImage);
                        }
                    } else {
                        selectedImage = defaultProfilePic;
                        encodedImage = encodeBase64(selectedImage);
                    }


                    OkHttpClient client = new OkHttpClient();
                    RequestBody formBody;

                    if (TYPE != null && LANGUAGE != null && ADDRESS != null && FEE != null && QUALIFICATION != null && START_TIME != null && END_TIME != null) {
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

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.e("Registration Error API", e.getMessage());
                            runOnUiThread(() -> showToast("Network Error")); // Show error message on UI thread
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String responseBody = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(responseBody);
                                handleRegistrationResponse(jsonObject); //Handle the response in a separate method
                            } catch (JSONException e) {
                                Log.e("Registration Error JSON", "Error parsing JSON: " + e.getMessage());
                                runOnUiThread(() -> showToast("Error parsing server response"));
                            }
                        }


                    });
                }).start();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }
    }


    //handleRegistrationResponse
    private void handleRegistrationResponse(JSONObject jsonObject) {
        runOnUiThread(() -> {
            try {
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");

                if (status.equals("success")) {
                    // Successful registration
                    Toast.makeText(getApplicationContext(), "Registration successful! Please login.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish(); // Close the registration activity

                } else if (status.equals("error")) {
                    // Handle different error messages
                    if (message.equals("Email already exists!")) {
                        ev_email.setError("Email already exists!");
                    } else if (message.contains("Phone number")) {
                        ev_phoneNo.setError("Invalid phone number");
                    } else if (message.contains("Password")) {
                        showToast("Password does not meet requirements.");
                    } else {
                        // Generic error message
                        showToast("Registration failed: " + message);
                    }
                } else {
                    // Unknown status from server
                    showToast("Unknown response from server.");
                }
            } catch (JSONException e) {
                Log.e("JSON_ERROR", "Error parsing JSON response: " + e.getMessage());
                showToast("Error processing server response.");
            }
        });
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
        String phone = ev_phoneNo.getText().toString();
        if (phone.isEmpty()) {
            ev_phoneNo.setError("Field cannot be empty");
            return false;
        } else if (!phone.matches("\\d{10}")) {
            ev_phoneNo.setError("Phone number must be exactly 10 digits");
            return false;
        }
        return true;
    }

    // Validate password input
    private boolean validatePass() {
        String password = ev_password.getText().toString();
        if (password.isEmpty()) {
            showToast("Field cannot be empty");
            return false;
        } else if (checkPasswordStrength(password) < 3) { // Require strong password
            showToast("Password must be strong (at least 8 characters, uppercase, lowercase, digit, special character)");
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
    private void showToast(String message) {
        if (currentToast != null) {
            currentToast.cancel();
        }
        currentToast = Toast.makeText(RegistrationFinal.this, message, Toast.LENGTH_SHORT);
        currentToast.show();
    }
}