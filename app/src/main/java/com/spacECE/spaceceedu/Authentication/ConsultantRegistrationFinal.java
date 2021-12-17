package com.spacECE.spaceceedu.Authentication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import com.spacECE.spaceceedu.R;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ConsultantRegistrationFinal extends AppCompatActivity {

    private Button b_register;
    private ImageView iv_profile_pic;
    private EditText ev_email,ev_phoneNo,ev_password,ev_repassword,ev_name;
    private boolean consultant=false;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private Uri picData= Uri.parse(String.valueOf(R.drawable.default_profilepic));
    Toolbar toolbar;
    UserLocalStore userLocalStore;

    String TYPE, LANGUAGE, ADDRESS, FEE,
            QUALIFICATION, START_TIME, END_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        userLocalStore = new UserLocalStore(getApplicationContext());

        b_register= findViewById(R.id.UserRegistration_Button_Signup);
        iv_profile_pic= findViewById(R.id.UserRegistration_ImageView_ProfilePic);

        ev_email=findViewById(R.id.UserRegistration_editTextText_Email);
        ev_password=findViewById(R.id.UserRegistration_editTextText_Password);
        ev_repassword=findViewById(R.id.UserRegistration_editTextText_Repassword);
        ev_name=findViewById(R.id.UserRegistration_editTextText_Name);
        ev_phoneNo=findViewById(R.id.UserRegistration_editTextText_PhoneNumber);
        toolbar =  findViewById(R.id.UserRegistration_toolbar);

        setSupportActionBar(toolbar);

        //Intent and shit

        Intent intent = getIntent();

        TYPE = intent.getStringExtra("Type");
        LANGUAGE = intent.getStringExtra("Language");
        ADDRESS = intent.getStringExtra("Address");
        FEE = intent.getStringExtra("Fee");
        QUALIFICATION = intent.getStringExtra("Qualification");
        START_TIME = intent.getStringExtra("StartTime");
        END_TIME = intent.getStringExtra("EndTime");



        //OnClickListener:
        iv_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check runtime permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){
                        //permission not granted, request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else {
                        //permission already granted
                        pickImageFromGallery();
                    }
                }
                else {
                    //system os is less then marshmallow
                    pickImageFromGallery();
                }

            }
        });

        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateData()) {

                    sendUserRegistration( ev_name.getText().toString(), ev_email.getText().toString(),
                            ev_password.getText().toString(), ev_phoneNo.getText().toString(), picData);

                } else {
                    Toast.makeText(getApplicationContext(), "Check Details", Toast.LENGTH_LONG).show();
                }

            }
        });




    }

    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    pickImageFromGallery();
                } else {
                    //permission was denied
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            //set image to image view
            picData= data.getData();
            iv_profile_pic.setImageURI(data.getData());
        }
    }

    private void sendUserRegistration(String name, String email, String password, String phone, Uri image){

        String register = "http://spacefoundation.in/test/SpacECE-4477/spacece_auth/register_action.php";

        new Thread(new Runnable() {

            JSONObject jsonObject;

            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                RequestBody fromBody = new FormBody.Builder()
                        .add("name", name)
                        .add("email", email)
                        .add("password", password)
                        .add("phone", phone)
                        .add("image", "null")
                        .add("type", "customer")
                        .build();

                Request request = new Request.Builder()
                        .url(register)
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
                            Log.d("TAG", "onResponse: "+jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error Registering!", Toast.LENGTH_SHORT).show();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                try {
                                    Log.d("TAG", "onResponse: "+jsonObject.getString("status"));
                                    if(jsonObject.getString("status").equals("error")) {
                                        if(jsonObject.getString("message").equals("Email already exists!")) {
                                            ev_email.setError("Email already exist!");
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Please try after some time!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if(jsonObject.getString("status").equals("success")) {

                                        Toast.makeText(getApplicationContext(), "Welcome to SpacECE Login to Continue!", Toast.LENGTH_SHORT).show();
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


    private boolean validateData() {
        validateName();
        validatePhone();
        validateEmail();
        validatePass();
        validateRepass();

        if(validateEmail() && validateName() && validatePass()
                && validateRepass() && validatePhone()) {
            return true;
        }
        return false;
    }

    private boolean validateEmail(){
        if(ev_email.getText().toString().isEmpty()){
            ev_email.setError("Field cannot be empty");
            return false;
        }
        else if(!(ev_email.getText().toString().contains("@"))){
            ev_email.setError("Invalid Email address");
            return false;
        }
        return true;
    }

    private boolean validateName(){
        if(ev_name.getText().toString().isEmpty()){
            ev_name.setError("Field cannot be empty");
            return false;
        }else{
            return true;
        }
    }

    private boolean validatePhone(){
        if(ev_phoneNo.getText().toString().isEmpty()){
            ev_phoneNo.setError("Field cannot be empty");
            return false;
        }else{
            return true;
        }
    }

    private boolean validatePass(){
        if(ev_repassword.getText().toString().isEmpty()){
            ev_repassword.setError("Field cannot be empty");
            return false;
        }else{
            return true;
        }
    }

    private boolean validateRepass(){
        if(!(ev_password.getText().toString().equals(ev_repassword.getText().toString()))){
            ev_repassword.setError("Reentered Password does not match");
            ev_repassword.setText("");
            ev_password.setText("");
            return false;
        }else if(ev_password.getText().toString().isEmpty()){
            ev_password.setError("Field cannot be empty");
            return false;
        }
        return true;
    }



}