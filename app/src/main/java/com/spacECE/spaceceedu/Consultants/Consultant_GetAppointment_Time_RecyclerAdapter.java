package com.spacECE.spaceceedu.Consultants;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.Consultant_GetAppointment;
import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class Consultant_GetAppointment_Time_RecyclerAdapter extends RecyclerView.Adapter<Consultant_GetAppointment_Time_RecyclerAdapter.ViewHolder> {

    ArrayList<String> timings = new ArrayList<>();
    RecyclerViewClickListener listener;


    public Consultant_GetAppointment_Time_RecyclerAdapter(ArrayList<String> timeList, Consultant_GetAppointment_Time_RecyclerAdapter.RecyclerViewClickListener listener) {
        this.timings=timeList;
        this.listener= listener;
        Log.i("TIME ADAPTER : ", "Initiated....");
    }


    @NonNull
    @Override
    public Consultant_GetAppointment_Time_RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.consultant_get_appointment_list_item_time, parent,false);
        return new Consultant_GetAppointment_Time_RecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Consultant_GetAppointment_Time_RecyclerAdapter.ViewHolder holder, int position) {
        holder.tv_date.setText(timings.get(position));
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_date;

        public ViewHolder(View view) {
            super(view);
            tv_date = view.findViewById(R.id.Consultant_GetAppointment_ListItem_AppointmentTime);
            itemView.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {listener.onClick(view, getAdapterPosition());}
    }

    @Override
    public int getItemCount() {
        return timings.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
