package com.spacECE.spaceceedu.Consultants;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spacECE.spaceceedu.Consultant_GetAppointment;
import com.spacECE.spaceceedu.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ConsultantProfile extends AppCompatActivity {
    public static ArrayList<Consultant_GetAppointment.Appointments> appointments=new ArrayList<Consultant_GetAppointment.Appointments>();

    Button b_call;
    Button b_chat;
    Button b_appointment;
    ImageView iv_profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_profile);

        TextView tv_name = findViewById(R.id.Consultant_Profile_textView_Name);
        iv_profilePic = findViewById(R.id.imageView_ProfilePic);
        b_appointment = findViewById(R.id.Consultant_Profile_Button_GetAppointment);

        String name = "No name";
        String consultant_id = "Consultant ID missing";
        String speciality = "None";
        String rating = "None";
        String pic_src = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("consultant_name");
            consultant_id = extras.getString("consultant_id");
            rating = extras.getString("rating");
            speciality = extras.getString("categories");
            pic_src=extras.getString("pic_src");
        }

        tv_name.setText(name);

        makeList();

        b_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Consultant_GetAppointment.class));
            }
        });
    }

    private void makeList() {
        appointments.add(new Consultant_GetAppointment.Appointments("26", "Aug",new String[]{"10:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("27", "Aug",new String[]{"9:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("28", "Aug",new String[]{"8:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("29", "Aug",new String[]{"7:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("30", "Aug",new String[]{"11:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("1", "Sep",new String[]{"9:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("3", "Sep",new String[]{"10:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("5", "Sep",new String[]{"11:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("7", "Sep",new String[]{"8:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("9", "Sep",new String[]{"7:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("10", "Sep",new String[]{"11:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("22", "Sep",new String[]{"10:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("3", "Oct",new String[]{"8:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("6", "Oct",new String[]{"9:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("12", "Oct",new String[]{"11:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
    }
}