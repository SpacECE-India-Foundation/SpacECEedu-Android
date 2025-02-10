package com.spacECE.spaceceedu.LearnOnApp;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.instamojo.android.Instamojo;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.spacECE.spaceceedu.R;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;

public class LearnOn_List_RecycleAdapter extends RecyclerView.Adapter<LearnOn_List_RecycleAdapter.MyViewHolder>{

    ArrayList<Learn> Llist;

    public static String orderID = "0169c0a4-342c-4040-9537-9f7d94c86553";

    private final LearnOn_List_RecycleAdapter.RecyclerViewClickListener listener;


    public LearnOn_List_RecycleAdapter(ArrayList<Learn> myList, RecyclerViewClickListener listener) {
        this.Llist = myList;
        this.listener = listener;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView tv_category, duration, price;
        private final Button tv_enroll;
        private final View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            tv_category = itemView.findViewById(R.id.LearnOn_List_ListItem_TextView_CategoryName);
            duration = itemView.findViewById(R.id.ShowCourseStartingDate);
            price = itemView.findViewById(R.id.ShowCoursePrice);
            tv_enroll = itemView.findViewById(R.id.Enroll);

            itemView.setOnClickListener(this);
            tv_enroll.setOnClickListener(v -> startPayment());
        }

        private void startPayment() {
            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_KQpgNv8PbMeQk1");

            Activity activity = (Activity) view.getContext();

            try {
                JSONObject options = new JSONObject();
                options.put("name", "SpacECEedu");
                options.put("description", "Education Platform");
                options.put("image", "http://example.com/image/rzp.jpg");
                options.put("theme.color", "#EAAE15");
                options.put("currency", "INR");

                double amount = Double.parseDouble(price.getText().toString());
                int amountInPaise = (int) amount * 100;
                options.put("amount", amountInPaise);

                options.put("prefill.email", "spaceece@gmail.com");
                options.put("prefill.contact", "9988776655");

                JSONObject retryObj = new JSONObject();
                retryObj.put("enabled", true);
                retryObj.put("max_count", 4);
                options.put("retry", retryObj);

                checkout.open(activity, options);

            } catch (Exception e) {
                Log.e("Checkout Error", "Error in starting Razorpay Checkout", e);
            }
        }



        @Override
        public void onClick(View view) {listener.onClick(view, getAdapterPosition());}
    }


    @NonNull
    @Override
    public LearnOn_List_RecycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.learnon_list_list_item, parent, false);
        return new LearnOn_List_RecycleAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.tv_category.setText(Llist.get(position).getTitle());
        holder.price.setText(Llist.get(position).getPrice());
        holder.duration.setText(Llist.get(position).getDuration());
    }

    @Override
    public int getItemCount() {
        return Llist.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }


}