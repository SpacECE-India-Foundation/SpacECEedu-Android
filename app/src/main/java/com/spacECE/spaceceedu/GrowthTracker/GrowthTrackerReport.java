package com.spacECE.spaceceedu.GrowthTracker;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Authentication.UserLocalStore;
import com.spacECE.spaceceedu.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GrowthTrackerReport extends Fragment {

    private RecyclerView recyclerView;
    private ReportDetailsAdapter adapter;
    private List<ReportDetails> sectionList;
    private TextView dateRangeTextView;
    private Calendar currentWeek;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_growth_tracker_report, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_growth_tracker_report);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dateRangeTextView = view.findViewById(R.id.date_range);
        ImageView leftArrow = view.findViewById(R.id.left_arrow);
        ImageView rightArrow = view.findViewById(R.id.right_arrow);

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

        requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();

        sectionList = new ArrayList<>();
        adapter = new ReportDetailsAdapter(getContext(), sectionList);
        recyclerView.setAdapter(adapter);

        fetchDataForCurrentWeek();

        return view;
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String endDate = dateFormat.format(currentWeek.getTime());
        UserLocalStore userLocalStore = new UserLocalStore(requireContext());
        Account account = userLocalStore.getLoggedInAccount();
        String userId = account.getAccount_id();
        String url = "http://43.205.45.96/Growth_Tracker/api_fetchWeeklyDetails.php?u_id=381&date=2024/7/19";

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
