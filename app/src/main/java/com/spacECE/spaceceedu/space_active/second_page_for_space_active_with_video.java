package com.spacECE.spaceceedu.space_active;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LifecycleObserver;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.spacECE.spaceceedu.R;
import com.squareup.picasso.Picasso;

public class second_page_for_space_active_with_video extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    TextView Activity_Name;
    TextView Objective;
    TextView Objective_Ans;
    TextView Material;
    TextView Material_Ans;
    TextView Instructions;
    TextView steps;
    TextView detail;
    TextView Activity;
    TextView Activity_Number;
    TextView Level;
    TextView Level_Number;
    TextView Dev_Domain;
    TextView Dev_Domain_Ans;
    TextView Dev_Key;
    TextView Dev_Key_Ans;
    TextView Assessment;
    TextView Assessment_Ans;
    TextView Process;
    TextView Process_Ans;
    TextView Result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second_page_for_space_active_with_video);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        Intent intent=getIntent();
        String activity_no=intent.getStringExtra("activity_no");
        String activity_name=intent.getStringExtra("activity_name");
        String activity_level=intent.getStringExtra("activity_level");
        String activity_dev_domain=intent.getStringExtra("activity_dev_domain");
        String activity_objectives=intent.getStringExtra("activity_objectives");
        String activity_key_dev=intent.getStringExtra("activity_key_dev");
        String activity_material=intent.getStringExtra("activity_material");
        String activity_assessment=intent.getStringExtra("activity_assessment");
        String activity_process=intent.getStringExtra("activity_process");
        String activity_instructions=intent.getStringExtra("activity_instructions");
        String activity_complete_status=intent.getStringExtra("activity_complete_status");
        String activity_image=intent.getStringExtra("activity_image");
        String activity_video=intent.getStringExtra("activity_video");
        String activity_type_status=intent.getStringExtra("activity_type_status");
        String activity_date=intent.getStringExtra("activity_date");
        String playlist_id=intent.getStringExtra("playlist_id");
        String playlist_descr=intent.getStringExtra("playlist_descr");
        String playlist_name=intent.getStringExtra("playlist_name");
        String image=intent.getStringExtra("activity_image");

        youTubePlayerView=findViewById(R.id.YouTubePlayerView);
        Activity_Name=findViewById(R.id.Activity_Name);
        Objective=findViewById(R.id.Objective);
        Objective_Ans=findViewById(R.id.Objective_Ans);
        Material=findViewById(R.id.Material);
        Material_Ans=findViewById(R.id.Material_Ans);
        Instructions=findViewById(R.id.Instructions);
        steps=findViewById(R.id.steps);
        detail=findViewById(R.id.detail);
        Activity=findViewById(R.id.Activity);
        Activity_Number=findViewById(R.id.Activity_Number);
        Level=findViewById(R.id.Level);
        Level_Number=findViewById(R.id.Level_Number);
        Dev_Domain=findViewById(R.id.Dev_Domain);
        Dev_Domain_Ans=findViewById(R.id.Dev_Domain_Ans);
        Dev_Key=findViewById(R.id.Dev_Key);
        Dev_Key_Ans=findViewById(R.id.Dev_Key_Ans);
        Assessment=findViewById(R.id.Assessment);
        Assessment_Ans=findViewById(R.id.Assessment_Ans);
        Process=findViewById(R.id.Process);
        Process_Ans=findViewById(R.id.Process_Ans);
        Result=findViewById(R.id.Result);


        Log.e( "onCreate: ",image+"");
        if (activity_video!=null && !activity_video.equals("null")){
            getLifecycle().addObserver((LifecycleObserver) youTubePlayerView);
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(activity_video, 0);
                }
            });
        }

        Activity_Name.setText(activity_name);
        Objective_Ans.setText(activity_objectives);
        Material_Ans.setText(activity_material);
        detail.setText(activity_instructions);
        Activity_Number.setText(activity_no);
        Level_Number.setText(activity_level);
        Dev_Domain_Ans.setText(activity_dev_domain);
        Dev_Key_Ans.setText(activity_key_dev);
        Assessment_Ans.setText(activity_assessment);
        Process_Ans.setText(activity_process);
    }
}