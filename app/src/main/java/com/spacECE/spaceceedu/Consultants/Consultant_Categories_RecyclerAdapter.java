package com.spacECE.spaceceedu.Consultants;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class Consultant_Categories_RecyclerAdapter extends RecyclerView.Adapter<Consultant_Categories_RecyclerAdapter.MyViewHolder>{

    ArrayList<ConsultantCategory> categories;

    private RecyclerViewClickListener listener;

    public Consultant_Categories_RecyclerAdapter(ArrayList<ConsultantCategory> categories, Consultant_Categories_RecyclerAdapter.RecyclerViewClickListener listener) {
        this.categories = categories;
        this.listener = listener;
        Log.i("ADAPTER WORKING : ","STARTED");
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_category;
        private ImageView iv_icon;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_category = view.findViewById(R.id.Consultant_Categories_ListItem_TextView_CategoryName);
            iv_icon = view.findViewById(R.id.Consultant_Categories_ListItem_Icon);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {listener.onClick(view, getAdapterPosition()); }
    }

    @NonNull
    @Override
    public Consultant_Categories_RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.consultant_category_list_item, parent, false);
        return new Consultant_Categories_RecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_category.setText(categories.get(position).getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}