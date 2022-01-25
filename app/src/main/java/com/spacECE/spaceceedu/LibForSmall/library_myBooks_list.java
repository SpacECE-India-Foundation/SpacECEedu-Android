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

public class library_myBooks_list extends Fragment {
    private RecyclerView mybooksrclview;

    private library_mybook_recyclerAdapter.RecyclerViewClickListener listener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_library_mybooks_listiten, container, false);
        v.setBackgroundColor(Color.WHITE);

        Window window = getActivity().getWindow();
        window.setStatusBarColor(Color.rgb(200,100,50));



        mybooksrclview = v.findViewById(R.id.library_MyBooks_RecyclerView);
        ArrayList<books> list = Library_main.list;
        setAdapter(list);

        return v;
    }
    private void setAdapter(ArrayList<books> mylist) {
        setOnClickListener();
        library_mybook_recyclerAdapter adapter = new library_mybook_recyclerAdapter(mylist, listener);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, LinearLayoutManager.VERTICAL, false);
        mybooksrclview.setLayoutManager(layoutManager);
        mybooksrclview.setItemAnimator(new DefaultItemAnimator());
        mybooksrclview.setAdapter(adapter);
    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            Intent intent = new Intent(getContext(), libraryDetailed.class);
            intent.putExtra("pos", position);
            startActivity(intent);
        };
    }

}
