package com.spacECE.spaceceedu.GrowthTracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Authentication.UserLocalStore;
import com.spacECE.spaceceedu.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GrowthTrackerHome extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private ImageView drop1l, drop500ml, drop250ml, drop150ml;

    private final Calendar calendar = Calendar.getInstance();

    private final int currentDate = calendar.get(Calendar.DATE);
    private final int currentMonth = calendar.get(Calendar.MONTH);
    private final int currentYear = calendar.get(Calendar.YEAR);

    // HashMap to store growth factors with initial values of 0
    private HashMap<String, Integer> growthFactors = new HashMap<>();
    {
        growthFactors.put("Water Intake", 0);
        growthFactors.put("Outdoor PlayTime", 0);
        growthFactors.put("Fruits", 0);
        growthFactors.put("Vegetables", 0);
        growthFactors.put("Screen Time", 0);
        growthFactors.put("Sleep Time", 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_tracker_home);

        sharedPreferences = getSharedPreferences("SwitchState", Context.MODE_PRIVATE);

        // Initialize switches and set their states
        initializeSwitch(R.id.switchOutdoorPlay, "Outdoor PlayTime");
        initializeSwitch(R.id.switchFruits, "Fruits");
        initializeSwitch(R.id.switchVegetable, "Vegetables");
        initializeSwitch(R.id.switchScreenTime, "Screen Time");
        initializeSwitch(R.id.switchSleepTime, "Sleep Time");

        // Initialize ImageViews
        drop1l = findViewById(R.id.drop_1l);
        drop500ml = findViewById(R.id.drop_500ml);
        drop250ml = findViewById(R.id.drop_250ml);
        drop150ml = findViewById(R.id.drop_150ml);

        // Set click listeners for ImageViews
        drop1l.setOnClickListener(view -> changeDrawable(drop1l,"1000"));
        drop500ml.setOnClickListener(view -> changeDrawable(drop500ml,"500"));
        drop250ml.setOnClickListener(view -> changeDrawable(drop250ml,"250"));
        drop150ml.setOnClickListener(view -> changeDrawable(drop150ml,"150"));

        BarChart barChart = findViewById(R.id.idBarChart);

        // Create sample data (replace with your own data)
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 20, "Water Intake"));
        entries.add(new BarEntry(2, 40, "Outdoor Play Time"));
        entries.add(new BarEntry(3, 80, "Fruits"));
        entries.add(new BarEntry(4, 50, "Vegetables"));
        entries.add(new BarEntry(5, 10, "Screen Time"));
        entries.add(new BarEntry(6, 70, "Sleep Time"));

        // Create a single dataset
        BarDataSet dataSet = getBarDataSet(entries);

        List<IBarDataSet> datasets = new ArrayList<>();
        datasets.add(dataSet);

        BarData barData = new BarData(datasets);
        barData.setBarWidth(0.4f); // Set bar width
        barChart.setData(barData);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);

        // Customize other properties as needed
        barChart.getDescription().setEnabled(false); // Hide description
        barChart.getLegend().setEnabled(false); // Show legend

        // x-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(6f); // Set the size of the labels on the x-axis
        xAxis.setValueFormatter(new ValueFormatter() {
            private final String[] labels = new String[]{"Water Intake", "Outdoor Play Time", "Fruits", "Vegetables", "Screen Time", "Sleep Time"};

            @Override
            public String getFormattedValue(float value) {
                return labels[(int) value - 1];
            }
        });

        // Refresh chart
        barChart.invalidate();

        updateDates();
    }

    private void initializeSwitch(int switchId, String switchKey) {
        @SuppressLint("UseSwitchCompatOrMaterialCode") SwitchCompat toggleSwitch = findViewById(switchId);
        boolean switchState = sharedPreferences.getBoolean(switchKey, false);
        toggleSwitch.setChecked(switchState);

        toggleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(switchKey, isChecked);
            editor.apply();

            if (isChecked) {
                // Delay showing the dialog after the switch animation completes
                buttonView.postDelayed(() -> {
                    if (toggleSwitch.isChecked()) {
                        showInputDialog(switchKey);
                    }
                }, 250); // Adjust delay time as needed (milliseconds)
            }
        });
    }

    private void showInputDialog(String switchKey) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_input, null);
        builder.setView(dialogView);

        EditText inputText = dialogView.findViewById(R.id.input_text);
        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);

        SwitchCompat toggleSwitch = findViewById(getSwitchId(switchKey)); // Get SwitchCompat by switchKey

        // Set dialog title and input hint based on switch key
        String title = "";
        String hint = "";
        switch (switchKey) {
            case "OutdoorPlayTime":
                title = "How many hours did he/she play today?";
                hint = "Enter hours";
                break;
            case "Fruits":
                title = "How many grams of fruits did he/she consume today?";
                hint = "Enter grams";
                break;
            case "Vegetables":
                title = "How many grams of vegetables did he/she consume today?";
                hint = "Enter grams";
                break;
            case "ScreenTime":
                title = "How much screen time did he/she have today?";
                hint = "Enter hours";
                break;
            case "SleepTime":
                title = "How many hours did he/she sleep last night?";
                hint = "Enter hours";
                break;
            default:
                title = "Enter your data";
                hint = "Enter value";
                break;
        }

        dialogTitle.setText(title);
        inputText.setHint(hint);

        builder.setTitle(getDialogTitle(switchKey)) // Update dialog title
                .setPositiveButton("Save", (dialog, id) -> {
                    String inputValue = inputText.getText().toString().trim();

                    // Validate input
                    if (!inputValue.isEmpty()) {
                        try {
                            int value = Integer.parseInt(inputValue);
                            growthFactors.put(switchKey, value);
                            saveDataToDatabase();
                        } catch (NumberFormatException e) {
                            // Handle case where inputValue is not a valid integer
                            e.printStackTrace(); // Log the error for debugging
                            // Optionally show a message to the user about invalid input
                        }
                    }

                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    dialog.dismiss();
                });

        AlertDialog alertDialog = builder.create();


        alertDialog.show();
        // Set a listener to handle switch state if dialog is dismissed
        alertDialog.setOnDismissListener(dialog -> {
            // Ensure toggleSwitch and inputText are properly initialized
            if (toggleSwitch != null) {
                // Update switch state based on input text
                toggleSwitch.setChecked(!inputText.getText().toString().trim().isEmpty());
            }
        });
    }

    private int getSwitchId(String switchKey) {
        switch (switchKey) {
            case "OutdoorPlayTime":
                return R.id.switchOutdoorPlay;
            case "Fruits":
                return R.id.switchFruits;
            case "Vegetables":
                return R.id.switchVegetable;
            case "ScreenTime":
                return R.id.switchScreenTime;
            case "SleepTime":
                return R.id.switchSleepTime;
            default:
                return -1; // Handle default case if needed
        }
    }

    private String getDialogTitle(String switchKey) {
        switch (switchKey) {
            case "WaterIntake":
                return "Water Intake";
            case "OutdoorPlayTime":
                return "Outdoor Play Time";
            case "Fruits":
                return "Fruits";
            case "Vegetables":
                return "Vegetables";
            case "ScreenTime":
                return "Screen Time";
            case "SleepTime":
                return "Sleep Time";
            default:
                return "Input Data";
        }
    }

    @NonNull
    private static BarDataSet getBarDataSet(ArrayList<BarEntry> entries) {
        BarDataSet dataSet = new BarDataSet(entries, "Growth Result");

        // Set specific colors for bars
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#F95F5F")); // Color for bar 1
        colors.add(Color.parseColor("#5B5BA7")); // Color for bar 2
        colors.add(Color.parseColor("#F95F5F")); // Color for bar 3
        colors.add(Color.parseColor("#5B5BA7")); // Color for bar 4
        colors.add(Color.parseColor("#F95F5F")); // Color for bar 5
        colors.add(Color.parseColor("#5B5BA7")); // Color for bar 6

        dataSet.setColors(colors); // Set colors to the dataset

        dataSet.setValueTextSize(8f); // Set text size of values

        // Set custom value formatter to show the label along with the value
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                return barEntry.getData().toString();
            }
        });
        return dataSet;
    }

    private void updateDates() {
        LinearLayout[] dateLayouts = new LinearLayout[]{
                findViewById(R.id.linearLayout3),
                findViewById(R.id.linearLayout5),
                findViewById(R.id.linearLayout9),
                findViewById(R.id.linearLayout4),
                findViewById(R.id.linearLayout8),
                findViewById(R.id.linearLayout7),
                findViewById(R.id.linearLayout6)
        };

        // Set the first day of the week to Sunday
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);

        int currentDay = calendar.get(Calendar.DAY_OF_WEEK); // Sunday is 1, Monday is 2, ..., Saturday is 7
        int currentDate = calendar.get(Calendar.DATE);

        for (int i = 0; i < 7; i++) {
            LinearLayout layout = dateLayouts[i];
            TextView dayText = (TextView) layout.getChildAt(0);
            TextView dateText = (TextView) layout.getChildAt(1);

            calendar.set(Calendar.DAY_OF_WEEK, i + 1); // Set the day of the week
            String day = new SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.getTime());
            String date = new SimpleDateFormat("d", Locale.getDefault()).format(calendar.getTime());

            dayText.setText(day);
            dateText.setText(date);

            if (i == currentDay - 1) { // Adjust for zero-based index
                layout.setBackgroundResource(R.drawable.date_select_bg);
            } else {
                layout.setBackgroundResource(R.drawable.date_background);
            }
        }

        if (currentDay == Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 1);
        }
    }

    private void changeDrawable(ImageView selectedImageView, String waterIntake) {
        // Reset all ImageViews to red_drop
        drop1l.setImageResource(R.drawable.red_drop);
        drop500ml.setImageResource(R.drawable.red_drop);
        drop250ml.setImageResource(R.drawable.red_drop);
        drop150ml.setImageResource(R.drawable.red_drop);

        // Change the drawable of the selected ImageView to blue_drop
        selectedImageView.setImageResource(R.drawable.blue_drop);

        // Update water intake value in HashMap based on selection
        growthFactors.put("Water Intake", Integer.parseInt(waterIntake));

        saveDataToDatabase();

    }

    private void saveDataToDatabase() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://43.205.45.96/Growth_Tracker/api_InsertGrowthDetails.php";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle the response from your PHP API
                    Log.d("SaveDataResponse", response);
                    // Optionally handle response based on your needs
                },
                error -> {
                    // Handle error
                    Log.e("SaveDataError", "Error while saving data: " + error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Parameters to be sent to the PHP API
                // Retrieve accountId from local storage
                UserLocalStore userLocalStore = new UserLocalStore(getApplicationContext());
                Account account = userLocalStore.getLoggedInAccount();
                String accountId = account.getAccount_id();

                String date = String.valueOf(currentDate)+ "/" + String.valueOf(currentMonth)+ "/" + String.valueOf(currentYear);

                Map<String, String> params = new HashMap<>();
                params.put("u_id", accountId);
                params.put("date", date);
                params.put("water_intake", String.valueOf(growthFactors.get("Water Intake")));
                params.put("outdoor_play", String.valueOf(growthFactors.get("Outdoor PlayTime")));
                params.put("fruits", String.valueOf(growthFactors.get("Fruits")));
                params.put("vegetables", String.valueOf(growthFactors.get("Vegetables")));
                params.put("screen_time", String.valueOf(growthFactors.get("Screen Time")));
                params.put("sleep_time", String.valueOf(growthFactors.get("Sleep Time")));
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDates();
    }
}
