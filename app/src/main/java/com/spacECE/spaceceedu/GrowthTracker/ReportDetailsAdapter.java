package com.spacECE.spaceceedu.GrowthTracker;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.spacECE.spaceceedu.R;

import java.util.ArrayList;
import java.util.List;

public class ReportDetailsAdapter extends RecyclerView.Adapter<ReportDetailsAdapter.ViewHolder> {

    private Context context;
    private List<ReportDetails> sectionList;

    public ReportDetailsAdapter(Context context, List<ReportDetails> sectionList) {
        this.context = context;
        this.sectionList = sectionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_growth_tracker_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportDetails reportDetails = sectionList.get(position);

        holder.sectionTitle.setText(reportDetails.getTitle());

        List<BarEntry> entries = new ArrayList<>();
        int[] data = reportDetails.getData();
        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            entries.add(new BarEntry(i, data[i]));
            if (data[i] > 60) {
                colors.add(context.getResources().getColor(R.color.blue));
            } else if (data[i] >= 30 && data[i] <= 60) {
                colors.add(context.getResources().getColor(R.color.yellow));
            } else {
                colors.add(context.getResources().getColor(R.color.red2));
            }
        }

        BarDataSet dataSet = new BarDataSet(entries, reportDetails.getTitle());
        dataSet.setColors(colors);
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.7f); // Set bar width to make bars slightly less wide
        holder.barChart.setData(barData);

        // Set X-axis labels
        XAxis xAxis = holder.barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getDaysOfWeek()));

        // Disable right Y-axis
        YAxis rightAxis = holder.barChart.getAxisRight();
        rightAxis.setEnabled(false);

        // Remove grid lines
        holder.barChart.getXAxis().setDrawGridLines(false);
        holder.barChart.getAxisLeft().setDrawGridLines(false);

        holder.barChart.invalidate(); // Refresh the chart

        holder.getTipsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start the GrowthTrackerReportTipsActivity
                int currentPosition = holder.getAbsoluteAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    ReportDetails clickedItem = sectionList.get(currentPosition);
                    // Log the title or any other detail of the clicked item
                    Log.d("RecyclerView", "Item pressed: " + clickedItem.getTitle());

                    Intent intent = new Intent(context, GrowthTrackerTips.class);
                    intent.putExtra("Category clicked", clickedItem.getTitle());
                    context.startActivity(intent);
                }
            }
        });
    }

    private List<String> getDaysOfWeek() {
        List<String> daysOfWeek = new ArrayList<>();
        daysOfWeek.add("Sun");
        daysOfWeek.add("Mon");
        daysOfWeek.add("Tue");
        daysOfWeek.add("Wed");
        daysOfWeek.add("Thu");
        daysOfWeek.add("Fri");
        daysOfWeek.add("Sat");
        return daysOfWeek;
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView sectionTitle;
        BarChart barChart;
        Button getTipsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionTitle = itemView.findViewById(R.id.section_title);
            barChart = itemView.findViewById(R.id.bar_chart);
            getTipsButton = itemView.findViewById(R.id.button_get_tips);
        }
    }
}