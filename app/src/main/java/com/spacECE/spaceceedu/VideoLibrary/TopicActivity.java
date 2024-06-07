package com.spacECE.spaceceedu.VideoLibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;
import com.spacECE.spaceceedu.R;

import java.net.URLEncoder;

public class TopicActivity extends AppCompatActivity {

        private YouTubePlayerView youTubePlayerView;

        private Button b_likeVideo;
        private Button b_dislikeVideo;
        private Button b_share;
        private Button b_comment;

        private TextView tv_title;
        private TextView tv_like;
        private TextView tv_dislike;
        private TextView tv_views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);


            TextView discrip_view = findViewById(R.id.Topic_TextView_Description);
            b_comment= findViewById(R.id.Topics_Button_Comment);
            b_share=findViewById(R.id.Topic_Button_Share);
            b_likeVideo=findViewById(R.id.Topic_Button_LikeVideo);
            b_dislikeVideo= findViewById(R.id.Topic_Button_DislikeVideo);
            tv_dislike=findViewById(R.id.Topic_TextView_dislikeCount);
            tv_like=findViewById(R.id.Topic_TextView_likeCount);
            tv_views = findViewById(R.id.Topic_TextView_viewCount);
            tv_title=findViewById(R.id.Topic_TextView_Title);

            String name = "No topic";
            String discription = "No ID";
            String v_url = "Video ID missing";
            String status ="Unknow";
            String v_id="Unknown";
            String like_count="Unknown";
            String dislike_count="Unknown";
            String views="unknown";

        //Getting Values from prev activity:
            Bundle extras = getIntent().getExtras();
            if(extras!= null){
                name=extras.getString("topic_name");
                discription=extras.getString("discrp");
                v_url = extras.getString("v_url");
                status= extras.getString("status");
                v_id=extras.getString("v_id");
                like_count=extras.getString("like_count");
                dislike_count=extras.getString("dislike_count");
                views=extras.getString("views");

            }

            discrip_view.setText(discription);
            tv_like.setText(like_count+" Likes");
            tv_dislike.setText(dislike_count+" Dislikes");
            tv_views.setText(views+" Views");
            tv_title.setText(name);

            //YouTube VideoPLayer:
            youTubePlayerView = findViewById(R.id.YoutubePlayerView);
            getLifecycle().addObserver(youTubePlayerView);

        String finalV_url = v_url;
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(finalV_url, 0);
                }
            });

        String finalV_id = v_id;
        b_likeVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "Liked", Toast.LENGTH_SHORT).show();

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                    UsefulFunctions.UsingGetAPI("http://43.205.45.96/SpacTube/api_extractlike.php?uid="+ MainActivity.ACCOUNT.getuId() +"2&vid=3"+ finalV_id);}});
                    thread.start();

                }
            });
        b_dislikeVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "Disliked", Toast.LENGTH_SHORT).show();

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                    UsefulFunctions.UsingGetAPI("http://43.205.45.96/SpacTube/api_getDisLike.php?uid="+MainActivity.ACCOUNT.getuId()+"2&vid="+finalV_id);}});
                    thread.start();
                }
            });
        String finalName = name;
        b_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(Intent.ACTION_SEND);
                    myIntent.setType("text/plain");
                    myIntent.putExtra(Intent.EXTRA_SUBJECT,"SpaceTube");
                    myIntent.putExtra(Intent.EXTRA_TEXT,"Hey!, check this out this video on " + finalName +" by SpacECE: https://www.youtube.com/watch?v="+ finalV_url);
                    startActivity(Intent.createChooser(myIntent,"Share Using"));
                }
            });

        b_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                EditText commentText = findViewById(R.id.Topic_EditText_Comment);
                String comment= URLEncoder.encode(commentText.getText().toString());
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                           UsefulFunctions.UsingGetAPI("http://43.205.45.96/SpacTube/api_extractlike.php?uid="+ MainActivity.ACCOUNT.getuId() +"2&vid=3"+ comment);}});
                    thread.start();
                    commentText.clearComposingText();
                }});
    }
}
