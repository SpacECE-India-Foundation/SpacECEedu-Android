package com.spacECE.spaceceedu.VideoLibrary;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacECE.spaceceedu.ApiFunctions;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.VideoLibrary.Topic;
import com.spacECE.spaceceedu.VideoLibrary.TopicActivity;
import com.spacECE.spaceceedu.VideoLibrary.VideoLibrary_RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideoLibrary_Free extends Fragment {

    ArrayList<Topic> topicList= new ArrayList<>();

    private RecyclerView recyclerView;
    VideoLibrary_RecyclerViewAdapter.RecyclerViewClickListener listener;
    String account_id=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_video_library__free, container, false);

//        Bundle extras = getIntent().getExtras();
//        if(extras!= null){account_id=extras.getString("account_id");}

        recyclerView= v.findViewById(R.id.VL_free_RecycleView);

        try {
            getList("http://3.109.14.4/SpacTube/api_getAllVideos");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }
    public void getList(String URL) throws JSONException {
        final JSONObject[] response = {null};

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

        response[0] = ApiFunctions.UsingGetAPI(URL);
        Log.i("Object Obtained: ", response.toString());

        JSONArray jsonArray = null;
        try {
            jsonArray = response[0].getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(" Response : "," Array not found in JSON Response object");
        }
        Log.i("API : ",response.toString());

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject response_element = null;
            try {
                response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Topic newTopic;
            try {
                newTopic = new Topic( response_element.getString("v_id"),
                        response_element.getString("title"), response_element.getString("v_id"),
                        response_element.getString("filter"), response_element.getString("length"),
                        response_element.getString("v_url"), response_element.getString("v_date"),
                        response_element.getString("v_uni_no"),response_element.getString("desc"));
                topicList.add(newTopic);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("JSON Object: ","Key not found");
            }
        }
        Log.i("VIDEOS:::::",topicList.toString());

            }
        });

        while(true){
            if(completed[0]==1)
            setAdapter(topicList);
        }
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
}