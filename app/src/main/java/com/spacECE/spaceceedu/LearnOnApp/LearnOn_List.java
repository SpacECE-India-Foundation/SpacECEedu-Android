package com.spacECE.spaceceedu.LearnOnApp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.spacECE.spaceceedu.R;

import java.util.ArrayList;


public class LearnOn_List extends Fragment {

    private RecyclerView ListRecyclerView;
    private LearnOn_List_RecycleAdapter.RecyclerViewClickListener listener;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_learn_on__list, container, false);
        v.setBackgroundColor(Color.WHITE);

        Window window = getActivity().getWindow();
        window.setStatusBarColor(Color.rgb(200,100,50));



        ListRecyclerView = v.findViewById(R.id.LearnOn_List_RecyclerView);
        ArrayList<Learn> llist = LearnOn_Main.Llist;
        setAdapter(llist);

        return v;
    }


    private void setAdapter(ArrayList<Learn> myList) {
        setOnClickListener();
        LearnOn_List_RecycleAdapter adapter = new LearnOn_List_RecycleAdapter(myList, listener);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, LinearLayoutManager.VERTICAL, false);
        ListRecyclerView.setLayoutManager(layoutManager);
        ListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ListRecyclerView.setAdapter(adapter);
    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            Intent intent = new Intent(getContext(), LearnDetailed.class);
            intent.putExtra("pos", position);
            startActivity(intent);



        };
    }

}