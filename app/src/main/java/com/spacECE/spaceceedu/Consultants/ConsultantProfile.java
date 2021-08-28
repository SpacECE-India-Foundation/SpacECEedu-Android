package com.spacECE.spaceceedu.Consultants;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spacECE.spaceceedu.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ConsultantProfile extends AppCompatActivity {
    public static ArrayList<Consultant_GetAppointment.Appointments> appointments=new ArrayList<Consultant_GetAppointment.Appointments>();

    Button b_call;
    Button b_chat;
    Button b_appointment;
    ImageView iv_profilePic;
    private TextView tv_name,tv_speciality,tv_chambers,tv_charges,tv_timing,tv_language,tv_days,tv_qualification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_profile);

        tv_name = findViewById(R.id.Consultant_Profile_textView_Name);
        iv_profilePic = findViewById(R.id.Consultant_Profile_ImageView_ProfilePic);
        tv_chambers=findViewById(R.id.Consultant_Profile_textView_Chamber);
        tv_charges=findViewById(R.id.Consultant_Profile_textView_Charges);
        tv_speciality=findViewById(R.id.Consultant_Profile_textView_Speciality);
        tv_days=findViewById(R.id.Consultant_Profile_textView_Days);
        tv_language=findViewById(R.id.Consultant_Profile_textView_Language);
        tv_timing=findViewById(R.id.Consultant_Profile_textView_Timing);
        tv_qualification=findViewById(R.id.Consultant_Profile_textView_Qualification);
        b_appointment = findViewById(R.id.Consultant_Profile_Button_GetAppointment);
        b_call=findViewById(R.id.Consultant_Profile_Button_Call);


        String name = "No name";
        String consultant_id = "Consultant ID missing";
        String speciality = "None";
        String address="Unknown";
        String fee="Free";
        String qualification="None";
        String language="All";
        String days_from="Any";
        String days_to="Any";
        String timing_to="All";
        String timing_from="All";
        String pic_src = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("consultant_name");
            Log.i("CONSULTANT NAME::::", name);
            address=extras.getString("chamber");
            fee= extras.getString("fee");
            language=extras.getString("language");
            days_from=extras.getString("days_from");
            days_to=extras.getString("days_to");
            consultant_id = extras.getString("consultant_id");
            speciality = extras.getString("speciality");
            pic_src=extras.getString("profile_pic");
            timing_from=extras.getString("timing_from");
            timing_to=extras.getString("timing_to");
            qualification=extras.getString("qualification");
        }
        tv_qualification.setText(qualification);
        tv_name.setText(name);
        tv_chambers.setText(address);
        tv_speciality.setText(speciality);
        tv_charges.setText(fee+" /-");
        tv_timing.setText(timing_from+" - "+timing_to);
        tv_language.setText(language);
        tv_days.setText(String.valueOf(days_from.charAt(0))+days_from.charAt(1)+days_from.charAt(2)+" - "+days_to.charAt(0)+days_to.charAt(1)+days_to.charAt(2));
        Picasso.get().load(pic_src).into(iv_profilePic);
        makeList();

        b_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

           //     startActivity(new Intent(getApplicationContext(), Agoraa.class));

            }
        });

        String finalPic_src = pic_src;
        String finalConsultant_id = consultant_id;
        String finalName = name;
        String finalFee = fee;
        String finalSpeciality = speciality;
        b_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Consultant_GetAppointment.class);
                intent.putExtra("profile_pic", finalPic_src);
                intent.putExtra("consultant_id", finalConsultant_id);
                intent.putExtra("consultant_name", finalName);
                intent.putExtra("speciality", finalSpeciality);
                intent.putExtra("fee", finalFee);
                startActivity(intent);            }
        });

    }


    private void makeList() {
        appointments.add(new Consultant_GetAppointment.Appointments("26", "Aug","2021",new String[]{"10:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("27", "Aug","2021",new String[]{"9:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("28", "Aug","2021",new String[]{"8:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("29", "Aug","2021",new String[]{"7:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("30", "Aug","2021",new String[]{"11:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("1", "Sep","2021",new String[]{"9:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("3", "Sep","2021",new String[]{"10:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("5", "Sep","2021",new String[]{"11:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("7", "Sep","2021",new String[]{"8:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("9", "Sep","2021",new String[]{"7:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("10", "Sep","2021",new String[]{"11:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("22", "Sep","2021",new String[]{"10:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("3", "Oct","2022",new String[]{"8:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("6", "Oct","2022",new String[]{"9:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("12", "Oct","2022",new String[]{"11:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
    }
}