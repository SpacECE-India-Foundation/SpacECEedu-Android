package com.spacECE.spaceceedu.ConsultUS;

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

public class Consultants_RecyclerViewAdapter extends RecyclerView.Adapter<Consultants_RecyclerViewAdapter.MyViewHolder> {
    ArrayList<Consultant> consultants;

    private RecyclerViewClickListener listener;

    public Consultants_RecyclerViewAdapter(ArrayList<Consultant> consultants, RecyclerViewClickListener listener) {
        this.consultants = consultants;
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
    public Consultants_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        //currently, src only send image name we have to set the image path
        try {
            profilePicSrc = "http://43.205.45.96/img/users/" + profilePicSrc;
            Picasso.get().load(profilePicSrc.replace("https://","http://")).into(holder.profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return consultants.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }


}