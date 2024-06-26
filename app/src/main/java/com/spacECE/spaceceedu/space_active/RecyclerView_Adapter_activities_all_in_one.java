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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerView_Adapter_activities_all_in_one extends RecyclerView.Adapter<RecyclerView_Adapter_activities_all_in_one.ViewHolder>{
    Context context;
    ArrayList<space_active_data_holder_all_in_one> arrayList_space_active_all_in_one_data_holder;
    ClickListener clickListener;

    public RecyclerView_Adapter_activities_all_in_one(ArrayList<space_active_data_holder_all_in_one> arrayList_space_active_all_in_one_data_holder, ClickListener clickListener, Context context) {
        this.arrayList_space_active_all_in_one_data_holder = arrayList_space_active_all_in_one_data_holder;
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
        holder.level.setText("Level -"+arrayList_space_active_all_in_one_data_holder.get(position).activity_level);
        holder.contains_video.setText("Contains videos -"+arrayList_space_active_all_in_one_data_holder.get(position).activity_video);
        holder.completed_or_not.setText("Completed or not -"+arrayList_space_active_all_in_one_data_holder.get(position).activity_complete_status);
        if (arrayList_space_active_all_in_one_data_holder.get(position).activity_image=="null"){
            holder.img.setImageResource(R.drawable.img_1);
        }
        if (arrayList_space_active_all_in_one_data_holder.get(position).activity_image!=null && !arrayList_space_active_all_in_one_data_holder.get(position).activity_image.equals("null")){
            String pic_src = "http://43.205.45.96/img/users/" + arrayList_space_active_all_in_one_data_holder.get(position).activity_image;
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
                    Log.e( "onResponse:-----------------",rsp);
                    if (rsp.contains("404 Not Found") || rsp.contains("message=Not Found") || rsp.contains("404") || rsp.length()==1) {
                        Log.e( "onResponse:---------","Not exist");
                        holder.img.setImageDrawable(context.getDrawable(R.drawable.img_1));
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e( "onFailure:-----------------",error.toString());
                    holder.img.setImageDrawable(context.getDrawable(R.drawable.img_1));
                }
            });
            requestQueue.add(stringRequest);
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
