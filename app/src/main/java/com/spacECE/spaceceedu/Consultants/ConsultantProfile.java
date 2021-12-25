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
        String pic_src = "https://img.favpng.com/11/24/17/management-consulting-consulting-firm-consultant-business-png-favpng-jkyKzuQ3UyL0wXXCBcvk4c1fu.jpg";

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            name = extras.getString("consultant_name");
            Log.i("CONSULTANT NAME::::", name);
            address=extras.getString("chamber");
            fee= extras.getString("fee");
            language=extras.getString("language");
            consultant_id = extras.getString("consultant_id");
            speciality = extras.getString("speciality");
            timing_from=extras.getString("timing_from");
            timing_to=extras.getString("timing_to");
            qualification=extras.getString("qualification");
            pic_src = extras.getString("profilepic");
        }


        tv_qualification.setText(qualification);
        tv_name.setText(name);
        tv_chambers.setText(address);
        tv_speciality.setText(speciality);
        tv_charges.setText(fee+" /-");
        tv_timing.setText(timing_from+" - "+timing_to);
        tv_language.setText(language);
        tv_days.setText(String.valueOf(days_from.charAt(0))+days_from.charAt(1)+days_from.charAt(2)+" - "+days_to.charAt(0)+days_to.charAt(1)+days_to.charAt(2));

        try {
            pic_src = "https://spacefoundation.in/test/SpacECE-PHP/img/users/" + pic_src;
            Picasso.get().load(pic_src.replace("https://","http://")).into(iv_profilePic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        makeList();

        b_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), Agoraa.class));

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
                startActivity(intent);
            }
        });

    }


    private void makeList() {
        appointments.add(new Consultant_GetAppointment.Appointments("26", "Aug","2021",new String[]{"10:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("6", "Oct","2022",new String[]{"9:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
        appointments.add(new Consultant_GetAppointment.Appointments("12", "Oct","2022",new String[]{"11:15 AM","11:30 AM","12:45 PM","1:25 PM","3:00 PM","4:15 PM","4:45 PM","6:00 PM"}));
    }
}