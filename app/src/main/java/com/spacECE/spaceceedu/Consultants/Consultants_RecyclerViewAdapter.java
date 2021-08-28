package com.spacECE.spaceceedu.Consultants;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Consultants_RecyclerViewAdapter extends RecyclerView.Adapter<Consultants_RecyclerViewAdapter.MyViewHolder> implements Filterable {
    ArrayList<Consultant> consultants;
    ArrayList<Consultant> AllConsultants,preFilteredList;

    private RecyclerViewClickListener listener;

    public Consultants_RecyclerViewAdapter(ArrayList<Consultant> consultants, RecyclerViewClickListener listener) {
        this.consultants = consultants;
        this.listener = listener;
        AllConsultants = new ArrayList<>(consultants);
        preFilteredList = new ArrayList<>(consultants);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name, rating, price, category;
        private ImageView profile;

        public MyViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.textView_Name);
            profile = view.findViewById(R.id.imageView_ProfilePic);
            category = view.findViewById(R.id.textView_Category);
            rating = view.findViewById(R.id.textView_Rating);
            price = view.findViewById(R.id.textView_Price);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {listener.onClick(view, getAdapterPosition()); }
    }

    @NonNull
    @Override
    public Consultants_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.consultant_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = consultants.get(position).getName();
        String profilePic_src = consultants.get(position).getProfilePic_src();
        String rating = "5";
        String categories = consultants.get(position).getCategories();
        String price = consultants.get(position).getPrice();

        holder.name.setText(name);
        holder.category.setText(categories);
        holder.price.setText(String.valueOf(price)+"/-");
        holder.rating.setText(String.valueOf(rating));
        Picasso.get().load(profilePic_src).into(holder.profile);
    }

    @Override
    public int getItemCount() {
        return consultants.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }


    @Override
    public Filter getFilter(){
        return SearchFilter;
    }

    private Filter SearchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Consultant> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(preFilteredList);
            } else {
                String filteredPattern = charSequence.toString().toLowerCase(Locale.ROOT).trim();

                for (Consultant item : preFilteredList) {
                    if (item.getName().toLowerCase(Locale.ROOT).contains(filteredPattern) || item.getCategories().toString().toLowerCase(Locale.ROOT).contains(filteredPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            consultants.clear();
            consultants.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}