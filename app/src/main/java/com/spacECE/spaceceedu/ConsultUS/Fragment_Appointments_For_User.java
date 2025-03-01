package com.spacECE.spaceceedu.ConsultUS;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class Fragment_Appointments_For_User extends Fragment {

    public static ArrayList<Appointment> appointmentsArrayList = new ArrayList<>();

    private RecyclerView recyclerView;
    private Appointments_For_User_RecyclerViewAdapter.RecyclerViewClickListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_consultus_appointments_for_user, container, false);

        recyclerView = v.findViewById(R.id.Appointments_For_Users_RecyclerView);

        if(MainActivity.ACCOUNT!=null){
            TextView tv_l=v.findViewById(R.id.MyC_Login);
            tv_l.setVisibility(View.INVISIBLE);
        }
        setAdapter(appointmentsArrayList);
        return v;
    }

    private void setAdapter(ArrayList<Appointment> list) {
        Log.i("SetAdapter:", "Working");
        setOnClickListener();
        Appointments_For_User_RecyclerViewAdapter adapter = new Appointments_For_User_RecyclerViewAdapter(list, listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        if(list.size()==0){
            Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
        }
        Log.i("Adapter", "Executed");
    }

    private void setOnClickListener() {
        listener = new Appointments_For_User_RecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
//                Intent intent = new Intent(v.getContext(), ConsultantProfile.class);
//                intent.putExtra("profile_pic", consultantsList.get(position).getProfilePic_src());
//                intent.putExtra("consultant_id", consultantsList.get(position).getConsultant_id());
//                intent.putExtra("consultant_name", consultantsList.get(position).getName());
//                intent.putExtra("speciality", consultantsList.get(position).getCategories());
//                intent.putExtra("chamber", consultantsList.get(position).getAddress());
//                intent.putExtra("fee", consultantsList.get(position).getPrice());
//                intent.putExtra("language", consultantsList.get(position).getLanguage());
//                intent.putExtra("timing_to", consultantsList.get(position).getTiming_end());
//                intent.putExtra("timing_from", consultantsList.get(position).getTiming_start());
//                intent.putExtra("qualification", consultantsList.get(position).getQualification());
//                intent.putExtra("profilePic", consultantsList.get(position).getProfilePic_src());
//                startActivity(intent);
            }
        };
    }
}