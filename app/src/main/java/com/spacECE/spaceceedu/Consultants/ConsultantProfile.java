package com.spacECE.spaceceedu.Consultants;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spacECE.spaceceedu.R;
import com.squareup.picasso.Picasso;

public class ConsultantProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_profile);

        Button b_chat= findViewById(R.id.Profile_button_GoToChat);
        TextView tv_about = findViewById(R.id.Profile_textView_Speciality);
        TextView tv_name = findViewById(R.id.Profile_textView_Name);
        ImageView iv_profilePic = findViewById(R.id.imageView_ProfilePic);

        String name = "No name";
        String consultant_id = "Video ID missing";
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

        Picasso.get().load(R.drawable.consultant).into(iv_profilePic);
        tv_name.setText(name);
        tv_about.setText(speciality);

    }
}