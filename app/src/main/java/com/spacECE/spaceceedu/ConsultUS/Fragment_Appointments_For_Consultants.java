package com.spacECE.spaceceedu.ConsultUS;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class Fragment_Appointments_For_Consultants extends Fragment {

    public static ArrayList<Appointment> appointmentsArrayList = new ArrayList<>();

    private RecyclerView recyclerView;
    private Appointments_For_Consultant_RecyclerViewAdapter.RecyclerViewClickListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_consultus_appointments_for_consultant, container, false);

        recyclerView=v.findViewById(R.id.Appointments_For_Consultant_RecyclerView);
        Log.e("onCreateView:1","++++++++++++++++++++++++");
        setAdapter(appointmentsArrayList);
        return v;
    }

    private void setAdapter(ArrayList<Appointment> list) {
        Log.i("SetAdapter:","Working");
        setOnClickListener();
        Appointments_For_Consultant_RecyclerViewAdapter adapter = new Appointments_For_Consultant_RecyclerViewAdapter(list,listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        if(list.size()==0){
            Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
        }
        Log.i("Adapter", "Executed");
    }

    private void setOnClickListener() {
        listener = new Appointments_For_Consultant_RecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
            }
        };
    }
}