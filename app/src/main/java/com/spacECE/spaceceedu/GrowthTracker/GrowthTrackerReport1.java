package com.spacECE.spaceceedu.GrowthTracker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Authentication.UserLocalStore;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.ConfigUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class GrowthTrackerReport1 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReportDetailsAdapter adapter;
    private List<ReportDetails> sectionList;
    private TextView dateRangeTextView;
    private Calendar currentWeek;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_tracker_report1);

        recyclerView = findViewById(R.id.recycler_view_growth_tracker_report1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dateRangeTextView = findViewById(R.id.date_range);
        ImageView leftArrow = findViewById(R.id.left_arrow);
        ImageView rightArrow = findViewById(R.id.right_arrow);

        currentWeek = Calendar.getInstance();
        updateDateRange();

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentWeek.add(Calendar.WEEK_OF_YEAR, -1);
                updateDateRange();
                fetchDataForCurrentWeek();
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentWeek.add(Calendar.WEEK_OF_YEAR, 1);
                updateDateRange();
                fetchDataForCurrentWeek();
            }
        });

        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        sectionList = new ArrayList<>();
        adapter = new ReportDetailsAdapter(this, sectionList);
        recyclerView.setAdapter(adapter);

        fetchDataForCurrentWeek();
    }

    private void updateDateRange() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
        Calendar startOfWeek = (Calendar) currentWeek.clone();
        startOfWeek.set(Calendar.DAY_OF_WEEK, startOfWeek.getFirstDayOfWeek());
        Calendar endOfWeek = (Calendar) startOfWeek.clone();
        endOfWeek.add(Calendar.DAY_OF_WEEK, 6);
        String dateRange = dateFormat.format(startOfWeek.getTime()) + " to " + dateFormat.format(endOfWeek.getTime());
        dateRangeTextView.setText(dateRange);
    }

    private void fetchDataForCurrentWeek() {
        try {
            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                String baseUrl= config.getString("BASE_URL");
                String growthFetchWeeklyDataUrl = config.getString("GROWTH_FETCHWEEKLYDATA");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String endDate = dateFormat.format(currentWeek.getTime());
                UserLocalStore userLocalStore = new UserLocalStore(this);
                Account account = userLocalStore.getLoggedInAccount();
                String userId = account.getAccount_id();
                String url = baseUrl+growthFetchWeeklyDataUrl+userId+"&date="+endDate;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getString("status").equals("success")) {
                                        sectionList.clear();
                                        JSONArray dataArray = response.getJSONArray("data");

                                        // Initialize the arrays with default values of 0
                                        int[] waterIntake = new int[7];
                                        int[] fruits = new int[7];
                                        int[] vegetables = new int[7];
                                        int[] outdoorPlay = new int[7];
                                        int[] sleepTime = new int[7];
                                        int[] screenTime = new int[7];

                                        for (int i = 0; i < dataArray.length(); i++) {
                                            JSONObject dataObject = dataArray.getJSONObject(i);
                                            // Extract the day index from the date (assuming data is ordered by date)
                                            Calendar date = Calendar.getInstance();
                                            date.setTime(dateFormat.parse(dataObject.getString("date")));
                                            int dayIndex = date.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;

                                            // Update the arrays with actual data
                                            waterIntake[dayIndex] = dataObject.getInt("water_intake");
                                            fruits[dayIndex] = dataObject.getInt("fruits");
                                            vegetables[dayIndex] = dataObject.getInt("vegetables");
                                            outdoorPlay[dayIndex] = dataObject.getInt("outdoor_play");
                                            sleepTime[dayIndex] = dataObject.getInt("sleep_time");
                                            screenTime[dayIndex] = dataObject.getInt("screen_time");
                                        }

                                        sectionList.add(new ReportDetails("Water Intake", waterIntake));
                                        sectionList.add(new ReportDetails("Fruit Intake", fruits));
                                        sectionList.add(new ReportDetails("Vegetable Intake", vegetables));
                                        sectionList.add(new ReportDetails("Outdoor Play", outdoorPlay));
                                        sectionList.add(new ReportDetails("Sleep Time", sleepTime));
                                        sectionList.add(new ReportDetails("Screen Time", screenTime));

                                        adapter.notifyDataSetChanged();
                                    }
                                } catch (JSONException | ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error
                            }
                        });

                requestQueue.add(jsonObjectRequest);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }
    }
}