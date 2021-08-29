package com.spacECE.spaceceedu.Consultants;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class Fragment_Consultants_MyAppointments extends Fragment {

    public static ArrayList<UserAppointments> myAppointments = new ArrayList<>();

    private RecyclerView recyclerView;
    private Consultants_MyConsultants_RecyclerViewAdapter.RecyclerViewClickListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_consultants__my_consultant, container, false);

        recyclerView = v.findViewById(R.id.Consultants_MyConsultants_RecyclerView);

        setAdapter(myAppointments);
        return v;
    }

    private void setAdapter(ArrayList<UserAppointments> list) {
        Log.i("SetAdapter:", "Working");
        setOnClickListener();
        Consultants_MyConsultants_RecyclerViewAdapter adapter = new Consultants_MyConsultants_RecyclerViewAdapter(list, listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        Log.i("Adapter", "Executed");
    }

    private void setOnClickListener() {
        listener = new Consultants_MyConsultants_RecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
//                Intent intent = new Intent(getContext(), ConsultantProfile.class);
//                intent.putExtra("position", position);
//                startActivity(intent);
            }
        };
    }
}