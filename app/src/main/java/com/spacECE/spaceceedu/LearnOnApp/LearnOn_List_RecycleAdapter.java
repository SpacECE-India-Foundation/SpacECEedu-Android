package com.spacECE.spaceceedu.LearnOnApp;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.instamojo.android.Instamojo;
import com.spacECE.spaceceedu.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LearnOn_List_RecycleAdapter extends RecyclerView.Adapter<LearnOn_List_RecycleAdapter.MyViewHolder> {

    ArrayList<Learn> Llist;

    public static String orderID = "0169c0a4-342c-4040-9537-9f7d94c86553";

    private final LearnOn_List_RecycleAdapter.RecyclerViewClickListener listener;


    public LearnOn_List_RecycleAdapter(ArrayList<Learn> myList, RecyclerViewClickListener listener) {
        this.Llist = myList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Instamojo.InstamojoPaymentCallback {
        private final TextView tv_category, duration, price;
        private final Button tv_enroll;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_category = view.findViewById(R.id.LearnOn_List_ListItem_TextView_CategoryName);
            duration = view.findViewById(R.id.ShowCourseStartingDate);
            price = view.findViewById(R.id.ShowCoursePrice);
            view.setOnClickListener(this);
            tv_enroll = view.findViewById(R.id.Enroll);
            tv_enroll.setOnClickListener(v -> {

                Instamojo.getInstance().initialize(view.getContext(), Instamojo.Environment.TEST);
                Instamojo.getInstance().initiatePayment((Activity) view.getContext(), orderID, this);

            });
        }


        @Override
        public void onClick(View view) {listener.onClick(view, getAdapterPosition());



        }

        @Override
        public void onInstamojoPaymentComplete(String s, String s1, String s2, String s3) {

        }

        @Override
        public void onPaymentCancelled() {

        }

        @Override
        public void onInitiatePaymentFailure(String s) {

        }
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
