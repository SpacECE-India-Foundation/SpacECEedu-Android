package com.spacECE.spaceceedu.Consultants;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacECE.spaceceedu.Consultants.Consultant;
import com.spacECE.spaceceedu.Consultants.ConsultantProfile;
import com.spacECE.spaceceedu.Consultants.Consultant_Categories;
import com.spacECE.spaceceedu.Consultants.Consultants_MyConsultants_RecyclerViewAdapter;
import com.spacECE.spaceceedu.Consultants.Consultants_RecyclerViewAdapter;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.VideoLibrary.Topic;
import com.spacECE.spaceceedu.VideoLibrary.VideoLibrary_RecyclerViewAdapter_paid;

import java.util.ArrayList;

public class FragmentMyConsultants extends Fragment {

    public static ArrayList<Consultant> myConsultants= new ArrayList<>();

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
        View v= inflater.inflate(R.layout.fragment_consultants__my_consultant, container, false);

        recyclerView= v.findViewById(R.id.Consultants_MyConsultants_RecyclerView);

        setAdapter(myConsultants);
        return v;
    }
    private void setAdapter(ArrayList<Consultant> list) {
        Log.i("SetAdapter:","Working");
        setOnClickListener();
        Consultants_MyConsultants_RecyclerViewAdapter adapter = new Consultants_MyConsultants_RecyclerViewAdapter(list,listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        Log.i("Adapter", "Executed");
    }
    private void setOnClickListener() {
        listener = new Consultants_MyConsultants_RecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getContext(), ConsultantProfile.class);
                intent.putExtra("consultant_name", myConsultants.get(position).getName());
                intent.putExtra("pic_src", myConsultants.get(position).getProfilePic_src());
                intent.putExtra("about", myConsultants.get(position).getAbout());
                intent.putExtra("consultant_id",myConsultants.get(position).getConsultant_id());
                intent.putExtra("categories",myConsultants.get(position).getCategories());

                startActivity(intent);
            }
        };
    }
}