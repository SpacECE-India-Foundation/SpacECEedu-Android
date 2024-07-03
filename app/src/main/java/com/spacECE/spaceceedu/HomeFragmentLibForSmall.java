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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.spacECE.spaceceedu.LibForSmall.AddBooks;
import com.spacECE.spaceceedu.LibForSmall.LibraryProductDetailed;
import com.spacECE.spaceceedu.LibForSmall.Library_main;
import com.spacECE.spaceceedu.LibForSmall.books;
import com.spacECE.spaceceedu.LibForSmall.library_RecycleAdapter;

import java.util.ArrayList;

public class HomeFragmentLibForSmall extends Fragment {
    private ImageView addBooksbtn;
    private RecyclerView ListRecyclerView;
    private EditText searchBar;
    private library_RecycleAdapter adapter;
    private library_RecycleAdapter.RecyclerViewClickListener listener;
    private ArrayList<books> originalList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_lib_for_small, container, false);
        v.setBackgroundColor(Color.WHITE);

        CardView btnfilter = v.findViewById(R.id.btn_lfs_filter);
        btnfilter.setOnClickListener(v1 -> Toast.makeText(getContext(), "Filter according to your preferences", Toast.LENGTH_SHORT).show());

        LinearLayout btnsort = v.findViewById(R.id.btn_lfs_sort);
        btnsort.setOnClickListener(v12 -> Toast.makeText(getContext(), "Sort according to your preferences", Toast.LENGTH_SHORT).show());

        ListRecyclerView = v.findViewById(R.id.recycler_view_libs_for_small_home);
        searchBar = v.findViewById(R.id.search_bar);

        originalList = Library_main.list;
        setAdapter(originalList);

        addBooksbtn = v.findViewById(R.id.btn_add_books_libs_for_small);
        addBooksbtn.setOnClickListener(v13 -> {
            // Create a new fragment instance
            Fragment newFragment = new AddBooks();

            // Replace the current fragment with the new one
            FragmentManager fragmentManager = getParentFragmentManager(); // Use getChildFragmentManager() if inside a child fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.libs_for_small_fragment_container, newFragment) // R.id.fragment_container is the id of the container in your layout
                    .addToBackStack(null)  // Add the transaction to the back stack
                    .commit();
        });

        // Set up search functionality
        setupSearchBar();

        return v;
    }

    private void setupSearchBar() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing before text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing after text is changed
            }
        });
    }

    private void filter(String text) {
        ArrayList<books> filteredList = new ArrayList<>();
        for (books item : originalList) {
            if (item.getProduct_title().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.setData(filteredList);
    }

    private void setAdapter(ArrayList<books> myList) {
        setOnClickListener();
        adapter = new library_RecycleAdapter(myList, listener);
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
