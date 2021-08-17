package com.spacECE.spaceceedu.VideoLibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class VideoLibrary_Recent extends Fragment {

    ArrayList<Topic> list;

    private RecyclerView recyclerView;
    VideoLibrary_RecyclerViewAdapter.RecyclerViewClickListener listener;
    String account_id="2";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_video_library__recent, container, false);

        list = new ArrayList<>(VideoLibrary_Activity.recentTopicList);
//        Bundle extras = getIntent().getExtras();
//        if(extras!= null){account_id=extras.getString("account_id");}

        recyclerView= v.findViewById(R.id.VL_free_RecycleView);

        setAdapter(list);
        return v;
    }
    private void setAdapter(ArrayList<Topic> myList) {
        try{
            Log.i("SetAdapter:", "Working");
            setOnClickListener();
            VideoLibrary_RecyclerViewAdapter adapter = new VideoLibrary_RecyclerViewAdapter(myList, listener);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            Log.i("Adapter", "Executed");
        }catch(NullPointerException n){
            Toast.makeText(getContext(),"No data to show",Toast.LENGTH_LONG).show();
        }
    }

    private void setOnClickListener() {
        listener = new VideoLibrary_RecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getContext(), TopicActivity.class);
                intent.putExtra("acocunt_id",account_id);
                intent.putExtra("topic_name", list.get(position).getTitle());
                intent.putExtra("v_url", list.get(position).getV_URL());
                intent.putExtra("discrp", list.get(position).getDesc());
                intent.putExtra("status",list.get(position).getStatus());
                intent.putExtra("v_id",list.get(position).getV_id());
                startActivity(intent);
            }
        };
    }

    }