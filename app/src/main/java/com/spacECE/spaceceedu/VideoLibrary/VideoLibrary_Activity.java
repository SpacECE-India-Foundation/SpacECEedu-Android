package com.spacECE.spaceceedu.VideoLibrary;


import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.spacECE.spaceceedu.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideoLibrary_Activity extends Activity {
    ArrayList<Topic> topicList= new ArrayList<>();
    private RecyclerView recyclerView;
    VideoLibrary_RecyclerViewAdapter.RecyclerViewClickListener listener;

    private RequestQueue mQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_library);

        mQueue = Volley.newRequestQueue(this);
        String url = "http://3.109.14.4/SpacTube/api_getAllVideos";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));

                            Topic newTopic = new Topic( response_element.getString("v_id"),
                                    response_element.getString("title"), response_element.getString("v_id"),
                                    response_element.getString("filter"), response_element.getString("length"),
                                    response_element.getString("v_url"), response_element.getString("v_date"),
                                            response_element.getString("v_uni_no"),response_element.getString("desc"));

                            topicList.add(newTopic);
                        }
                        setAdapter();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("    API","NO RESPONSE !!!!!!!!!!!!!!!!!");
            }
        });

        recyclerView= findViewById(R.id.RecycleView);

        mQueue.add(request);



    }

     void setList() {
       //
    }

    private void setAdapter() {
        Log.i("SetAdapter:","Working");
        setOnClickListener();
        VideoLibrary_RecyclerViewAdapter adapter = new VideoLibrary_RecyclerViewAdapter(topicList,listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        Log.i("Adapter", "Executed");
    }

    private void setOnClickListener() {
        listener = new VideoLibrary_RecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {

                Intent intent = new Intent(getApplicationContext(), TopicActivity.class);
                intent.putExtra("topic_name", topicList.get(position).getTitle());
                intent.putExtra("v_url", topicList.get(position).getV_URL());
                intent.putExtra("discrp", topicList.get(position).getDesc());
                intent.putExtra("status",topicList.get(position).getStatus());

                startActivity(intent);
            }
        };
    }
}