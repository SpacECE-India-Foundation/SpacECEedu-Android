package com.spacECE.spaceceedu.LibForSmall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class library_list extends Fragment {
    private RecyclerView ListRecyclerView;
    private library_RecycleAdapter.RecyclerViewClickListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_library_itemlist, container, false);
        v.setBackgroundColor(Color.WHITE);

        Window window = getActivity().getWindow();
        window.setStatusBarColor(Color.rgb(200,100,50));



        ListRecyclerView = v.findViewById(R.id.library_RecyclerView);
        ArrayList<books> list = Library_main.list;
        setAdapter( list);

        return v;
    }
    private void setAdapter(ArrayList<books> myList) {
        setOnClickListener();
        library_RecycleAdapter adapter = new library_RecycleAdapter(myList, listener);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, LinearLayoutManager.VERTICAL, false);
        ListRecyclerView.setLayoutManager(layoutManager);
        ListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ListRecyclerView.setAdapter(adapter);
    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            Intent intent = new Intent(getContext(), libraryDetailed.class);
            intent.putExtra("pos", position);
            startActivity(intent);
        };
    }

}
