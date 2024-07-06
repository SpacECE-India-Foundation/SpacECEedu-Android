package com.spacECE.spaceceedu.LearnOnApp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.spacECE.spaceceedu.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LearnOn_List_RecycleAdapter_MyCourse extends RecyclerView.Adapter<LearnOn_List_RecycleAdapter_MyCourse.MyViewHolder> {

    private ArrayList<Learn> Llist;
    private final RecyclerViewClickListener listener;

    public LearnOn_List_RecycleAdapter_MyCourse(ArrayList<Learn> myList, RecyclerViewClickListener listener) {
        this.Llist = myList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tv_category, duration, price;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_category = view.findViewById(R.id.LearnOn_List_ListItem_TextView_CategoryName);
            duration = view.findViewById(R.id.ShowCourseStartingDate);
            price = view.findViewById(R.id.ShowCoursePrice);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.learnon_list_list_item_for_main_course, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LearnOn_List_RecycleAdapter_MyCourse.MyViewHolder holder, int position) {
        Learn learn = Llist.get(position);
        Log.d("Adapter Bind", "Binding: " + learn.toString());
        holder.tv_category.setText(learn.getTitle());
        holder.price.setText(learn.getPrice());
        holder.duration.setText(learn.getDuration());
    }

    @Override
    public int getItemCount() {
        return Llist != null ? Llist.size() : 0;
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}