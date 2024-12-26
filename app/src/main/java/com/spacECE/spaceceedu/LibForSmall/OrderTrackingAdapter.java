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
        TextView orderId, orderStatus, orderDate;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.textView_orderId);
            orderStatus = itemView.findViewById(R.id.textView_orderStatus);
            orderDate = itemView.findViewById(R.id.textView_orderDate);
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
        holder.orderId.setText(order.getOrderId());
        holder.orderStatus.setText(order.getOrderStatus());
        holder.orderDate.setText(order.getOrderDate());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


}
