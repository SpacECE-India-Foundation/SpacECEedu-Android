package com.spacECE.spaceceedu.ConsultUS;

import static com.spacECE.spaceceedu.ConsultUS.Consultant_Main.SetDateTimeDay;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Appointments_For_User_RecyclerViewAdapter extends RecyclerView.Adapter<Appointments_For_User_RecyclerViewAdapter.MyViewHolder>{
    ArrayList<Appointment> myConsultants;
    ArrayList<Appointment> AllConsultants,preFilteredList;

    private RecyclerViewClickListener listener;

    public Appointments_For_User_RecyclerViewAdapter(ArrayList<Appointment> myConsultants, RecyclerViewClickListener listener) {
        this.myConsultants = myConsultants;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name, date, time, day;
        private ImageView profile;
        private ImageButton call;

        public MyViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.Consultant_Consultants_textView_Name);
            profile = view.findViewById(R.id.Consultant_Consultants_ImageView_ProfilePic);
            date = view.findViewById(R.id.Consultant_Consultants_textView_Date);
            day = view.findViewById(R.id.Consultant_Consultants_textView_Day);
            time=view.findViewById(R.id.Consultant_Consultants_textView_Timing);
            call=view.findViewById(R.id.bCall);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {listener.onClick(view, getAdapterPosition()); }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.consultus_appointments_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = myConsultants.get(position).getC_name();
        SetDateTimeDay(position, myConsultants, holder.date, holder.time, holder.day);
        String profilePic_src = null;
        holder.name.setText("Dr. "+name);
        Picasso.get().load(R.drawable.default_profilepic).into(holder.profile);
        holder.call.setVisibility(View.VISIBLE);
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = myConsultants.get(holder.getAdapterPosition()).getMobile();
                Log.d("Adapter_User", "phoneNumber: " + phoneNumber);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                v.getContext().startActivity(intent);
            }
            /*@Override
            public void onClick(View v) {
                Log.d("------","consultant id ="+myConsultants.get(holder.getAbsoluteAdapterPosition()).getConsult_id());
                Log.d("------","name ="+myConsultants.get(holder.getAbsoluteAdapterPosition()).getC_name());
                Log.d("------","pic ="+myConsultants.get(holder.getAbsoluteAdapterPosition()).getC_pic());
                Log.d("------","Uid ="+ MainActivity.ACCOUNT.getuId());
                Log.d("------","AccountId ="+ MainActivity.ACCOUNT.getAccount_id());

                Intent intent = new Intent(v.getContext(), Agoraa.class);
                intent.putExtra("c_id", myConsultants.get(holder.getAdapterPosition()).getConsult_id());
                intent.putExtra("c_name", myConsultants.get(holder.getAdapterPosition()).getC_name());
                intent.putExtra("c_pic", myConsultants.get(holder.getAdapterPosition()).getC_pic());
                v.getContext().startActivity(intent);
            } */
        });
    }

    @Override
    public int getItemCount() {
        return myConsultants.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}