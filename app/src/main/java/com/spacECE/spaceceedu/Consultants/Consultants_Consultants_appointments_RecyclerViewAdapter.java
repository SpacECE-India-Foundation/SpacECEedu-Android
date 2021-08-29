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

public class Consultants_Consultants_appointments_RecyclerViewAdapter extends RecyclerView.Adapter<Consultants_Consultants_appointments_RecyclerViewAdapter.MyViewHolder> {
    ArrayList<UserAppointments> myConsultants;

    private RecyclerViewClickListener listener;

    public Consultants_Consultants_appointments_RecyclerViewAdapter(ArrayList<UserAppointments> myConsultants, RecyclerViewClickListener listener) {
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
    public Consultants_Consultants_appointments_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.consultants_consultants_appointments_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = myConsultants.get(position).getP_name();
     //   String profilePic_src = myConsultants.get(position).getProfilePic_src();
        String time = myConsultants.get(position).getTime();
        String status = myConsultants.get(position).getStatus();
        String date = myConsultants.get(position).getDate();
        String month = myConsultants.get(position).getMonth();
        if(status.equalsIgnoreCase("inactive")) {
            holder.status.setText("Payment Pending");
            holder.status.setTextColor(Color.YELLOW);
        }else{
            holder.status.setText("Confirmed");
            holder.status.setTextColor(Color.GREEN);
        }
        holder.name.setText(name);
        holder.date.setText(String.valueOf(date.charAt(8))+date.charAt(9)+" / "+date.charAt(5)+date.charAt(6));
        holder.time.setText(time);
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