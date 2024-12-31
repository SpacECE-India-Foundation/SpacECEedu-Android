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

public class VideoLibrary_Premium extends Fragment {

    ArrayList<Topic> list= new ArrayList<>();

    private RecyclerView recyclerView;
    private VideoLibrary_RecyclerViewAdapter_paid.RecyclerViewClickListener listener;
    String account_id=null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_video_library__premium, container, false);

        list=new ArrayList<>(VideoLibrary_Activity.paidTopicList);


        recyclerView= v.findViewById(R.id.VL_premium_RecyclerView);

        setAdapter(list);
        return v;
    }

    private void setAdapter(ArrayList<Topic> topicList) {
        Log.i("SetAdapter:","Working");
        setOnClickListener();
        VideoLibrary_RecyclerViewAdapter_paid adapter = new VideoLibrary_RecyclerViewAdapter_paid(topicList,listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        if(topicList.size()==0){
            Toast.makeText(getActivity(), "No Paid Content Available", Toast.LENGTH_SHORT).show();
        }
        Log.i("Adapter", "Executed");
    }

    private void setOnClickListener() {
        listener = new VideoLibrary_RecyclerViewAdapter_paid.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getContext(), TopicActivity.class);
                intent.putExtra("acocunt_id",account_id);
                intent.putExtra("topic_name", list.get(position).getTitle());
                intent.putExtra("v_url", list.get(position).getV_URL());
                intent.putExtra("discrp", list.get(position).getDesc());
                intent.putExtra("status",list.get(position).getStatus());
                intent.putExtra("v_id",list.get(position).getV_id());
                intent.putExtra("comments", list.get(position).getCntcomment());
                intent.putExtra("views", list.get(position).getViews());
                intent.putExtra("like_count", list.get(position).getCntlike());
                intent.putExtra("dislike_count", list.get(position).getCntdislike());
                startActivity(intent);
            }
        };
    }
}