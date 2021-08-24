package com.spacECE.spaceceedu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Authentication.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRegistration extends AppCompatActivity {

    private Button b_register;
    private EditText ev_email,ev_phoneNo,ev_password,ev_repassword,ev_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        b_register= findViewById(R.id.UserRegistration_Button_Signup);

        ev_email=findViewById(R.id.UserRegistration_editTextText_Email);
        ev_password=findViewById(R.id.UserRegistration_editTextText_Password);
        ev_repassword=findViewById(R.id.UserRegistration_editTextText_Repassword);
        ev_name=findViewById(R.id.UserRegistration_editTextText_Name);
        ev_phoneNo=findViewById(R.id.UserRegistration_editTextText_PhoneNumber);

        //OnClickListener:
        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateData()){
                    b_register.setClickable(false);
                        sendUserRegistration();
                }else{
                    return;
                }
            }
        });

    }

    private void sendUserRegistration() {

        //API Call to register:

        if(true){

            LoginActivity.ACCOUNT= new Account("example@example.com","Sachin","8946452130");
            Toast.makeText(this, "Welcome to SpacECE!", Toast.LENGTH_SHORT).show();
        }else{



        }
    }

    private boolean validateData() {
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
        }else if(!(ev_email.getText().toString()
                .matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$"))){
            ev_email.setError("Invalid Email address");
            return false;
        }else if(emailRegistered(ev_email.getText().toString())){
            ev_email.setError("Email already registered ");
        }
        return true;
    }

    private boolean emailRegistered(String email) {
        JSONObject check=  ApiFunctions.UsingGetAPI(""+email);
        try {
            if(check.get("email").toString().equalsIgnoreCase("true")){
            return true;
            }
        } catch (JSONException e) {
            Log.i("Email Registered API Check :: ","Response not found in object");
            e.printStackTrace();
        }
        return false;
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
            ev_password.setError("Field cannot be empty");
            return false;
        }else{
            return true;
        }
    }

    private boolean validateRepass(){
        if(ev_password.getText().toString().isEmpty()){
            ev_repassword.setError("Field cannot be empty");
            return false;
        }else if(!(ev_password.getText().toString().equals(ev_repassword.getText().toString()))){
            ev_repassword.setError("Reentered Password does not match");
            ev_repassword.setText("");
            ev_password.setText("");
            return false;
        }
        return true;
    }



}