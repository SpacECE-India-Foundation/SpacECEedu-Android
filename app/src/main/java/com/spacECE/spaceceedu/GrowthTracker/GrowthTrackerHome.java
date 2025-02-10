package com.spacECE.spaceceedu.GrowthTracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
import com.spacECE.spaceceedu.LearnOnApp.LearnOn_List_SplashScreen;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.ConfigUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GrowthTrackerHome extends AppCompatActivity {

    private RecyclerView recyclerView;
    private vaccinationAdapter adapter;
    private List<ItemModel> itemList;

    private ImageView drop1l, drop500ml, drop250ml, drop150ml;

    private final Calendar calendar = Calendar.getInstance();

    private final int currentDate = calendar.get(Calendar.DATE);
    private final int currentMonth = calendar.get(Calendar.MONTH) + 1;
    private final int currentYear = calendar.get(Calendar.YEAR);


    private final HashMap<String, Float> dataList = new HashMap<>();

    {
        dataList.put("Water Intake", 0f);
        dataList.put("OutdoorPlayTime", 0f);
        dataList.put("Fruits", 0F);
        dataList.put("Vegetables", 0f);
        dataList.put("ScreenTime", 0f);
        dataList.put("SleepTime", 0f);
    }

    private final HashMap<String, Float> avgPercent = new HashMap<>();

    {
        avgPercent.put("Sun", 0f);
        avgPercent.put("Mon", 0f);
        avgPercent.put("Tue", 0F);
        avgPercent.put("Wed", 0f);
        avgPercent.put("Thu", 0f);
        avgPercent.put("Fri", 0f);
        avgPercent.put("Sat", 0f);
    }

    // HashMap to store growth factors with initial values of 0
    private final HashMap<String, Float> growthFactors = new HashMap<>();

    {
        growthFactors.put("Water_Intake", 0f);
        growthFactors.put("Outdoor_Play", 0f);
        growthFactors.put("Fruits", 0f);
        growthFactors.put("Vegetables", 0f);
        growthFactors.put("Screen_Time", 0f);
        growthFactors.put("Sleep_Time", 0f);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_tracker_home);


        // Initialize switches and set their states
        initializeSwitch(R.id.switchOutdoorPlay, "Outdoor_Play");
        initializeSwitch(R.id.switchFruits, "Fruits");
        initializeSwitch(R.id.switchVegetable, "Vegetables");
        initializeSwitch(R.id.switchScreenTime, "Screen_Time");
        initializeSwitch(R.id.switchSleepTime, "Sleep_Time");

        // Initialize ImageViews
        drop1l = findViewById(R.id.drop_1l);
        drop500ml = findViewById(R.id.drop_500ml);
        drop250ml = findViewById(R.id.drop_250ml);
        drop150ml = findViewById(R.id.drop_150ml);


        // Set click listeners for ImageViews
        drop1l.setOnClickListener(view -> changeDrawable(drop1l, "1000"));
        drop500ml.setOnClickListener(view -> changeDrawable(drop500ml, "500"));
        drop250ml.setOnClickListener(view -> changeDrawable(drop250ml, "250"));
        drop150ml.setOnClickListener(view -> changeDrawable(drop150ml, "150"));

        updateDates();


        BarChart barChart = findViewById(R.id.idBarChart);

        // Create sample data (replace with your own data)
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 1, "WaterIntake"));
        entries.add(new BarEntry(2, 1, "OutdoorPlayTime"));
        entries.add(new BarEntry(3, 1, "Fruits"));
        entries.add(new BarEntry(4, 1, "Vegetables"));
        entries.add(new BarEntry(5, 1, "ScreenTime"));
        entries.add(new BarEntry(6, 1, "SleepTime"));

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
            private final String[] labels = new String[]{"WaterIntake", "OutdoorPlayTime", "Fruits", "Vegetables", "ScreenTime", "SleepTime"};

            @Override
            public String getFormattedValue(float value) {
                return labels[(int) value - 1];
            }
        });

        // y-axis
        barChart.getAxisLeft().setAxisMinimum(0f); // Minimum value 0
        barChart.getAxisLeft().setAxisMaximum(100f); // Maximum value 100
        barChart.getAxisRight().setEnabled(false); // Disable right axis

        // Refresh chart
        barChart.invalidate();

        barChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GrowthTrackerHome.this, track_food_fitness_vaccination.class);
                startActivity(intent);
            }
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_vaccination);

        // Set up the layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list of items
        itemList = new ArrayList<>();

        //Api call
        fetchVaccinationData();

        // Initialize the adapter
        adapter = new vaccinationAdapter(itemList,this);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
    }



    private void initializeSwitch(int switchId, String switchKey) {
        @SuppressLint("UseSwitchCompatOrMaterialCode") SwitchCompat toggleSwitch = findViewById(switchId);

        // Fetch data from API and set switch state based on fetched data
        fetchSwitchStateFromApi(switchKey, apiChecked -> {
            toggleSwitch.setChecked(apiChecked);

            toggleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    // Delay showing the dialog after the switch animation completes
                    buttonView.postDelayed(() -> {
                        if (toggleSwitch.isChecked()) {
                            showInputDialog(switchKey);
                        }
                    }, 250); // Adjust delay time as needed (milliseconds)
                } else {
                    // If switch is unchecked, set growth factor to 0
                    buttonView.postDelayed(() -> {
                        growthFactors.put(switchKey, 0f);
                        saveDataToDatabase(); // Save updated data
                        updateDates(); // Update UI or other components
                        updateDates();
                    },250);
                }
            });
        });
    }


    private void fetchVaccinationData() {
        try {
            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                String baseUrl= config.getString("BASE_URL");
                String growthVaccineDataUrl = config.getString("GROWTH_FETCHVACCINEDATA");
                String url = baseUrl + growthVaccineDataUrl; // Replace with your API URL
                // Replaced all the "Volley.newRequestQueue(this)" with this new call , it ensure we reuse the existing RequestQueue instead of creating new ones.
                RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                itemList.clear();
                                try {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        String vaccineId = jsonObject.getString("vaccine_id");
                                        String vaccineName = jsonObject.getString("vaccine_name");
                                        String protectsAgainst = jsonObject.getString("protects_against");
                                        String info = jsonObject.getString("info");

                                        itemList.add(new ItemModel(vaccineId, vaccineName, protectsAgainst, info));
                                    }
                                    adapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(GrowthTrackerHome.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(GrowthTrackerHome.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

                requestQueue.add(jsonArrayRequest);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }
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
        int maxValue = 0;

        switch (switchKey) {
            case "Outdoor_Play":
                title = "How many hours did he/she play today?";
                hint = "Enter hours (max 12)";
                maxValue = 12;
                break;
            case "Fruits":
                title = "How many grams of fruits did he/she consume today?";
                hint = "Enter grams (max 1000)";
                maxValue = 1000;
                break;
            case "Vegetables":
                title = "How many grams of vegetables did he/she consume today?";
                hint = "Enter grams";
                maxValue = 1000;
                break;
            case "Screen_Time":
                title = "How much screen time did he/she have today?";
                hint = "Enter hours";
                maxValue = 6;
                break;
            case "Sleep_Time":
                title = "How many hours did he/she sleep last night?";
                hint = "Enter hours";
                maxValue = 12;
                break;
            default:
                title = "Enter your data";
                hint = "Enter value";
                break;
        }
        dialogTitle.setText(title);
        inputText.setHint(hint);

        // Create the dialog without setting the button listeners yet
        builder.setTitle(getDialogTitle(switchKey))
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", (dialog, id) -> {
                    // Reset the switch if canceled.
                    toggleSwitch.setChecked(false);
                    dialog.dismiss();
                });

        // Create the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Now override the positive button listener after the dialog is shown.
        int finalMaxValue = maxValue;
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String inputValue = inputText.getText().toString().trim();

            if (inputValue.isEmpty()) {
                Toast.makeText(this, "Please enter a value.", Toast.LENGTH_SHORT).show();
                // Ensure the switch remains off
                toggleSwitch.setChecked(false);
                return; // Do not dismiss dialog
            }

            try {
                int value = Integer.parseInt(inputValue);
                if (value < 0 || value > finalMaxValue) {
                    Toast.makeText(this, "Value must be between 0 and " + finalMaxValue, Toast.LENGTH_SHORT).show();
                    // Ensure the switch remains off
                    toggleSwitch.setChecked(false);
                    return; // Do not dismiss dialog
                }
                // Valid input: update growthFactors and save to DB
                growthFactors.put(switchKey, (float) value);
                saveDataToDatabase();

                // Dismiss dialog if input is valid
                alertDialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid input. Please enter a number.", Toast.LENGTH_SHORT).show();
                // Ensure the switch remains off
                toggleSwitch.setChecked(false);
                // Do not dismiss dialog
            }
            // Optionally, call updateDates() if needed here.
            updateDates();
        });

        // Add the positive button after overriding its listener.
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
    }

    private int getSwitchId(String switchKey) {
        switch (switchKey) {
            case "Outdoor_Play":
                return R.id.switchOutdoorPlay;
            case "Fruits":
                return R.id.switchFruits;
            case "Vegetables":
                return R.id.switchVegetable;
            case "Screen_Time":
                return R.id.switchScreenTime;
            case "Sleep_Time":
                return R.id.switchSleepTime;
            default:
                return -1; // Handle default case if needed
        }
    }

    private String getDialogTitle(String switchKey) {
        switch (switchKey) {
            case "Water_Intake":
                return "Water Intake";
            case "Outdoor_Play":
                return "OutdoorPlayTime";
            case "Fruits":
                return "Fruits";
            case "Vegetables":
                return "Vegetables";
            case "Screen_Time":
                return "ScreenTime";
            case "Sleep_Time":
                return "SleepTime";
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

        dataSet.setValueTextSize(6f); // Set text size of values

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

        // Emoji section ImageViews
        ImageView[] emojiImageViews = new ImageView[]{
                findViewById(R.id.Sun),
                findViewById(R.id.Mon),
                findViewById(R.id.Tue),
                findViewById(R.id.Wed),
                findViewById(R.id.Thu),
                findViewById(R.id.Fri),
                findViewById(R.id.Sat)
        };

        // Set the first day of the week to Sunday
        Calendar calendarInstance = Calendar.getInstance();
        calendarInstance.setFirstDayOfWeek(Calendar.SUNDAY);

        int currentDayOfWeek = calendarInstance.get(Calendar.DAY_OF_WEEK); // Sunday is 1, Monday is 2, ..., Saturday is 7
        int currentDate = calendarInstance.get(Calendar.DATE);
        int currentMonth = calendarInstance.get(Calendar.MONTH) + 1; // Month is 0-based in Calendar
        int currentYear = calendarInstance.get(Calendar.YEAR);

        for (int i = 0; i < 7; i++) {
            LinearLayout layout = dateLayouts[i];
            TextView dayText = (TextView) layout.getChildAt(0);
            TextView dateText = (TextView) layout.getChildAt(1);

            // Initialize a new Calendar instance for each day
            Calendar dayCalendar = (Calendar) calendarInstance.clone();
            dayCalendar.set(Calendar.DAY_OF_WEEK, i + 1); // Set the day of the week

            String day = new SimpleDateFormat("EEE", Locale.getDefault()).format(dayCalendar.getTime());
            String date = new SimpleDateFormat("d", Locale.getDefault()).format(dayCalendar.getTime());

            dayText.setText(day);
            dateText.setText(date);

            int layoutDate = dayCalendar.get(Calendar.DATE);
            int layoutMonth = dayCalendar.get(Calendar.MONTH) + 1; // Month is 0-based in Calendar
            int layoutYear = dayCalendar.get(Calendar.YEAR);

            // Format the date as yyyy/M/d
            String formattedDate = String.valueOf(layoutYear) + "/" + String.valueOf(layoutMonth) + "/" + String.valueOf(layoutDate);

            if (i == currentDayOfWeek - 1) { // Adjust for zero-based index
                layout.setBackgroundResource(R.drawable.date_select_bg);
                // Fetch data for the current date by default
                Log.e("Date", "Current Date: " + formattedDate);
                fetchDataFromApi(formattedDate);
            } else {
                layout.setBackgroundResource(R.drawable.date_background);
            }

            if (layoutYear > currentYear ||
                    (layoutYear == currentYear && layoutMonth > currentMonth) ||
                    (layoutYear == currentYear && layoutMonth == currentMonth && layoutDate > currentDate)) {
                layout.setEnabled(false);
                layout.setAlpha(0.5f); // Dim the future dates
            } else {
                layout.setEnabled(true);
                layout.setAlpha(1.0f); // Make past and current dates fully visible
                layout.setOnClickListener(v -> {
                    // Reset all backgrounds
                    for (LinearLayout dateLayout : dateLayouts) {
                        dateLayout.setBackgroundResource(R.drawable.date_background);
                    }
                    // Set the background of the clicked layout
                    layout.setBackgroundResource(R.drawable.date_select_bg);
                    // Fetch data for the clicked date
                    String clickedDate = String.valueOf(layoutYear) + "/" + String.valueOf(layoutMonth) + "/" + String.valueOf(layoutDate);
                    Log.e("Date", "Clicked Date: " + clickedDate);
                    fetchDataFromApi(clickedDate);
                });
            }
        }

        // Set the alpha of emoji ImageViews based on the current day
        for (int i = 0; i < emojiImageViews.length; i++) {
            if (i + 1 <= currentDayOfWeek) { // Days of the week in Calendar start from 1 (Sunday) to 7 (Saturday)
                emojiImageViews[i].setAlpha(1f);
            } else {
                emojiImageViews[i].setAlpha(0.0f); // Assuming 0.0 is the default alpha for future dates
            }
        }
        fetchAvgDataForPastWeek();
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
        growthFactors.put("Water_Intake", Float.valueOf(waterIntake));

        saveDataToDatabase();

        updateDates();

    }

    private void applySavedWaterIntakeState(int waterIntake) {
        switch (waterIntake) {
            case 1000:
                drop1l.setImageResource(R.drawable.blue_drop);
                break;
            case 500:
                drop500ml.setImageResource(R.drawable.blue_drop);
                break;
            case 250:
                drop250ml.setImageResource(R.drawable.blue_drop);
                break;
            case 150:
                drop150ml.setImageResource(R.drawable.blue_drop);
                break;
            default:
                // Reset to default if no valid state found
                drop1l.setImageResource(R.drawable.red_drop);
                drop500ml.setImageResource(R.drawable.red_drop);
                drop250ml.setImageResource(R.drawable.red_drop);
                drop150ml.setImageResource(R.drawable.red_drop);
                break;
        }
    }

    private void saveDataToDatabase() {
        try {
            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                String baseUrl= config.getString("BASE_URL");
                String growthInsertGrowthDataUrl = config.getString("GROWTH_INSERTGROWTHDATA");
                // Instantiate the RequestQueue.
                RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
                String url = baseUrl+growthInsertGrowthDataUrl;
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
                        Calendar calendar = Calendar.getInstance();
                        Date date2 = calendar.getTime();
                        // 3 letter name form of the day
                        String day = (new SimpleDateFormat("EE", Locale.ENGLISH).format(date2.getTime()));

                        String date = String.valueOf(currentYear) + "/" + String.valueOf(currentMonth) + "/" + String.valueOf(currentDate);

                        Map<String, String> params = new HashMap<>();
                        params.put("u_id", accountId);
                        params.put("date", date);
                        params.put("water_intake", String.valueOf(growthFactors.get("Water_Intake")));
                        params.put("outdoor_play", String.valueOf(growthFactors.get("Outdoor_Play")));
                        params.put("fruits", String.valueOf(growthFactors.get("Fruits")));
                        params.put("vegetables", String.valueOf(growthFactors.get("Vegetables")));
                        params.put("screen_time", String.valueOf(growthFactors.get("Screen_Time")));
                        params.put("sleep_time", String.valueOf(growthFactors.get("Sleep_Time")));
                        params.put("day", day);
                        params.put("average", String.valueOf(convertToPercentageAndCalculateAverage(growthFactors)));


                        return params;
                    }
                };

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }
    }


    private void fetchDataFromApi(String date) {
        try {
            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                String baseUrl= config.getString("BASE_URL");
                String growthFetchGrowthDataUrl = config.getString("GROWTH_FETCHGROWTHDATA");
                // Instantiate the RequestQueue.
                UserLocalStore userLocalStore = new UserLocalStore(getApplicationContext());
                Account account = userLocalStore.getLoggedInAccount();
                String accountId = account.getAccount_id();
                RequestQueue queue =VolleySingleton.getInstance(this).getRequestQueue();
                String url = baseUrl + growthFetchGrowthDataUrl + accountId + "&date=" + date;

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        response -> {
                            // Handle the response from your PHP API
                            Log.d("FetchDataResponse", response);
                            // Parse the JSON response
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                String status = jsonResponse.getString("status");
                                if (status.equals("success")) {
                                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject dataObject = dataArray.getJSONObject(i);

                                        float waterIntake = dataObject.getInt("water_intake");
                                        float outdoorPlayTime = dataObject.getInt("outdoor_play");
                                        float fruits = dataObject.getInt("fruits");
                                        float vegetables = dataObject.getInt("vegetables");
                                        float screenTime = dataObject.getInt("screen_time");
                                        float sleepTime = dataObject.getInt("sleep_time");

                                        // Calculate percentages
                                        float waterIntakePercentage = (waterIntake / 1000f) * 100;
                                        float outdoorPlayPercentage = (outdoorPlayTime / 6f) * 100;
                                        float fruitsPercentage = (fruits / 40f) * 100;
                                        float vegetablesPercentage = (vegetables / 300f) * 100;
                                        float screenTimePercentage = (screenTime / 6f) * 100;
                                        float sleepTimePercentage = (sleepTime / 8f) * 100;

                                        // Update dataList with new percentages
                                        dataList.put("WaterIntake", waterIntakePercentage);
                                        dataList.put("OutdoorPlayTime", outdoorPlayPercentage);
                                        dataList.put("Fruits", fruitsPercentage);
                                        dataList.put("Vegetables", vegetablesPercentage);
                                        dataList.put("ScreenTime", screenTimePercentage);
                                        dataList.put("SleepTime", sleepTimePercentage);

                                        growthFactors.put("Water_Intake", waterIntake);
                                        growthFactors.put("Outdoor_Play", outdoorPlayTime);
                                        growthFactors.put("Fruits", fruits);
                                        growthFactors.put("Vegetables", vegetables);
                                        growthFactors.put("Screen_Time", screenTime);
                                        growthFactors.put("Sleep_Time", sleepTime);

                                    }

                                } else {
                                    String errorMessage = jsonResponse.getString("message");
                                    resetDataList();
                                    Log.e("FetchDataError", errorMessage);
                                    // Handle error case if needed
                                }
                                // Update the bar chart with new data
                                updateBarChart();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            // Handle error
                            Log.e("FetchDataError", "Error while fetching data: " + error.toString());
                        });

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }
    }

    private void fetchSwitchStateFromApi(String switchKey, SwitchStateCallback callback) {
        try {
            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                String baseUrl= config.getString("BASE_URL");
                String growthFetchGrowthDataUrl = config.getString("GROWTH_FETCHGROWTHDATA");

                UserLocalStore userLocalStore = new UserLocalStore(getApplicationContext());
                Account account = userLocalStore.getLoggedInAccount();
                String accountId = account.getAccount_id();
                String date = String.valueOf(currentYear) + "/" + String.valueOf(currentMonth) + "/" + String.valueOf(currentDate);

                RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
                String url = baseUrl + growthFetchGrowthDataUrl + accountId + "&date=" + date;

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        response -> {
                            // Handle the response from your PHP API
                            Log.d("FetchSwitchStateResponse", response);
                            // Parse the JSON response
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                String status = jsonResponse.getString("status");
                                if (status.equals("success")) {
                                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject dataObject = dataArray.getJSONObject(i);

                                        int waterIntake = dataObject.getInt("water_intake");

                                        applySavedWaterIntakeState(waterIntake);
                                    }

                                    if (dataArray.length() > 0) {
                                        JSONObject dataObject = dataArray.getJSONObject(0); // Assuming one entry per day

                                        // Check the value for the switch key
                                        int switchValue = dataObject.getInt(switchKey.toLowerCase()); // Assuming API response key is lowercase

                                        // Update switch state based on fetched data
                                        boolean isChecked = switchValue > 0; // Set switch to true if value is greater than 0
                                        callback.onSwitchStateReceived(isChecked);
                                    } else {
                                        // No data found for the day, set switch state to false
                                        callback.onSwitchStateReceived(false);
                                    }
                                } else {
                                    String errorMessage = jsonResponse.getString("message");
                                    Log.e("FetchSwitchStateError", errorMessage);
                                    applySavedWaterIntakeState(0);
                                    // Handle error case if needed
                                    callback.onSwitchStateReceived(false); // Set switch state to false on error
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                callback.onSwitchStateReceived(false); // Set switch state to false on JSON exception
                            }
                        },
                        error -> {
                            // Handle error
                            Log.e("FetchSwitchStateError", "Error while fetching switch state: " + error.toString());
                            callback.onSwitchStateReceived(false); // Set switch state to false on error
                        });

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }
    }

    // Interface to handle switch state callback
    interface SwitchStateCallback {
        void onSwitchStateReceived(boolean isChecked);
    }

    private void updateBarChart() {
        BarChart barChart = findViewById(R.id.idBarChart);

        // Create entries based on dataList
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, dataList.get("WaterIntake"), "WaterIntake"));
        entries.add(new BarEntry(2, dataList.get("OutdoorPlayTime"), "OutdoorPlayTime"));
        entries.add(new BarEntry(3, dataList.get("Fruits"), "Fruits"));
        entries.add(new BarEntry(4, dataList.get("Vegetables"), "Vegetables"));
        entries.add(new BarEntry(5, dataList.get("ScreenTime"), "ScreenTime"));
        entries.add(new BarEntry(6, dataList.get("SleepTime"), "SleepTime"));

        //find the max percentage value
        float maxValue = 0;
        for (float value : dataList.values()) {
            maxValue = Math.max(maxValue, value);
        }
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
            private final String[] labels = new String[]{"WaterIntake", "OutdoorPlayTime", "Fruits", "Vegetables", "ScreenTime", "SleepTime"};

            @Override
            public String getFormattedValue(float value) {
                return labels[(int) value - 1];
            }
        });

        // y-axis
        barChart.getAxisLeft().setAxisMinimum(0f); // Minimum value 0
        float yAxisMax = maxValue + (maxValue * 0.1f);
        barChart.getAxisLeft().setAxisMaximum(yAxisMax); // Maximum value 100
        barChart.getAxisRight().setEnabled(false); // Disable right axis

        // Refresh chart
        barChart.invalidate();

        Log.e("error", String.valueOf(dataList));
    }

    private float convertToPercentageAndCalculateAverage(HashMap<String, Float> data) {
        float totalPercentage = 0f;
        int count = 0;

        for (Map.Entry<String, Float> entry : data.entrySet()) {
            String key = entry.getKey();
            float value = entry.getValue();
            float percentage = 0f;
            float maxFruits = 200;
            float maxVegetables = 500;

            switch (key) {
                case "Water_Intake":
                    percentage = (value / 1000) * 100;
                    break;
                case "Outdoor_Play":
                    percentage = (value / 6) * 100;
                    break;
                case "Fruits":
                    percentage = (value / maxFruits) * 100;
                    break;
                case "Vegetables":
                    percentage = (value / maxVegetables) * 100;
                    break;
                case "Screen_Time":
                    percentage = (value / 6) * 100;
                    totalPercentage -= percentage; // Subtract percentage for screen time
                    count++; // Increment count for screen time separately
                    continue; // Skip the rest of the loop for screen time
                case "Sleep_Time":
                    percentage = (value / 8) * 100;
                    break;
            }

            totalPercentage += percentage;
            count++;
        }

        // Calculate the average percentage
        return totalPercentage / count;
    }

    private void resetDataList() {
        dataList.put("WaterIntake", 0f);
        dataList.put("OutdoorPlayTime", 0f);
        dataList.put("Fruits", 0f);
        dataList.put("Vegetables", 0f);
        dataList.put("ScreenTime", 0f);
        dataList.put("SleepTime", 0f);

        growthFactors.put("Water_Intake", 0f);
        growthFactors.put("Outdoor_Play", 0f);
        growthFactors.put("Fruits", 0f);
        growthFactors.put("Vegetables", 0f);
        growthFactors.put("Screen_Time", 0f);
        growthFactors.put("Sleep_Time", 0f);
    }

    private void fetchDataAvgFromApi(String date, DataFetchCallback callback) {
        try {
            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                String baseUrl= config.getString("BASE_URL");
                String growthFetchGrowthDataUrl = config.getString("GROWTH_FETCHGROWTHDATA");
                // Instantiate the RequestQueue.
                UserLocalStore userLocalStore = new UserLocalStore(getApplicationContext());
                Account account = userLocalStore.getLoggedInAccount();
                String accountId = account.getAccount_id();
                RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
                String url = baseUrl + growthFetchGrowthDataUrl + accountId + "&date=" + date;

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        response -> {
                            // Handle the response from your PHP API
                            Log.d("FetchDataResponse", response);
                            // Parse the JSON response
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                String status = jsonResponse.getString("status");
                                if (status.equals("success")) {
                                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject dataObject = dataArray.getJSONObject(i);

                                        String day = dataObject.getString("day");
                                        float avg = (float) dataObject.getDouble("average");

                                        Log.d("FetchDataAverage", "Day: " + day + ", Average: " + avg);

                                        avgPercent.put(day, avg);
                                    }

                                    // Log avgPercent after adding values
                                    Log.d("FetchDataAvgPercent", "avgPercent: " + avgPercent);

                                } else {
                                    String errorMessage = jsonResponse.getString("message");
                                    Log.e("FetchDataError", errorMessage);
                                    // Handle error case if needed
                                }

                                // Call the callback to indicate data fetched
                                callback.onDataFetched();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            // Handle error
                            Log.e("FetchDataError", "Error while fetching data: " + error.toString());

                            // Call the callback to indicate data fetched (even if error occurs)
                            callback.onDataFetched();
                        });

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }
    }

    // Interface for callback
    interface DataFetchCallback {
        void onDataFetched();
    }

    private void fetchAvgDataForPastWeek() {
        Calendar calendar = Calendar.getInstance();

        // Counter to track completed API requests
        AtomicInteger requestsCompleted = new AtomicInteger(0);

        // Loop through the past 7 days
        for (int i = 0; i < 7; i++) {
            // Get the date string in the format "yyyy-MM-dd"
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
            String date = dateFormat.format(calendar.getTime());

            // Call your fetchDataAvgFromApi function for this date
            fetchDataAvgFromApi(date, new DataFetchCallback() {
                @Override
                public void onDataFetched() {
                    // Increment the counter
                    int count = requestsCompleted.incrementAndGet();

                    // Check if all requests have completed
                    if (count >= 7) {
                        // All API requests have completed, update drawables
                        updateImageViewDrawables();
                    }
                }
            });

            // Move calendar to the previous day
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
    }


    private void updateImageViewDrawables() {
        ImageView sunImageView = findViewById(R.id.Sun);
        ImageView monImageView = findViewById(R.id.Mon);
        ImageView tueImageView = findViewById(R.id.Tue);
        ImageView wedImageView = findViewById(R.id.Wed);
        ImageView thuImageView = findViewById(R.id.Thu);
        ImageView friImageView = findViewById(R.id.Fri);
        ImageView satImageView = findViewById(R.id.Sat);

        for (String day : avgPercent.keySet()) {
            float percent = avgPercent.get(day);
            ImageView imageView = getImageViewForDay(day);

            if (imageView != null) {
                if (percent > 60) {
                    imageView.setImageResource(R.drawable.happy);
                } else if (percent >= 30) {
                    imageView.setImageResource(R.drawable.sad);
                } else {
                    imageView.setImageResource(R.drawable.bad);
                }
            }
        }
    }

    private ImageView getImageViewForDay(String day) {
        switch (day) {
            case "Sun":
                return findViewById(R.id.Sun);
            case "Mon":
                return findViewById(R.id.Mon);
            case "Tue":
                return findViewById(R.id.Tue);
            case "Wed":
                return findViewById(R.id.Wed);
            case "Thu":
                return findViewById(R.id.Thu);
            case "Fri":
                return findViewById(R.id.Fri);
            case "Sat":
                return findViewById(R.id.Sat);
            default:
                return null;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        updateDates();
    }

    // by Mohit
}