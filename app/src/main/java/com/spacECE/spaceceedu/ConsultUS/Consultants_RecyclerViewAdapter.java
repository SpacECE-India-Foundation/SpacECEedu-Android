package com.spacECE.spaceceedu.ConsultUS;

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

public class Consultants_RecyclerViewAdapter extends RecyclerView.Adapter<Consultants_RecyclerViewAdapter.MyViewHolder> {
    ArrayList<Consultant> consultants;
    Context context;
    private RecyclerViewClickListener listener;

    public Consultants_RecyclerViewAdapter(ArrayList<Consultant> consultants, RecyclerViewClickListener listener,Context context) {
        this.consultants = consultants;
        this.context=context;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name, rating, price, category;
        private ImageView profile;

        public MyViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.textView_Name);
            profile = view.findViewById(R.id.imageView_ProfilePic);
            category = view.findViewById(R.id.textView_Category);
            rating = view.findViewById(R.id.textView_Rating);
            price = view.findViewById(R.id.textView_Price);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {listener.onClick(view, getAdapterPosition()); }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.consultant_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = consultants.get(position).getName();
        String profilePicSrc = consultants.get(position).getProfilePic_src();
        String categories = consultants.get(position).getCategories();
        String price = consultants.get(position).getPrice();
        holder.name.setText(name);
        holder.category.setText(categories);
        holder.price.setText("Fee: "+String.valueOf(price)+"/-");

        try {
            JSONObject config = ConfigUtils.loadConfig(context.getApplicationContext());
            if (config != null) {
                String baseUrl= config.getString("BASE_URL");
                String userImgUrl = config.getString("USER_IMG");

                //currently, src only send image name we have to set the image path
                profilePicSrc = baseUrl+userImgUrl + profilePicSrc;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }
        try {
            Picasso.get().load(profilePicSrc.replace("https://","http://")).into(holder.profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = profilePicSrc.replace("https://","http://");
        RequestQueue requestQueue=new Volley().newRequestQueue(context);
        StringRequest stringRequest=new StringRequest(url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String rsp=response;
                Log.e( "onResponse:-----------------",rsp);
                if (rsp.contains("404 Not Found") || rsp.contains("message=Not Found") || rsp.contains("404") || rsp.length()==1) {
                    Log.e( "onResponse:---------","Not exist");
                    holder.profile.setImageDrawable(context.getDrawable(R.drawable.img_1));
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e( "onFailure:-----------------",error.toString());
                holder.profile.setImageDrawable(context.getDrawable(R.drawable.img_1));
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return consultants.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }


}