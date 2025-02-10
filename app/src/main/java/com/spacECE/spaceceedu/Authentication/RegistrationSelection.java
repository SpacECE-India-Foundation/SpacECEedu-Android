package com.spacECE.spaceceedu.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.spacECE.spaceceedu.R;

public class RegistrationSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_selection);
        Button b_User=findViewById(R.id.Registration_Selection_Button_User);
        Button b_Consultant= findViewById(R.id.Registration_Selection_Button_Consultant);


        b_Consultant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConsultantRegistrationInit.class);
                startActivity(intent);
            }
        });

        b_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistrationFinal.class);
                startActivity(intent);
            }
        });
    }
}