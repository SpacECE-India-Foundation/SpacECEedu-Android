package com.spacECE.spaceceedu.VideoLibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.spacECE.spaceceedu.R;

public class TopicActivity extends AppCompatActivity {

        private YouTubePlayerView youTubePlayerView;

        Button b_likeVideo;
        Button b_dislikeVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);


            TextView discrip_view = findViewById(R.id.DescripTextView);
            TextView status_view = findViewById(R.id.Topic_TextView_Status);
            String name = "No topic";
            String discription = "No ID";
            String v_url = "Video ID missing";
            String status ="Unknow";

            //Getting Values from prev activity:
            Bundle extras = getIntent().getExtras();
            if(extras!= null){
                name=extras.getString("topic_name");
                discription=extras.getString("discrp");
                v_url = extras.getString("v_url");
                status= extras.getString("status");
            }

            discrip_view.setText(discription);
            status_view.setText(status);

            //YouTube VideoPLayer:
            youTubePlayerView = findViewById(R.id.YoutubePlayerView);
            getLifecycle().addObserver(youTubePlayerView);
            String finalV_id = v_url;
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(finalV_id, 0);
                }
            });
            //

            b_likeVideo=findViewById(R.id.Topic_Button_LikeVideo);
            b_dislikeVideo= findViewById(R.id.Topic_Button_DislikeVideo);

            b_likeVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "Liked", Toast.LENGTH_SHORT).show();
                    //API CALL TO LIKE;
                }
            });
            b_dislikeVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "Disliked", Toast.LENGTH_SHORT).show();

                    //API CALL TO DISLIKE;

                }
            });
        }



    }
