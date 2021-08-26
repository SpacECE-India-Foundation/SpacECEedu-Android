package com.spacECE.spaceceedu.Consultants;

import android.view.ViewGroup;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class Consultant_GetAppointment_Date_RecyclerAdapter extends RecyclerView.Adapter<Consultant_GetAppointment_Date_RecyclerAdapter.ViewHolder>{

    ArrayList<Consultant_GetAppointment.Appointments> appointments = new ArrayList<>();

    private RecyclerViewClickListener listener;

    public Consultant_GetAppointment_Date_RecyclerAdapter(ArrayList<Consultant_GetAppointment.Appointments> myList, RecyclerViewClickListener listener) {
        this.appointments= myList;
        this.listener=listener;
        Log.i("ADAPTER WORKING : ","STARTED");

    }

    @NonNull
    @Override
    public Consultant_GetAppointment_Date_RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.consultant_get_appointment_list_item, parent,false);
    return new Consultant_GetAppointment_Date_RecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Consultant_GetAppointment_Date_RecyclerAdapter.ViewHolder holder, int position) {
        holder.tv_date.setText(appointments.get(position).getDate());
        holder.tv_month.setText(appointments.get(position).getMonth());
        holder.ll_view.setMinimumWidth(holder.ll_view.getHeight());
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_date;
        private TextView tv_month;
        private LinearLayout ll_view;

        public ViewHolder(View view) {
            super(view);
            tv_date = view.findViewById(R.id.Consultant_GetAppointment_ListItem_AppointmentDate);
            tv_month = view.findViewById(R.id.Consultant_GetAppointment_ListItem_AppointmentMonth);
            ll_view=view.findViewById(R.id.Consultant_GetAppointment_ListItem_LinearLayout);
            itemView.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {listener.onClick(view, getAdapterPosition());}
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
