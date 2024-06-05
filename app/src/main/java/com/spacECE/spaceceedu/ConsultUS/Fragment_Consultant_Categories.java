package com.spacECE.spaceceedu.ConsultUS;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.Utils.UsefulFunctions;
import com.spacECE.spaceceedu.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class Fragment_Consultant_Categories extends Fragment {

    private ProgressBar progressBar;

    private ArrayList<ConsultantCategory> categories=new ArrayList<>();
    private RecyclerView categoryRecyclerView;
    private Consultant_Categories_RecyclerAdapter adapter;
    private Consultant_Categories_RecyclerAdapter.RecyclerViewClickListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_consultant__categories, container, false);

        Log.i("Categories", "Initiated");

        categoryRecyclerView = v.findViewById(R.id.Consultant_Category_RecyclerView);

        categories = Consultant_Main.categoryList;

        setAdapter(categories);

        return v;
    }


    private void setAdapter(ArrayList<ConsultantCategory> myList) {
        Log.i("SetAdapter:", "Working");
        setOnClickListener();
        adapter = new Consultant_Categories_RecyclerAdapter(myList, listener,getContext());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, LinearLayoutManager.VERTICAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryRecyclerView.setAdapter(adapter);
        Log.i("Adapter", "Executed");
    }

    private void setOnClickListener() {
        listener = new Consultant_Categories_RecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                progressBar = getActivity().findViewById(R.id.Loading_Consultants);
                progressBar.setVisibility(View.VISIBLE);
                getList(categories.get(position).getCategoryName());
            }
        };
    }

    public void getList(String category) {

        System.out.println(category);

        Thread thread = new Thread(() -> {

            JSONObject apiCall = null;


            try {
                try {
                    apiCall = UsefulFunctions.UsingGetAPI("http://43.205.45.96/ConsultUs/api_getconsultant.php?cat=" + URLEncoder.encode(category, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                JSONArray jsonArray = null;
                try {
                    jsonArray = apiCall.getJSONArray("data");
                    Log.i("API : ", apiCall.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ConsultantsLibrary.consultantsList = new ArrayList<>();
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));

                        Consultant consultant = new Consultant(response_element.getString("u_name"),
                                response_element.getString("u_id"), response_element.getString("image"),
                                response_element.getString("cat_name"), response_element.getString("c_office"),
                                response_element.getString("c_language"), response_element.getString("c_from_time"),
                                response_element.getString("c_to_time"), response_element.getString("c_qualification"),
                                response_element.getString("c_fee"));

                        ConsultantsLibrary.consultantsList.add(consultant);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            Intent intent = new Intent(getContext(), ConsultantsLibrary.class);
            progressBar.setVisibility(View.INVISIBLE);
            startActivity(intent);
        });

        thread.start();

    }

}
