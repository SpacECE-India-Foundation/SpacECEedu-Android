package com.spacECE.spaceceedu.Consultants;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacECE.spaceceedu.R;

import java.util.ArrayList;


public class Consultant_Categories extends Fragment {

   private ArrayList<ConsultantCategory> categories;
   private RecyclerView categoryRecyclerView;
   private Consultant_Categories_RecyclerAdapter adapter;
   private Consultant_Categories_RecyclerAdapter.RecyclerViewClickListener listener;
   private GridLayoutManager gridLayoutManager;


    public Consultant_Categories() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_consultant__categories, container, false);
        Log.i("Categories","Initiated");
 //       generateList();

        categoryRecyclerView = v.findViewById(R.id.Consultant_Category_RecyclerView);
        categories = Consultant_Main.categoryList;
        setAdapter(categories);

        return v;
    }

//    private void generateList() {
//        Log.i("Generate List : "," Generating....");
//        categories.add(new ConsultantCategory("Psycho","Nice"));
//        categories.add(new ConsultantCategory("Surgeon","Nice"));
//        categories.add(new ConsultantCategory("Anesthesia","Nice"));
//        categories.add(new ConsultantCategory("New","Nice"));
//        categories.add(new ConsultantCategory("Old","Nice"));
//        categories.add(new ConsultantCategory("Good","Nice"));
//        categories.add(new ConsultantCategory("Great","Nice"));
//        categories.add(new ConsultantCategory("Cool","Nice"));
//        categories.add(new ConsultantCategory("Kind","Nice"));
//        categories.add(new ConsultantCategory("Unkind","Nice"));
//        categories.add(new ConsultantCategory("Right","Nice"));
//        categories.add(new ConsultantCategory("Down","Nice"));
//        categories.add(new ConsultantCategory("Up","Nice"));
//        setAdapter(categories);
//    }

    private void setAdapter(ArrayList<ConsultantCategory> myList) {
        Log.i("SetAdapter:","Working");
        setOnClickListener();
        adapter = new Consultant_Categories_RecyclerAdapter(myList,listener);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager( getContext(), 2, LinearLayoutManager.VERTICAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryRecyclerView.setAdapter(adapter);
        Log.i("Adapter", "Executed");
    }

    private void setOnClickListener() {
        listener = new Consultant_Categories_RecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getContext(), ConsultantsLibrary.class);
                intent.putExtra("CategoryName", categories.get(position).getCategoryName());
                startActivity(intent);
            }
        };
    }

    public static class ConsultantCategory {
        private String CategoryName,icon;

        public ConsultantCategory(String categoryName, String icon) {
            this.CategoryName = categoryName;
            this.icon = icon;
        }

        public String getIcon() {return icon;}

        public String getCategoryName() {
            return CategoryName;
        }
    }
}
