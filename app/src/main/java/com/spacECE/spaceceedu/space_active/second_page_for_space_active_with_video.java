package com.spacECE.spaceceedu.space_active;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LifecycleObserver;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.slider.Slider;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ncorti.slidetoact.SlideToActView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.ConfigUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
    String activity_no;
    SlideToActView slider;

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
        activity_no=intent.getStringExtra("activity_no");
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
        slider=findViewById(R.id.slider);
        Objective=findViewById(R.id.Objective);
        Objective_Ans=findViewById(R.id.Objective_Ans);
        Material=findViewById(R.id.Material);
        Material_Ans=findViewById(R.id.Material_Ans);
        Instructions=findViewById(R.id.Instructions);
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





        slider.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NonNull SlideToActView slideToActView) {
                if (MainActivity.ACCOUNT !=null && MainActivity.ACCOUNT.getAccount_id()!=null){
                    View view= LayoutInflater.from(second_page_for_space_active_with_video.this).inflate(R.layout.main_pop_up,null);
                    AlertDialog.Builder alert =new AlertDialog.Builder(second_page_for_space_active_with_video.this);
                    alert.setView(view);
                    view.findViewById(R.id.yet_to_start).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setCompleted(-1);
                        }
                    });
                    view.findViewById(R.id.on_going).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setCompleted(0);
                        }
                    });
                    view.findViewById(R.id.completed).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setCompleted(1);
                        }
                    });
                    alert.show();
                }
                else {
                    Toast.makeText(second_page_for_space_active_with_video.this, "Please Login First", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void setCompleted(int i){
        Log.e("setCompleted:>>>>>>>>>>>>>>>>>>>>>>>>","");
        RequestQueue requestQueue= Volley.newRequestQueue(second_page_for_space_active_with_video.this);
        Log.e( "setCompleted:-----------","user_id="+ MainActivity.ACCOUNT.getAccount_id()+"&activity_no="+activity_no+"&workdone="+"1");
        try {
            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                String baseUrl= config.getString("BASE_URL");
                String insertUserUrl = config.getString("SPACEACTIVE_INSERTUSER");
                String url=baseUrl+insertUserUrl;
                StringRequest stringRequest=new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e( "onResponse:<<<<<<<<<<<<<",response.toString());
                        JsonParser jsonParser=new JsonParser();
                        JsonObject jsonObject= (JsonObject) jsonParser.parse(response);
                        try {
                            Log.e( "onResponse:!!!!!!!!!!!!!!!!!!!!!!!",jsonObject.get("message").toString());
//                            Toast.makeText(second_page_for_space_active_with_video.this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(second_page_for_space_active_with_video.this,ActivitiesListActivity.class);
                            startActivity(intent);
                        }catch (Exception e){
                            try {
                                Log.e( "onResponse:!!!!!!!!!!!!!!!!!!!!!!!",jsonObject.get("error").toString());
                                Toast.makeText(second_page_for_space_active_with_video.this, jsonObject.get("error").toString(), Toast.LENGTH_SHORT).show();
                            }catch (Exception ea){
                                Log.e( "onResponse:!!!!!!!!!!!!!!!!!!!!!!!",ea.toString());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e( "onErrorResponse:>>>>>>>>>>",error.toString());
                    }
                }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String>params=new HashMap<>();
                        params.put("user_id", MainActivity.ACCOUNT.getAccount_id());
                        params.put("activity_no", activity_no);
                        params.put("workdone", i+"");
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }
    }
}