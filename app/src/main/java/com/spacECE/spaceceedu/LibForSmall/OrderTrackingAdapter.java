package com.spacECE.spaceceedu.LibForSmall;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class OrderTrackingAdapter extends RecyclerView.Adapter<OrderTrackingAdapter.OrderViewHolder> {
    private final ArrayList<Order> orderList;

    public OrderTrackingAdapter(ArrayList<Order> orderList){
        this.orderList = orderList;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView bookName, orderQuantity, orderPrice;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.textView_bookName);
            orderQuantity = itemView.findViewById(R.id.textView_orderQuantity);
            orderPrice = itemView.findViewById(R.id.textView_orderPrice);
        }
    }

    @NonNull
    @Override
    public OrderTrackingAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderTrackingAdapter.OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bookName.setText(order.getBookName());
        holder.orderQuantity.setText(order.getOrderQuantity());
        holder.orderPrice.setText(order.getOrderPrice());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


}
