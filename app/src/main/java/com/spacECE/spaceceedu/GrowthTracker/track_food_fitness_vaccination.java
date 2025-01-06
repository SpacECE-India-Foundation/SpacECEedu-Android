package com.spacECE.spaceceedu.GrowthTracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.guilhe.views.CircularProgressView;
import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Authentication.UserLocalStore;
import com.spacECE.spaceceedu.ConsultUS.ConsultUs_SplashScreen;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.ConfigUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class track_food_fitness_vaccination extends AppCompatActivity {

    CircularProgressView circularProgressView_food, circularProgressView_vaccinations, circularProgressView_fitness;
    Button seemore_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_track_food_fitness_vaccination);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        circularProgressView_food = findViewById(R.id.growth_tracker_circular_pie_food);
        circularProgressView_vaccinations = findViewById(R.id.growth_tracker_circular_pie_vaccinations);
        circularProgressView_fitness = findViewById(R.id.growth_tracker_circular_pie_fitness);
        seemore_btn = findViewById(R.id.tracker_seemore_btn);

        seemore_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: write link to next page here
                Intent intent = new Intent(getApplicationContext(), GrowthTrackerReport1.class);
                startActivity(intent);
            }
        });

        fetchData();
    }

    private void fetchData() {
        try {
            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                String baseUrl= config.getString("BASE_URL");
                String growthFetchWeeklyDataUrl = config.getString("GROWTH_FETCHWEEKLYDATA");
                UserLocalStore userLocalStore = new UserLocalStore(getApplicationContext());
                Account account = userLocalStore.getLoggedInAccount();
                String accountId = account.getAccount_id();

                OkHttpClient client = new OkHttpClient();
                String todayDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
                Request request = new Request.Builder()
                        .url(baseUrl+growthFetchWeeklyDataUrl+accountId+"&date=" + todayDate)
                        .build();

                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String jsonResponse = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(jsonResponse);
                                String status = jsonObject.getString("status");
                                if ("success".equals(status)) {
                                    JSONArray dataArray = jsonObject.getJSONArray("data");

                                    int totalWaterIntake = 0;
                                    int totalOutdoorPlay = 0;
                                    int totalFruits = 0;
                                    int totalVegetables = 0;
                                    int totalScreenTime = 0;
                                    int totalSleepTime = 0;

                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject detail = dataArray.getJSONObject(i);
                                        totalWaterIntake += detail.getInt("water_intake");
                                        totalOutdoorPlay += detail.getInt("outdoor_play");
                                        totalFruits += detail.getInt("fruits");
                                        totalVegetables += detail.getInt("vegetables");
                                        totalScreenTime += detail.getInt("screen_time");
                                        totalSleepTime += detail.getInt("sleep_time");
                                    }

                                    // Extract vaccination status
                                    JSONObject vaccinationStatus = jsonObject.getJSONObject("vaccination_status");
                                    int totalVaccines = vaccinationStatus.getInt("total_vaccines");
                                    int vaccinated = vaccinationStatus.getInt("vaccinated");
                                    int notVaccinated = vaccinationStatus.getInt("not_vaccinated");

                                    calcPercentage(totalWaterIntake/1000, totalFruits, totalVegetables, 1);
                                    calcPercentage(totalVaccines, vaccinated, notVaccinated, 2);
                                    calcPercentage(totalOutdoorPlay, totalScreenTime, totalSleepTime, 3);

                                    TextView water = findViewById(R.id.track_food_water_lbl);
                                    TextView fruit = findViewById(R.id.track_food_fruits_lbl);
                                    TextView vege = findViewById(R.id.track_food_vegetables_lbl);
                                    TextView total_vacci = findViewById(R.id.track_vacci_total_lbl);
                                    TextView vaccinated_lbl = findViewById(R.id.track_vacci_vaccinated_lbl);
                                    TextView notvaccinated = findViewById(R.id.track_vacci_notvaccinated_lbl);
                                    TextView playtime_lbl = findViewById(R.id.track_fitness_play_lbl);
                                    TextView screentime_lbl = findViewById(R.id.track_fitness_screen_lbl);
                                    TextView sleeptime_lbl = findViewById(R.id.track_fitness_sleep_lbl);
                                    int finalTotalWaterIntake = totalWaterIntake;
                                    int finalTotalFruits = totalFruits;
                                    int finalTotalVegetables = totalVegetables;
                                    int finalTotalSleepTime = totalSleepTime;
                                    int finalTotalScreenTime = totalScreenTime;
                                    int finalTotalOutdoorPlay = totalOutdoorPlay;
                                    runOnUiThread(() -> {
                                        water.setText("Water: " + finalTotalWaterIntake / 1000.0 + "L");
                                        fruit.setText("Fruits: " + finalTotalFruits+"g");
                                        vege.setText("Vegetables: " + finalTotalVegetables+"g");

                                        total_vacci.setText("Total Vaccines: " + totalVaccines);
                                        vaccinated_lbl.setText("Vaccinated: " + vaccinated);
                                        notvaccinated.setText("Not Vaccinated: " + notVaccinated);

                                        playtime_lbl.setText("Play Time: " + finalTotalOutdoorPlay + "H");
                                        screentime_lbl.setText("Screen Time: " + finalTotalScreenTime + "H");
                                        sleeptime_lbl.setText("Sleep Time: " + finalTotalSleepTime + "H");
                                    });


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }
    }


    //this calculates percentage of 3 data and sets it to all pie charts
    //pass 1 in operation to set food chart
    //2 to set vaccinations chart
    //3 to set fitness chart
    private void calcPercentage(float val1, float val2, float val3, int operation) {
        float total = val1 + val2 + val3;


        if (total == 0) {
            Toast.makeText(this, "No data to show percentages", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate percentages
        float percent1 = (val1 / total) * 100;
        float percent2 = (val2 / total) * 100;
        float percent3 = (val3 / total) * 100;

        // Display toasts
//        runOnUiThread(() -> {
        if (operation == 1) {
            set_food_circular_pie_chart(percent1, percent2, percent3);
        } else if (operation == 2) {
            set_vaccinations_circular_pie_chart(percent1, percent2, percent3);
        } else {
            set_fitness_circular_pie_chart(percent1, percent2, percent3);
        }
//        });
    }


    void set_food_circular_pie_chart(float food_water, float food_fruits, float food_vegitables) {
        List<Float> val = new ArrayList<>();
        val.add(food_water);
        val.add(food_fruits);
        val.add(food_vegitables);

        List<Integer> col = new ArrayList<>();
        col.add(getColor(R.color.growth_tracker_water));
        col.add(getColor(R.color.growth_tracker_fruit));
        col.add(getColor(R.color.growth_tracker_vegitable));

        circularProgressView_food.setProgress(val, col);
    }

    void set_vaccinations_circular_pie_chart(float upcomming_vaccinated, float not_vaccinated, float vaccinated) {
        List<Float> val = new ArrayList<>();
        val.add(upcomming_vaccinated);
        val.add(not_vaccinated);
        val.add(vaccinated);

        List<Integer> col = new ArrayList<>();
        col.add(getColor(R.color.growth_tracker_upcoming_vaccine));
        col.add(getColor(R.color.growth_tracker_not_vaccine));
        col.add(getColor(R.color.growth_tracker_vaccinated));

        circularProgressView_vaccinations.setProgress(val, col);
    }

    void set_fitness_circular_pie_chart(float play_time, float screnn_time, float sleep_time) {
        List<Float> val = new ArrayList<>();
        val.add(play_time);
        val.add(screnn_time);
        val.add(sleep_time);

        List<Integer> col = new ArrayList<>();
        col.add(getColor(R.color.growth_tracker_play_time));
        col.add(getColor(R.color.growth_tracker_screen_time));
        col.add(getColor(R.color.growth_tracker_sleep_time));

        circularProgressView_fitness.setProgress(val, col);
    }

}