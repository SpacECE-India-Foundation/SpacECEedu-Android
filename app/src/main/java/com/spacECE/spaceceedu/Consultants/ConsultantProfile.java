package com.spacECE.spaceceedu.Consultants;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

        Button b_chat= findViewById(R.id.Profile_Button_GoToChat);
        Button b_call=findViewById(R.id.Profile_Button_Call);
        TextView tv_about = findViewById(R.id.Profile_textView_Speciality);
        TextView tv_name = findViewById(R.id.Profile_textView_Name);
        ImageView iv_profilePic = findViewById(R.id.imageView_ProfilePic);


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
        b_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        b_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9876543210"));
                startActivity(intent);
            }
        });

        Picasso.get().load(R.drawable.consultant).into(iv_profilePic);
        tv_name.setText(name);
        tv_about.setText(speciality);



    }
}