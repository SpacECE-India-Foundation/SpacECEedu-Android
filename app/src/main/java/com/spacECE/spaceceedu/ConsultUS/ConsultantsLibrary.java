package com.spacECE.spaceceedu.ConsultUS;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class ConsultantsLibrary extends AppCompatActivity {

    public static ArrayList<Consultant> consultantsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Consultants_RecyclerViewAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultants_library);
        getWindow().setStatusBarColor(ContextCompat.getColor(ConsultantsLibrary.this,R.color.black));
        recyclerView = findViewById(R.id.RecycleView);
        setUpAdapter();
    }

    private void setUpAdapter() {
        Log.i("Adapter", "Started");
        setOnClickListener();
        Consultants_RecyclerViewAdapter adapter = new Consultants_RecyclerViewAdapter(consultantsList, listener,ConsultantsLibrary.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        Log.i("Adapter", "Ended");
    }


    private void setOnClickListener() {
        listener = new Consultants_RecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), ConsultantProfile.class);
                intent.putExtra("profile_pic", consultantsList.get(position).getProfilePic_src());
                intent.putExtra("consultant_id", consultantsList.get(position).getConsultant_id());
                intent.putExtra("consultant_name", consultantsList.get(position).getName());
                intent.putExtra("speciality", consultantsList.get(position).getCategories());
                intent.putExtra("chamber", consultantsList.get(position).getAddress());
                intent.putExtra("fee", consultantsList.get(position).getPrice());
                intent.putExtra("language", consultantsList.get(position).getLanguage());
                intent.putExtra("timing_to", consultantsList.get(position).getTiming_end());
                intent.putExtra("timing_from", consultantsList.get(position).getTiming_start());
                intent.putExtra("qualification", consultantsList.get(position).getQualification());
                intent.putExtra("profilePic", consultantsList.get(position).getProfilePic_src());


                intent.putExtra("c_available_from", consultantsList.get(position).getC_available_from());
                intent.putExtra("c_available_to", consultantsList.get(position).getC_available_to());
                intent.putExtra("c_aval_days()", consultantsList.get(position).getC_aval_days());
                startActivity(intent);
            }
        };
    }


}