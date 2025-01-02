package com.spacECE.spaceceedu.LibForSmall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class OrderTrackingFragment extends Fragment {
    private RecyclerView ordersTrackRv;
    private ArrayList<Order> orderTrackList;
    private OrderTrackingAdapter orderTrackingAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_order_tracking, container, false);

        ordersTrackRv = v.findViewById(R.id.ordersTrackRv);

        // Fetching arguments passed to this fragment
        ArrayList<String> bookNames = getArguments() != null ? getArguments().getStringArrayList("bookNames") : new ArrayList<>();
        ArrayList<String> bookQuantities = getArguments() != null ? getArguments().getStringArrayList("bookQuantities") : new ArrayList<>();
        ArrayList<String> bookPrices = getArguments() != null ? getArguments().getStringArrayList("bookPrices") : new ArrayList<>();
        int totalPrice = getArguments() != null ? getArguments().getInt("totalPrice"): 0;

        TextView totalPriceTextView = v.findViewById(R.id.ordersTotal);
        totalPriceTextView.setText("Total: ₹ " + totalPrice);

        orderTrackList = new ArrayList<>();

        // Populate the orderTrackList by combining book details
        for (int i = 0; i < bookNames.size(); i++) {
            String bookName = bookNames.get(i);
            String bookQuantity = i < bookQuantities.size() ? bookQuantities.get(i) : "N/A";
            String bookPrice = i < bookPrices.size() ? bookPrices.get(i) : "N/A";
            orderTrackList.add(new Order(bookName, bookQuantity+" pcs","₹ " + bookPrice));
        }

        orderTrackingAdapter = new OrderTrackingAdapter(orderTrackList);
        ordersTrackRv.setLayoutManager(new LinearLayoutManager(getContext()));
        ordersTrackRv.setAdapter(orderTrackingAdapter);
        return v;
    }

    public static OrderTrackingFragment newInstance(ArrayList<String> bookNames, ArrayList<String> bookQuantities, ArrayList<String> bookPrices, int totalPrice) {
        OrderTrackingFragment fragment = new OrderTrackingFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("bookNames", bookNames);
        args.putStringArrayList("bookQuantities", bookQuantities);
        args.putStringArrayList("bookPrices", bookPrices);
        args.putInt("totalPrice", totalPrice);
        fragment.setArguments(args);
        return fragment;
    }

}
