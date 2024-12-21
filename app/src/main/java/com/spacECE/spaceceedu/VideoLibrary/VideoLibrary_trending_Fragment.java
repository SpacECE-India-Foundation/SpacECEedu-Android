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

import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class VideoLibrary_trending_Fragment extends Fragment {
    private ArrayList<Topic> rlist= new ArrayList<>();

    private RecyclerView recyclerView;
    private VideoLibrary_RecyclerViewAdapter_recent.RecyclerViewClickListener listener;
    String account_id=null;

    public VideoLibrary_trending_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rlist = new ArrayList<>(VideoLibrary_Activity.trendingTopicList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_video_library__trending_, container, false);
    
        recyclerView=v.findViewById(R.id.VL_trending_RecyclerView);
        setAdapter(rlist);
        return v;
    }
    private void setAdapter(ArrayList<Topic> topicList) {
        Log.i("SetAdapter:","Working");
        setOnClickListener();
        VideoLibrary_RecyclerViewAdapter_recent adapter = new VideoLibrary_RecyclerViewAdapter_recent(topicList,listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        Log.i("Adapter", "Executed");
    }

    private void setOnClickListener() {
        listener = new VideoLibrary_RecyclerViewAdapter_recent.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getContext(), TopicActivity.class);
                intent.putExtra("acocunt_id",account_id);
                intent.putExtra("topic_name", rlist.get(position).getTitle());
                intent.putExtra("v_url", rlist.get(position).getV_URL());
                intent.putExtra("discrp", rlist.get(position).getDesc());
                intent.putExtra("status",rlist.get(position).getStatus());
                intent.putExtra("v_id",rlist.get(position).getV_id());
                intent.putExtra("comments", rlist.get(position).getCntcomment());
                intent.putExtra("views", rlist.get(position).getViews());
                intent.putExtra("like_count", rlist.get(position).getCntlike());
                intent.putExtra("dislike_count", rlist.get(position).getCntdislike());
                startActivity(intent);
            }
        };
    }
}