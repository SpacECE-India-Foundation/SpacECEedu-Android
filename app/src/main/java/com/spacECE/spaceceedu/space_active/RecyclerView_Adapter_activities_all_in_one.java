package com.spacECE.spaceceedu.space_active;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.ConfigUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerView_Adapter_activities_all_in_one extends RecyclerView.Adapter<RecyclerView_Adapter_activities_all_in_one.ViewHolder>{
    Context context;
    ArrayList<space_active_data_holder_all_in_one> arrayList_space_active_all_in_one_data_holder;
    ClickListener clickListener;
    HashMap<String,String> activity_completed=new HashMap<>();

    public RecyclerView_Adapter_activities_all_in_one(ArrayList<space_active_data_holder_all_in_one> arrayList_space_active_all_in_one_data_holder, ClickListener clickListener, Context context,HashMap<String,String>activity_completed) {
        this.arrayList_space_active_all_in_one_data_holder = arrayList_space_active_all_in_one_data_holder;
        this.activity_completed=activity_completed;
        this.clickListener = clickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sapce_active_all_in_one_xml_format,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(arrayList_space_active_all_in_one_data_holder.get(position).activity_name);
        holder.desc.setText(arrayList_space_active_all_in_one_data_holder.get(position).activity_objectives);
        holder.level.setText("\uD83C\uDF82 Level -"+arrayList_space_active_all_in_one_data_holder.get(position).activity_level);
        if (arrayList_space_active_all_in_one_data_holder.get(position).getActivity_video()!=null && !arrayList_space_active_all_in_one_data_holder.get(position).getActivity_video().equals("null")){
            holder.contains_video.setTextColor(context.getResources().getColor(R.color.green));
            holder.contains_video.setText("Contains videos");
        }else {
            holder.contains_video.setVisibility(View.GONE);
            holder.contains_video.setText("Not Contains videos");
        }
        if (arrayList_space_active_all_in_one_data_holder.get(position).activity_image=="null"){
            holder.img.setImageResource(R.drawable.download);
        }
        if (activity_completed.containsKey(arrayList_space_active_all_in_one_data_holder.get(position).activity_no)){
            Integer i=Integer.parseInt(activity_completed.get(arrayList_space_active_all_in_one_data_holder.get(position).activity_no));
            Log.e("onBindViewHolder:!!!!!!!!!!!!!!!!!!!!",i+"");
            if (i==1){
                holder.completed_or_not.setTextColor(context.getResources().getColor(R.color.green));
                holder.completed_or_not.setText("Activity Completed");
            }else if (i==0){
                holder.completed_or_not.setTextColor(context.getResources().getColor(R.color.gray));
                holder.completed_or_not.setText("Half Completed");
            }else if (i==-1){
                holder.completed_or_not.setTextColor(context.getResources().getColor(R.color.red));
                holder.completed_or_not.setText("Not Completed");
            }

        }else {
            holder.completed_or_not.setTextColor(context.getResources().getColor(R.color.red));
            holder.completed_or_not.setText("Not Completed");
        }
        if (arrayList_space_active_all_in_one_data_holder.get(position).activity_image!=null && !arrayList_space_active_all_in_one_data_holder.get(position).activity_image.equals("null")){
            try {
                JSONObject config = ConfigUtils.loadConfig(context.getApplicationContext());
                if (config != null) {
                    String baseUrl= config.getString("BASE_URL");
                    String spaceactiveInsertUserUrl = config.getString("SPACEACTIVE_INSERTUSER");
                    String pic_src = baseUrl + spaceactiveInsertUserUrl + arrayList_space_active_all_in_one_data_holder.get(position).activity_image;
                    try{
                        Picasso.get().load(pic_src).into(holder.img);
                    }catch (Exception e){
                        Log.e( "onBindViewHolder:-------------------",e.toString());
                    }
                    RequestQueue requestQueue= Volley.newRequestQueue(context);
                    StringRequest stringRequest=new StringRequest(pic_src, new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String rsp=response;
                            if (rsp.contains("404 Not Found") || rsp.contains("message=Not Found") || rsp.contains("404") || rsp.length()==1) {
                                Log.e( "onResponse:---------","Not exist");
                                holder.img.setImageDrawable(context.getDrawable(R.drawable.download));
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e( "onFailure:-----------------",error.toString());
                            holder.img.setImageDrawable(context.getDrawable(R.drawable.download));
                        }
                    });
                    requestQueue.add(stringRequest);
                }
            }catch (Exception e) {
                e.printStackTrace();
                Log.i("ERROR:::", "Failed to load API URLs");
            }

        }
    }

    @Override
    public int getItemCount() {
        return arrayList_space_active_all_in_one_data_holder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        TextView desc;
        TextView contains_video;
        TextView completed_or_not;
        TextView level;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            desc=itemView.findViewById(R.id.desc);
            level=itemView.findViewById(R.id.level);
            contains_video=itemView.findViewById(R.id.contains_video);
            completed_or_not=itemView.findViewById(R.id.completed_or_not);
            img=itemView.findViewById(R.id.img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.e( "onClick:-----------",getAdapterPosition()+"---------");
            clickListener.onclick_space_active(getAdapterPosition());
        }
    }
}
