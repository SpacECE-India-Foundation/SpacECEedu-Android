package com.spacECE.spaceceedu.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.spacECE.spaceceedu.ApiFunctions;
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
    static int RC_SIGN_IN = 1;

    String e=null;
    String p=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email=findViewById(R.id.editTextText_EmailAddress);
        et_password=findViewById(R.id.editTextText_Password);
        b_login=findViewById(R.id.Button_Login);
        tv_register=findViewById(R.id.TextView_Register);

        SignInButton G_signInButton = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Input Values: ",et_email.getText()+"/"+et_password.getText().toString());

                logIn(et_email.getText().toString(),et_password.getText().toString());
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(this,RegisterChoice.class));
            }
        });

        G_signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    private void logIn(String email,String password) {
//https://run.mocky.io/v3/af53b6e0-04bf-4ddd-b72c-107fd5b89647

        final JSONObject[] apiCall = {null};
        final Account[] account = {null};
        TextView tv_invalidCredentials=findViewById(R.id.TextView_InvalidCredentials);

        if(email.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")) {
            e = "af53b6e0-04bf-4ddd-";
            p = "b72c-107fd5b89647";
            tv_invalidCredentials.setVisibility(View.INVISIBLE);

        }else{
            Toast.makeText(this,"Invalid Credentials",Toast.LENGTH_LONG).show();
            et_email.setText("");
            et_password.setText("");
            tv_invalidCredentials.setVisibility(View.VISIBLE);
            return;
        }
        final boolean[] authenticated = {false};

        Thread thread = new Thread(new Runnable() {


            @Override
            public void run() {
                TextView tv_invalidCredentials=findViewById(R.id.TextView_InvalidCredentials);

                try  {
                    apiCall[0] =  ApiFunctions.UsingGetAPI("https://run.mocky.io/v3/"+e+p);
                    Log.i("Object Obtained: ",apiCall[0].get("authentication").toString());
                    try {
                        if(apiCall[0].get("authentication").toString().equalsIgnoreCase("success")){

                            Log.i("Authentication: ","Approved!!");

                            tv_invalidCredentials.setVisibility(View.INVISIBLE);

                             account[0] =new Account(apiCall[0].getJSONObject("data").getString("email"),
                                    apiCall[0].getJSONObject("data").getString("username"),
                                    apiCall[0].getJSONObject("data").getString("contact_number"));
                            Log.i(" Data: ", account[0].toString());
                            Intent goToMainPage = new Intent(getApplicationContext(), MainActivity.class);

                        authenticated[0] =true;
                        }else{
                            et_email.setText("");
                            et_password.setText("");
                            tv_invalidCredentials.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                        Log.i("JSON: ","Not Found or Invalid Credentials!!" );
                        et_email.setText("");
                        et_password.setText("");
                        tv_invalidCredentials.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("JSON: ","Not Found or Invalid Credentials!!" );
                    et_email.setText("");
                    et_password.setText("");
                    tv_invalidCredentials.setVisibility(View.VISIBLE);
                }
            }
        });

        thread.start();
        while(!authenticated[0]){
            Log.i("LOOP","00000");

        if(account[0] !=null){
            Log.i("Loop","Ended");
            Intent goToMainPage = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(goToMainPage);
        } }


    }

}