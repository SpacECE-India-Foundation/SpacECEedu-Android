package com.spacECE.spaceceedu;

import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.spacECE.spaceceedu.LibForSmall.AddBooks;
import com.spacECE.spaceceedu.LibForSmall.LibraryProductDetailed;
import com.spacECE.spaceceedu.LibForSmall.Library_main;
import com.spacECE.spaceceedu.LibForSmall.LibraryProductDetailed;
import com.spacECE.spaceceedu.LibForSmall.books;
import com.spacECE.spaceceedu.LibForSmall.library_RecycleAdapter;

import java.util.ArrayList;

public class HomeFragmentLibForSmall extends Fragment {
    private ImageView addBooksbtn;

    private RecyclerView ListRecyclerView;
    private library_RecycleAdapter.RecyclerViewClickListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_lib_for_small, container, false);
        v.setBackgroundColor(Color.WHITE);

        CardView btnfilter = v.findViewById(R.id.btn_lfs_filter);

        btnfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Filter according to your preferences", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout btnsort = v.findViewById(R.id.btn_lfs_sort);

        btnsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Sort according to your preferences", Toast.LENGTH_SHORT).show();
            }
        });

        ListRecyclerView = v.findViewById(R.id.recycler_view_libs_for_small_home);
        ArrayList<books> list = Library_main.list;
        setAdapter(list);

        addBooksbtn = v.findViewById(R.id.btn_add_books_libs_for_small);
        addBooksbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new fragment instance
                Fragment newFragment = new AddBooks();

                // Replace the current fragment with the new one
                FragmentManager fragmentManager = getParentFragmentManager(); // Use getChildFragmentManager() if inside a child fragment
                fragmentManager.beginTransaction()
                        .replace(R.id.libs_for_small_fragment_container, newFragment) // R.id.fragment_container is the id of the container in your layout
                        .addToBackStack(null)  // Add the transaction to the back stack
                        .commit();
            }
        });

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
            Fragment newFragment = new LibraryProductDetailed();

            // Pass the position as an argument to the new fragment
            Bundle args = new Bundle();
            args.putInt("pos", position);
            newFragment.setArguments(args);

            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.libs_for_small_fragment_container, newFragment)
                    .addToBackStack(null)
                    .commit();
        };
    }
}
