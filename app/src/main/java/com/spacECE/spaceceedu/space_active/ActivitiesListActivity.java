package com.spacECE.spaceceedu.space_active;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.ConfigUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivitiesListActivity extends AppCompatActivity implements ClickListener {
    RecyclerView list_activity;

    // Global list with all activities
    ArrayList<space_active_data_holder_all_in_one> arrayList_space_active_all_in_one_data_holder = new ArrayList<>();

    // Filtered lists
    ArrayList<space_active_data_holder_all_in_one> free_arrayList_space_active_all_in_one_data_holder = new ArrayList<>();
    ArrayList<space_active_data_holder_all_in_one> paid_arrayList_space_active_all_in_one_data_holder = new ArrayList<>();

    HashMap<String, ArrayList<space_active_data_holder_all_in_one>> level = new HashMap<>();
    HashMap<String, ArrayList<space_active_data_holder_all_in_one>> domain = new HashMap<>();
    HashMap<String, ArrayList<space_active_data_holder_all_in_one>> key_domain = new HashMap<>();
    HashMap<String, String> activity_completed = new HashMap<>();

    AppCompatButton button_free;
    AppCompatButton button_paid;
    AppCompatButton all_button;

    ArrayList<String> key_domain_list;
    Spinner key_spinner;

    ArrayList<String> dev_domain_list;
    Spinner dev_spinner;

    ArrayList<String> level_list;
    Spinner level_spinner;

    // This list will always hold the currently displayed items
    ArrayList<space_active_data_holder_all_in_one> currentDisplayedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_list);

        // Set status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivitiesListActivity.this, R.color.black));

        key_spinner = findViewById(R.id.key_domain_spinner);
        dev_spinner = findViewById(R.id.dev_domain_spinner);
        list_activity = findViewById(R.id.list_activity);
        level_spinner = findViewById(R.id.level_spinner);

        button_free = findViewById(R.id.button_free);
        button_paid = findViewById(R.id.button_paid);
        all_button = findViewById(R.id.all_button);

        // Fetch data from server
        fetch();

        button_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_free_activities();
                Toast.makeText(ActivitiesListActivity.this, "Refresh Done for free", Toast.LENGTH_SHORT).show();
            }
        });

        all_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_all_activities();
                Toast.makeText(ActivitiesListActivity.this, "Refresh Done for all", Toast.LENGTH_SHORT).show();
            }
        });

        button_paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_paid_activities();
                if (paid_arrayList_space_active_all_in_one_data_holder.size() == 0) {
                    Toast.makeText(ActivitiesListActivity.this, "No Paid Activities", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(ActivitiesListActivity.this, "Refresh Done for paid", Toast.LENGTH_SHORT).show();
            }
        });

        key_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                set_key_domain_activities(key_domain_list.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        dev_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                set_domain_activities(dev_domain_list.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        level_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                set_level_activities(level_list.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    public void set_level_activities(String selectedLevel) {
        if (selectedLevel.equals("All")) {
            set_all_activities();
        } else {
            ArrayList<space_active_data_holder_all_in_one> local = level.get(selectedLevel);
            // Update the current displayed list
            currentDisplayedList = local;
            RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne =
                    new RecyclerView_Adapter_activities_all_in_one(local, ActivitiesListActivity.this, ActivitiesListActivity.this, activity_completed);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivitiesListActivity.this);
            list_activity.setLayoutManager(layoutManager);
            list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
        }
    }

    public void set_domain_activities(String selectedDomain) {
        if (selectedDomain.equals("All")) {
            set_all_activities();
        } else {
            ArrayList<space_active_data_holder_all_in_one> local = domain.get(selectedDomain);
            currentDisplayedList = local;
            RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne =
                    new RecyclerView_Adapter_activities_all_in_one(local, ActivitiesListActivity.this, ActivitiesListActivity.this, activity_completed);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivitiesListActivity.this);
            list_activity.setLayoutManager(layoutManager);
            list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
        }
    }

    public void set_key_domain_activities(String selectedKeyDomain) {
        if (selectedKeyDomain.equals("All")) {
            set_all_activities();
        } else {
            ArrayList<space_active_data_holder_all_in_one> local = key_domain.get(selectedKeyDomain);
            currentDisplayedList = local;
            RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne =
                    new RecyclerView_Adapter_activities_all_in_one(local, ActivitiesListActivity.this, ActivitiesListActivity.this, activity_completed);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivitiesListActivity.this);
            list_activity.setLayoutManager(layoutManager);
            list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
        }
    }

    public void set_free_activities() {
        currentDisplayedList = free_arrayList_space_active_all_in_one_data_holder;
        RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne =
                new RecyclerView_Adapter_activities_all_in_one(free_arrayList_space_active_all_in_one_data_holder, ActivitiesListActivity.this, ActivitiesListActivity.this, activity_completed);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivitiesListActivity.this);
        list_activity.setLayoutManager(layoutManager);
        list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
    }

    public void set_all_activities() {
        currentDisplayedList = arrayList_space_active_all_in_one_data_holder;
        RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne =
                new RecyclerView_Adapter_activities_all_in_one(arrayList_space_active_all_in_one_data_holder, ActivitiesListActivity.this, ActivitiesListActivity.this, activity_completed);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivitiesListActivity.this);
        list_activity.setLayoutManager(layoutManager);
        list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
        System.out.println("List:" + list_activity);
    }

    public void set_paid_activities() {
        currentDisplayedList = paid_arrayList_space_active_all_in_one_data_holder;
        RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne =
                new RecyclerView_Adapter_activities_all_in_one(paid_arrayList_space_active_all_in_one_data_holder, ActivitiesListActivity.this, ActivitiesListActivity.this, activity_completed);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivitiesListActivity.this);
        list_activity.setLayoutManager(layoutManager);
        list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
        System.out.println("List:" + list_activity);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetch();
    }

    public void fetch() {
        try {
            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                String baseUrl = config.getString("BASE_URL");
                String spaceactiveDataFetchUrl = config.getString("SPACEACTIVE_DATA_FETCH");
                String spaceactiveDataFetchUserUrl = config.getString("SPACEACTIVE_DATA_FETCH_USER");
                RequestQueue requestQueue = Volley.newRequestQueue(ActivitiesListActivity.this);
                String url = baseUrl + spaceactiveDataFetchUrl;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Clear all lists and maps
                        arrayList_space_active_all_in_one_data_holder.clear();
                        free_arrayList_space_active_all_in_one_data_holder.clear();
                        paid_arrayList_space_active_all_in_one_data_holder.clear();
                        level.clear();
                        domain.clear();
                        key_domain.clear();
                        key_domain_list = new ArrayList<>();
                        level_list = new ArrayList<>();
                        dev_domain_list = new ArrayList<>();

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String activity_no = jsonObject.getString("activity_no");
                                String activity_name = jsonObject.getString("activity_name");
                                String activity_level = jsonObject.getString("activity_level");
                                String activity_dev_domain = jsonObject.getString("activity_dev_domain");
                                String activity_objectives = jsonObject.getString("activity_objectives");
                                String activity_key_dev = jsonObject.getString("activity_key_dev");
                                String activity_material = jsonObject.getString("activity_material");
                                String activity_assessment = jsonObject.getString("activity_assessment");
                                String activity_process = jsonObject.getString("activity_process");
                                String activity_instructions = jsonObject.getString("activity_instructions");
                                String activity_type_status = jsonObject.getString("status");
                                String activity_date = jsonObject.getString("activity_date");
                                String activity_playlist_id = jsonObject.getString("playlist_id");
                                String getActivity_playlist_description = jsonObject.getString("playlist_descr");
                                String getActivity_playlist_name = jsonObject.getString("playlist_name");

                                space_active_data_holder_all_in_one activity = new space_active_data_holder_all_in_one(
                                        activity_assessment, activity_date, activity_dev_domain, activity_instructions,
                                        activity_key_dev, activity_level, activity_material, activity_name, activity_no,
                                        activity_objectives, activity_playlist_id, activity_process, activity_type_status,
                                        getActivity_playlist_description, getActivity_playlist_name);

                                // Set video, image, and work done status if available
                                if (jsonObject.getString("v_id") != null) {
                                    activity.setActivity_video(jsonObject.getString("v_id"));
                                }
                                if (jsonObject.getString("image_url") != null) {
                                    activity.setActivity_image(jsonObject.getString("image_url"));
                                }
                                if (jsonObject.getString("work_done") != null) {
                                    activity.setActivity_complete_status(jsonObject.getString("work_done"));
                                }
                                activity.print_All();

                                // Add to global list
                                arrayList_space_active_all_in_one_data_holder.add(activity);

                                // Add to free or paid list
                                if (activity.activity_type_status.equals("free")) {
                                    free_arrayList_space_active_all_in_one_data_holder.add(activity);
                                    Log.e("Free:", "Activity no: " + activity.activity_no);
                                } else if (activity.activity_type_status.equals("paid")) {
                                    paid_arrayList_space_active_all_in_one_data_holder.add(activity);
                                    Log.e("Paid:", "Activity no: " + activity.activity_no);
                                }

                                // Group by level
                                if (level.containsKey(activity.activity_level)) {
                                    level.get(activity.activity_level).add(activity);
                                } else {
                                    ArrayList<space_active_data_holder_all_in_one> list = new ArrayList<>();
                                    list.add(activity);
                                    level.put(activity.activity_level, list);
                                }

                                // Group by domain
                                if (domain.containsKey(activity.activity_dev_domain)) {
                                    domain.get(activity.activity_dev_domain).add(activity);
                                } else {
                                    ArrayList<space_active_data_holder_all_in_one> list = new ArrayList<>();
                                    list.add(activity);
                                    domain.put(activity.activity_dev_domain, list);
                                }

                                // Group by key domain
                                if (key_domain.containsKey(activity.activity_key_dev)) {
                                    key_domain.get(activity.activity_key_dev).add(activity);
                                } else {
                                    ArrayList<space_active_data_holder_all_in_one> list = new ArrayList<>();
                                    list.add(activity);
                                    key_domain.put(activity.activity_key_dev, list);
                                }
                            }

                            // Set adapter with the full list (all activities) and update currentDisplayedList
                            set_all_activities();

                            // Set up the key domain spinner
                            key_domain_list = new ArrayList<>(key_domain.keySet());
                            key_domain_list.add(0, "All");
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ActivitiesListActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, key_domain_list);
                            key_spinner.setAdapter(arrayAdapter);

                            // Set up the developer domain spinner
                            dev_domain_list = new ArrayList<>(domain.keySet());
                            dev_domain_list.add(0, "All");
                            ArrayAdapter<String> dev_domain_adapter = new ArrayAdapter<>(ActivitiesListActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, dev_domain_list);
                            dev_spinner.setAdapter(dev_domain_adapter);

                            // Set up the level spinner
                            level_list = new ArrayList<>(level.keySet());
                            level_list.add(0, "All");
                            ArrayAdapter<String> level_adapter = new ArrayAdapter<>(ActivitiesListActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, level_list);
                            level_spinner.setAdapter(level_adapter);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("onErrorResponse:", error.toString());
                    }
                });
                requestQueue.add(jsonObjectRequest);

                // Fetch user-specific activity completion data
                String url1 = baseUrl + spaceactiveDataFetchUserUrl;
                if (MainActivity.ACCOUNT != null && MainActivity.ACCOUNT.getAccount_id() != null) {
                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(url1 + MainActivity.ACCOUNT.getAccount_id(), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            activity_completed.clear();
                            try {
                                JSONArray jsonArray = response.getJSONArray("activities");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    activity_completed.put(jsonObject.getString("activity_no"), jsonObject.getString("workdone"));
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("User Data Error:", error.toString());
                        }
                    });
                    requestQueue.add(jsonObjectRequest1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }
    }

    @Override
    public void onclick_space_active(int position) {
        Log.e("onclick_space_active:", position + "-----------");
        try {
            // Use the currently displayed list to get the correct activity
            space_active_data_holder_all_in_one item = currentDisplayedList.get(position);

            Intent intent;
            if (item.activity_video != null && !item.activity_video.equals("null")) {
                intent = new Intent(ActivitiesListActivity.this, second_page_for_space_active_with_video.class);
            } else {
                intent = new Intent(ActivitiesListActivity.this, second_page_for_space_active_with_image.class);
            }

            // Pass all required extras
            intent.putExtra("activity_no", item.activity_no);
            intent.putExtra("activity_name", item.activity_name);
            intent.putExtra("activity_level", item.activity_level);
            intent.putExtra("activity_dev_domain", item.activity_dev_domain);
            intent.putExtra("activity_objectives", item.activity_objectives);
            intent.putExtra("activity_key_dev", item.activity_key_dev);
            intent.putExtra("activity_material", item.activity_material);
            intent.putExtra("activity_assessment", item.activity_assessment);
            intent.putExtra("activity_process", item.activity_process);
            intent.putExtra("activity_instructions", item.activity_instructions);
            intent.putExtra("activity_complete_status", item.activity_complete_status);
            intent.putExtra("activity_image", item.activity_image);
            intent.putExtra("activity_video", item.activity_video);
            intent.putExtra("activity_type_status", item.activity_type_status);
            intent.putExtra("activity_date", item.activity_date);
            intent.putExtra("playlist_id", item.activity_playlist_id);
            intent.putExtra("playlist_descr", item.getActivity_playlist_description);
            intent.putExtra("playlist_name", item.getActivity_playlist_name);
            startActivity(intent);
        } catch (Exception e) {
            Log.e("onclick_space_active:", e.toString());
        }
    }
}
