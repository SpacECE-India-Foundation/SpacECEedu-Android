package com.spacECE.spaceceedu.GrowthTracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.R;

import java.util.List;

public class vaccinationAdapter extends RecyclerView.Adapter<vaccinationAdapter.MyViewHolder> {

    private final List<ItemModel> itemList;
    private final Context context;

    public vaccinationAdapter(List<ItemModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccination_item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemModel item = itemList.get(position);
        holder.protectsAgainst.setText(item.getProtectsAgainst());
        holder.vaccineName.setText(item.getVaccineName());

        holder.itemView.setOnClickListener(v -> showVaccinationDialog(item));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView protectsAgainst;
        TextView vaccineName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            protectsAgainst = itemView.findViewById(R.id.protects_against);
            vaccineName = itemView.findViewById(R.id.vaccine_name);

        }

    }
    private void showVaccinationDialog(ItemModel item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Has your child received the"+item.getVaccineName()+"vaccine?")
                .setPositiveButton("Yes", (dialog, id) -> {
                    // Handle Yes option
                    Toast.makeText(context, "Child is vaccinated", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, id) -> {
                    // Handle No option
                    Toast.makeText(context, "Child is not vaccinated", Toast.LENGTH_SHORT).show();
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}