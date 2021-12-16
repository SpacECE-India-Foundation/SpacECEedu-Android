package com.spacECE.spaceceedu.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;

public class ConsultantRegistration extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_registration);
        Button b_register=findViewById(R.id.ConsultantRegistration_Button_Signup);
        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                toolbar =  findViewById(R.id.UserRegistration_toolbar);

                setSupportActionBar(toolbar);

                toolbar.setTitle("Consultant Registration");
                b_register.setText("Next");
            }
        });
    }
}