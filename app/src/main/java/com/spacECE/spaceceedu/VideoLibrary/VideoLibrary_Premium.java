package com.spacECE.spaceceedu.VideoLibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.ApiFunctions;
import com.spacECE.spaceceedu.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideoLibrary_Premium extends Fragment {

    ArrayList<Topic> topicList= new ArrayList<>();

    private RecyclerView recyclerView;
    VideoLibrary_RecyclerViewAdapter.RecyclerViewClickListener listener;
    String account_id=null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_video_library__premium, container, false);

////        Bundle extras = getIntent().getExtras();
////        if(extras!= null){account_id=extras.getString("account_id");}
//
        recyclerView= v.findViewById(R.id.VL_premium_RecyclerView);

       return v;
    }



    private void setAdapter(ArrayList<Topic> topicList) {
        Log.i("SetAdapter:","Working");
        setOnClickListener();
        VideoLibrary_RecyclerViewAdapter adapter = new VideoLibrary_RecyclerViewAdapter(topicList,listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        Log.i("Adapter", "Executed");
    }

    private void setOnClickListener() {
        listener = new VideoLibrary_RecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getContext(), TopicActivity.class);
                intent.putExtra("acocunt_id",account_id);
                intent.putExtra("topic_name", topicList.get(position).getTitle());
                intent.putExtra("v_url", topicList.get(position).getV_URL());
                intent.putExtra("discrp", topicList.get(position).getDesc());
                intent.putExtra("status",topicList.get(position).getStatus());
                intent.putExtra("v_id",topicList.get(position).getV_id());
                startActivity(intent);
            }
        };
    }
}