package com.spacECE.spaceceedu.LibForSmall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.R;

import java.util.ArrayList;
import java.util.List;

public class OrderTrackingFragment extends Fragment {
    private RecyclerView ordersTrackRv;
    private ArrayList<Order> orderTrackList;
    private OrderTrackingAdapter orderTrackingAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_order_tracking, container, false);

        ordersTrackRv = v.findViewById(R.id.ordersTrackRv);

        ArrayList<String> bookNames = getArguments() != null ? getArguments().getStringArrayList("bookNames") : new ArrayList<>();
        orderTrackList = new ArrayList<>();
        for (String bookName : bookNames) {
            orderTrackList.add(new Order(bookName, "Booked", "25 Dec 2024"));
        }

        orderTrackingAdapter = new OrderTrackingAdapter(orderTrackList);
        ordersTrackRv.setLayoutManager(new LinearLayoutManager(getContext()));
        ordersTrackRv.setAdapter(orderTrackingAdapter);
        return v;
    }

    public static OrderTrackingFragment newInstance(ArrayList<String> bookNames) {
        OrderTrackingFragment fragment = new OrderTrackingFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("bookNames", bookNames);
        fragment.setArguments(args);
        return fragment;
    }

}
