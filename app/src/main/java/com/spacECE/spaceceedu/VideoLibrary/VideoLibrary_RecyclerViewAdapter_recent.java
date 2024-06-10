package com.spacECE.spaceceedu.VideoLibrary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoLibrary_RecyclerViewAdapter_recent extends RecyclerView.Adapter<VideoLibrary_RecyclerViewAdapter_recent.MyViewHolder> {
    ArrayList<Topic> topics= new ArrayList<Topic>();
    private RecyclerViewClickListener listener;

    public VideoLibrary_RecyclerViewAdapter_recent(ArrayList<Topic> topics, RecyclerViewClickListener listener){
        this.topics=topics;
        this.listener= listener;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView topic_name;
        private TextView duration;
        private ImageView bg_img;

        public MyViewHolder(@NonNull View view) {
            super(view);
            topic_name = view.findViewById(R.id.topicName);
            bg_img = view.findViewById(R.id.imageView);
            duration= view.findViewById(R.id.durationView);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public VideoLibrary_RecyclerViewAdapter_recent.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoLibrary_RecyclerViewAdapter_recent.MyViewHolder holder, int position) {
        String name = topics.get(position).getTitle();
        String v_url = topics.get(position).getV_URL();
        String duration = topics.get(position).getLength();
        holder.topic_name.setText(name);
        holder.duration.setText(duration);
        Picasso.get().load("https://img.youtube.com/vi/"+v_url+"/0.jpg").into(holder.bg_img);
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
