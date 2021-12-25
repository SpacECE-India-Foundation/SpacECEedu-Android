package com.spacECE.spaceceedu.Consultants;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;

public class Consultant_AppointmentConfirmation extends AppCompatActivity {

    private TextView BookingId, BookedOn;
    private String bookingId, bookedOn, duration;
    private Button Home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_appointment_confirmation);

        BookingId = findViewById(R.id.Order_ID);
        BookedOn = findViewById(R.id.BookedOn);
        Home = findViewById(R.id.BookingtoHome);

        Bundle extra = getIntent().getExtras();
        bookingId = extra.getString("bookingId");
        bookedOn = extra.getString("bookedOn");
        duration = extra.getString("duration");

        BookingId.setText("Order ID : "+bookingId);
        BookedOn.setText("Booked On "+bookedOn+"for "+duration+"minutes");



        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Consultant_AppointmentConfirmation.this, MainActivity.class));
                finishAffinity();
            }
        });



    }
}