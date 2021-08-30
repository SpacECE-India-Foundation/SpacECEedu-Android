package com.spacECE.spaceceedu.Authentication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.UsefulFunctions;
import com.spacECE.spaceceedu.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UserRegistration extends AppCompatActivity {

    private Button b_register;
    private ImageView iv_profile_pic;
    private EditText ev_email,ev_phoneNo,ev_password,ev_repassword,ev_name;
    private boolean consultant=false;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private Uri picData= Uri.parse(String.valueOf(R.drawable.default_profilepic));
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        Bundle extra = getIntent().getExtras();

        consultant=getIntent().getBooleanExtra("consultant",false);

        b_register= findViewById(R.id.UserRegistration_Button_Signup);
        iv_profile_pic= findViewById(R.id.UserRegistration_ImageView_ProfilePic);

        ev_email=findViewById(R.id.UserRegistration_editTextText_Email);
        ev_password=findViewById(R.id.UserRegistration_editTextText_Password);
        ev_repassword=findViewById(R.id.UserRegistration_editTextText_Repassword);
        ev_name=findViewById(R.id.UserRegistration_editTextText_Name);
        ev_phoneNo=findViewById(R.id.UserRegistration_editTextText_PhoneNumber);
        toolbar =  findViewById(R.id.UserRegistration_toolbar);
        setSupportActionBar(toolbar);
        if(consultant){
            toolbar.setTitle("Consultant Registration");
            b_register.setText("Next");
        }
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
                        if(consultant){
                            MainActivity.ACCOUNT= new Account(ev_email.getText().toString(),ev_name.getText().toString(),ev_phoneNo.getText().toString(),true,picData.toString(), "1", null);
                            startActivity(new Intent(getApplicationContext(), ConsultantRegistration.class));
                        }else{
                            sendUserRegistration(ev_email.getText().toString(), ev_name.getText().toString(), ev_password.getText().toString(), ev_phoneNo.getText().toString());
                            MainActivity.ACCOUNT= new Account(ev_email.getText().toString(),ev_name.getText().toString(),ev_phoneNo.getText().toString(),false,null, "1", null);

                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
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
    private void sendUserRegistration(String email_id, String name, String password, String contact){

        //API Call to register:
        if(true){
            String sendData=ev_email.getText().toString()+"&fullname="+ev_name.getText().toString()+"&password=+"+ev_password.getText().toString()+"&email="+ev_email.getText().toString()+"&mobile="+ev_phoneNo.getText().toString()+"&img="+"null"+"&token="+"null";
                Thread newThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            UsefulFunctions.UsingGetAPI("http://educationfoundation.space/ConsultUs/api_registration_user?username="+URLEncoder.encode(sendData, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    }
                });
                newThread.start();
            Toast.makeText(this, "Welcome to SpacECE!", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Check Network", Toast.LENGTH_LONG).show();
        }
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

    private boolean validateName(){
        if(ev_name.getText().toString().isEmpty()){
            ev_name.setError("Field cannot be empty");
            return false;
        }else{
            return true;
        }
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
        else if(!emailRegistered(ev_email.getText().toString())){
            ev_email.setError("Email already registered ");
            return false;
        }
        return true;
    }

    private boolean emailRegistered(String email) {

        final boolean[] COMPLETED = {false};
        final boolean[] EXISTS = {false};
        final JSONObject[] apiCall = {null};

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                        JSONObject check = UsefulFunctions.UsingGetAPI("http://educationfoundation.space/ConsultUs/api_getalluser?user=" + email);
                        Log.i("Email Validation(EV):::", check.toString());
                        try {
                            EXISTS[0]=(check.get("status").toString().equalsIgnoreCase("fail"));
                        } catch (JSONException e) {
                            Log.i("Email Registered API Check (ERAC):: ", "Response not found in object");
                            e.printStackTrace();
                        }
                    COMPLETED[0]=true;
                }catch(RuntimeException e ){
                    Log.i(e+" EXCEPTION:::","Server took too long or JSON Key not found");
                    COMPLETED[0]=true;
                }
            }
        });

        thread.start();
        while(!COMPLETED[0]){
            Log.i("Email Check (EC):::", "Waiting.....");
        }
        Log.i("Email Check (EC):::", "Completed.");
        Log.i("EMAIL FOUND (EF):::::", String.valueOf(EXISTS[0]));
        return EXISTS[0];
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