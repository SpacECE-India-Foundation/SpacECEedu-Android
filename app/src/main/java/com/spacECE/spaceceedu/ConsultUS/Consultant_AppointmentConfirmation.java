package com.spacECE.spaceceedu.ConsultUS;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
        getWindow().setStatusBarColor(ContextCompat.getColor(Consultant_AppointmentConfirmation.this,R.color.black));
        BookingId = findViewById(R.id.Order_ID);
        BookedOn = findViewById(R.id.BookedOn);
        Home = findViewById(R.id.BookingtoHome);


        //TODO display the details of booking api still not working on the server side.
//        Intent intent = new Intent(Consultant_Main.this, Notification.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(Consultant_Main.this,
//                Integer.parseInt(response_element.getString("booking_id")), intent, 0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        Date date = new Date();
//        Calendar calendar = Calendar.getInstance();
//        try {
//            date = UsefulFunctions.DateFunc.StringToDate(response_element.getString("b_time"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        calendar.setTime(date);
//        Log.d("Notification", "sendNotification: "+calendar.getTime());
//        alarmManager.set(AlarmManager.RTC_WAKEUP,
//                calendar.getTimeInMillis()-1000*60*10, pendingIntent);


//        Bundle extra = getIntent().getExtras();
//        bookingId = extra.getString("bookingId");
//        bookedOn = extra.getString("bookedOn");
//        duration = extra.getString("duration");
//
//        BookingId.setText("Order ID : "+bookingId);
//        BookedOn.setText("Booked On "+bookedOn+"for "+duration+"minutes");



        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Consultant_AppointmentConfirmation.this, Consultant_Main.class);
                intent.putExtra("show_appointments", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
            }
        });



    }
}