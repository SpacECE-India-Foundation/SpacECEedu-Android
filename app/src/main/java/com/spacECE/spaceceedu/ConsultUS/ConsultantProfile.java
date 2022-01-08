package com.spacECE.spaceceedu.ConsultUS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spacECE.spaceceedu.Authentication.LoginActivity;
import com.spacECE.spaceceedu.R;
import com.squareup.picasso.Picasso;

import static com.spacECE.spaceceedu.MainActivity.ACCOUNT;

public class ConsultantProfile extends AppCompatActivity {

    Button b_appointment;
    ImageView iv_profilePic;
    private TextView tv_name,tv_speciality,tv_chambers,tv_charges,tv_timing,tv_language,tv_days,tv_qualification;


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(ACCOUNT!=null) {
            b_appointment.setText("Book Appointment!");
        }
    }

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


        if(ACCOUNT==null) {
            b_appointment.setText("Login to Book!");
        }

        String name = "No name";
        String consultant_id = "Consultant ID missing";
        String speciality = "None";
        String address="Unknown";
        String fee="Free";
        String qualification="None";
        String language="All";
        String available_days="";
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
            available_days=extras.getString("available_days");
            qualification=extras.getString("qualification");
            pic_src = extras.getString("profile_pic");
        }


        tv_qualification.setText(qualification);
        tv_name.setText(name);
        tv_chambers.setText(address);
        tv_speciality.setText(speciality);
        tv_charges.setText(fee+" /-");
        tv_timing.setText(timing_from.substring(0,5)+" - "+timing_to.substring(0,5));
        tv_language.setText(language);
        tv_days.setText(available_days);

        try {
            pic_src = "https://spacefoundation.in/test/SpacECE-PHP/img/users/" + pic_src;
            Picasso.get().load(pic_src.replace("https://","http://")).into(iv_profilePic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String finalPic_src = pic_src;
        String finalConsultant_id = consultant_id;
        String finalName = name;
        String finalFee = fee;
        String finalSpeciality = speciality;
        String finalAvailable_days = available_days;
        String finalTiming_from = timing_from;
        String finalTiming_to = timing_to;

        b_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;
                if(ACCOUNT==null) {
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), Consultant_GetAppointment.class);
                    intent.putExtra("profile_pic", finalPic_src);
                    intent.putExtra("consultant_id", finalConsultant_id);
                    intent.putExtra("consultant_name", finalName);
                    intent.putExtra("speciality", finalSpeciality);
                    intent.putExtra("fee", finalFee);
                    intent.putExtra("available_days", finalAvailable_days);
                    intent.putExtra("startTime", finalTiming_from);
                    intent.putExtra("endTime", finalTiming_to);
                }
                startActivity(intent);
            }
        });

    }
}