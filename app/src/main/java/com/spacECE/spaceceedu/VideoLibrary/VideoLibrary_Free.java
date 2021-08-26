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
import android.widget.TextView;

import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class VideoLibrary_Free extends Fragment {

    private ArrayList<Topic> list= new ArrayList<>();
    private ArrayList<Topic> rlist= new ArrayList<>();

    private TextView tv_recentlyViewed;

    private RecyclerView recyclerView;
    private RecyclerView recentRecyclerView;
    VideoLibrary_RecyclerViewAdapter_Free.RecyclerViewClickListener listener;
    private VideoLibrary_RecyclerViewAdapter_recent.RecyclerViewClickListener rListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_video_library__free, container, false);

        list = new ArrayList<>(VideoLibrary_Activity.freeTopicList);
        rlist = new ArrayList<>(VideoLibrary_Activity.recentTopicList);

//        Bundle extras = getIntent().getExtras();
//        if(extras!= null){account_id=extras.getString("account_id");}

        tv_recentlyViewed=v.findViewById(R.id.VL_Free_TextView_recentlyViewed);
        recyclerView= v.findViewById(R.id.VL_free_RecycleView);
        recentRecyclerView=v.findViewById(R.id.VL_recent_RecyclerView);

        setAdapter(list);

        if(!VideoLibrary_Activity.recentTopicList.isEmpty()){
            setRAdapter(rlist);
            tv_recentlyViewed.setVisibility(View.VISIBLE);
        }
        return v;
    }
    private void setRAdapter(ArrayList<Topic> topicList) {
        Log.i("SetAdapter:","Working");
        setOnRClickListener();
        VideoLibrary_RecyclerViewAdapter_recent adapter = new VideoLibrary_RecyclerViewAdapter_recent(topicList,rListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false);
        recentRecyclerView.setLayoutManager(layoutManager);
        recentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        recentRecyclerView.setAdapter(adapter);
        Log.i("Adapter", "Executed");
    }
    private void setOnRClickListener() {
        rListener = new VideoLibrary_RecyclerViewAdapter_recent.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getContext(), TopicActivity.class);
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

    private void setAdapter(ArrayList<Topic> myList) {
        Log.i("SetAdapter:","Working");
        setOnClickListener();
        VideoLibrary_RecyclerViewAdapter_Free adapter = new VideoLibrary_RecyclerViewAdapter_Free(myList,listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        Log.i("Adapter", "Executed");
    }

    private void setOnClickListener() {
        listener = new VideoLibrary_RecyclerViewAdapter_Free.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getContext(), TopicActivity.class);
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