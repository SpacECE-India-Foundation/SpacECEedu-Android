package com.spacECE.spaceceedu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacECE.spaceceedu.Consultants.Consultant;
import com.spacECE.spaceceedu.Consultants.Consultant_Main;

import java.util.ArrayList;

public class Consultants_MyConsultant extends Fragment {

    RecyclerView RecyclerviewMyConsultants;
    private ArrayList<Consultant> consultants = new ArrayList<>();

    public Consultants_MyConsultant() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //consultants = Consultant_Main.;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_consultants__my_consultant, container, false);



        return v;
    }
}