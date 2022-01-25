package com.spacECE.spaceceedu.LibForSmall;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class library_mybook_recyclerAdapter extends RecyclerView.Adapter<library_mybook_recyclerAdapter.myviewholder> {

    ArrayList<books> list;
    private final library_mybook_recyclerAdapter.RecyclerViewClickListener listener;

    public library_mybook_recyclerAdapter(ArrayList<books> list, RecyclerViewClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_item_myooks, parent, false);
        return new library_mybook_recyclerAdapter.myviewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
//        holder.book_name.setText(list.get(position).getProduct_title());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }

    public class myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView book_name;
        public myviewholder(@NonNull View view) {
            super(view);
            book_name=view.findViewById(R.id.cardview_bookname);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

}
