package com.spacECE.spaceceedu.Consultants;

import android.graphics.Color;
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

public class Appointments_For_Consultant_RecyclerViewAdapter extends RecyclerView.Adapter<Appointments_For_Consultant_RecyclerViewAdapter.MyViewHolder> {

    ArrayList<Appointment> myConsultants;
    private RecyclerViewClickListener listener;

    public Appointments_For_Consultant_RecyclerViewAdapter(ArrayList<Appointment> myConsultants, RecyclerViewClickListener listener) {
        this.myConsultants = myConsultants;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name, date, status, time;
        private ImageView profile;

        public MyViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.Consultant_Consultants_textView_Name);
            profile = view.findViewById(R.id.Consultant_Consultants_ImageView_ProfilePic);
            status = view.findViewById(R.id.Consultant_Consultants_textView_Status);
            date = view.findViewById(R.id.Consultant_Consultants_textView_Date);
            time = view.findViewById(R.id.Consultant_Consultants_textView_Timing);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {listener.onClick(view, getAdapterPosition()); }
    }

    @NonNull
    @Override
    public Appointments_For_Consultant_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.consultus_appointments_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String name = myConsultants.get(position).getU_name();
        holder.name.setText(name);
        Picasso.get().load(R.drawable.default_profilepic).into(holder.profile);
    }

    @Override
    public int getItemCount() {
        return myConsultants.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }

}