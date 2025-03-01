package com.spacECE.spaceceedu.LibForSmall;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class OrderTrackingFragment extends Fragment implements PaymentResultWithDataListener {
    private RecyclerView ordersTrackRv;
    private ArrayList<Order> orderTrackList;
    private OrderTrackingAdapter orderTrackingAdapter;

    private ArrayList<String> bookNames ;
    private ArrayList<String> bookQuantities;
    private ArrayList<String> bookPrices;
    private int totalPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_order_tracking, container, false);
        ordersTrackRv = v.findViewById(R.id.ordersTrackRv);

        // Fetching arguments passed to this fragment
        if(getArguments() != null){
            bookNames = getArguments().getStringArrayList("bookNames");
            bookQuantities = getArguments().getStringArrayList("bookQuantities");
            bookPrices = getArguments().getStringArrayList("bookPrices");
            totalPrice = getArguments().getInt("totalPrice",0);
        }else{
            bookNames = new ArrayList<>();
            bookQuantities = new ArrayList<>();
            bookPrices = new ArrayList<>();
            totalPrice = 0;
        }

        TextView totalPriceTextView = v.findViewById(R.id.ordersTotal);
        totalPriceTextView.setText("Total: ₹ " + totalPrice);

        // Preload the payment interface
        Checkout.preload(requireActivity().getApplicationContext());
        Button btnPay = v.findViewById(R.id.btnPay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });

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

    // Call this method to start the payment process
    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_KQpgNv8PbMeQk1");
        try {
            JSONObject options = new JSONObject();

            options.put("name", "SpacECEedu");
            options.put("description", "Education Platform");
            options.put("image", "http://example.com/image/rzp.jpg");
            //   options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#EAAE15");
            options.put("currency", "INR");
            options.put("amount", totalPrice * 100);//pass amount in currency subunits
            Account account = MainActivity.ACCOUNT;
            if (account != null) {
                Log.d("Number","number is "+account.getContact_number());
                options.put("prefill.email", Account.getUser_email());
                options.put("prefill.contact", account.getContact_number());
            } else {
                // Default values if no user is logged in
                options.put("prefill.email", "default_email@example.com");
                options.put("prefill.contact", "1234567890");
            }

            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(requireActivity(), options);

        } catch(Exception e) {
            Log.e("Checkout Error", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Toast.makeText(requireActivity(),"Payment Success",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(requireActivity(),"Error :"+s,Toast.LENGTH_SHORT).show();
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
