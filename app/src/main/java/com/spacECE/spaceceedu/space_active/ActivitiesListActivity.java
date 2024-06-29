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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivitiesListActivity extends AppCompatActivity implements ClickListener{
    RecyclerView list_activity;
    ArrayList<space_active_data_holder_all_in_one>arrayList_space_active_all_in_one_data_holder=new ArrayList<space_active_data_holder_all_in_one>();
    ArrayList<space_active_data_holder_all_in_one>free_arrayList_space_active_all_in_one_data_holder=new ArrayList<space_active_data_holder_all_in_one>();
    ArrayList<space_active_data_holder_all_in_one>paid_arrayList_space_active_all_in_one_data_holder=new ArrayList<space_active_data_holder_all_in_one>();
    HashMap<String,ArrayList<space_active_data_holder_all_in_one>>level=new HashMap<>();
    HashMap<String,ArrayList<space_active_data_holder_all_in_one>>domain=new HashMap<>();
    HashMap<String,ArrayList<space_active_data_holder_all_in_one>>key_domain=new HashMap<>();
    HashMap<String,String>activity_completed=new HashMap<>();

    AppCompatButton button_free;
    AppCompatButton button_paid;
    AppCompatButton all_button;
    ArrayList<String>key_domain_list;
    Spinner key_spinner;

    ArrayList<String>dev_domain_list;
    Spinner dev_spinner;



    ArrayList<String>level_list;
    Spinner level_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_list);


        getWindow().setStatusBarColor(ContextCompat.getColor(ActivitiesListActivity.this,R.color.black));
        key_spinner = findViewById(R.id.key_domain_spinner);dev_spinner = findViewById(R.id.dev_domain_spinner);
        list_activity=findViewById(R.id.list_activity);
        level_spinner=findViewById(R.id.level_spinner);
        fetch();

        button_free=findViewById(R.id.button_free);
        button_paid=findViewById(R.id.button_paid);
        all_button=findViewById(R.id.all_button);

        button_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivitiesListActivity.this,free_arrayList_space_active_all_in_one_data_holder.size()+"", Toast.LENGTH_SHORT).show();
                set_free_activities();
                Toast.makeText(ActivitiesListActivity.this, " Refresh Done for free ", Toast.LENGTH_SHORT).show();
            }
        });all_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_all_activities();
                Toast.makeText(ActivitiesListActivity.this, " Refresh Done for all ", Toast.LENGTH_SHORT).show();
            }
        });
        button_paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivitiesListActivity.this,paid_arrayList_space_active_all_in_one_data_holder.size()+"", Toast.LENGTH_SHORT).show();
                set_paid_activities();
                Toast.makeText(ActivitiesListActivity.this, " Refresh Done for paid ", Toast.LENGTH_SHORT).show();
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


        //added for testing


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
    public void set_level_activities(String string) {
        if (string.equals("All")){
            set_all_activities();
        }else {
            ArrayList<space_active_data_holder_all_in_one> local = level.get(string);
            RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne = new RecyclerView_Adapter_activities_all_in_one(local, ActivitiesListActivity.this, ActivitiesListActivity.this, activity_completed);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivitiesListActivity.this);
            list_activity.setLayoutManager(layoutManager);
            list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
        }
    }
    public void set_domain_activities(String string) {
        if (string.equals("All")){
            set_all_activities();
        }else {
            ArrayList<space_active_data_holder_all_in_one> local = domain.get(string);
            RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne = new RecyclerView_Adapter_activities_all_in_one(local, ActivitiesListActivity.this, ActivitiesListActivity.this, activity_completed);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivitiesListActivity.this);
            list_activity.setLayoutManager(layoutManager);
            list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
        }
    }
    public void set_key_domain_activities(String string) {
        if (string.equals("All")){
            set_all_activities();
        }else {
            ArrayList<space_active_data_holder_all_in_one> local=key_domain.get(string);
            RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne=new RecyclerView_Adapter_activities_all_in_one(local,ActivitiesListActivity.this,ActivitiesListActivity.this,activity_completed);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ActivitiesListActivity.this);
            list_activity.setLayoutManager(layoutManager);
            list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
        }
    }
    public void set_free_activities() {
        RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne=new RecyclerView_Adapter_activities_all_in_one(free_arrayList_space_active_all_in_one_data_holder,ActivitiesListActivity.this,ActivitiesListActivity.this,activity_completed);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ActivitiesListActivity.this);
        list_activity.setLayoutManager(layoutManager);
        list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
    }
    public void set_all_activities() {
        RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne=new RecyclerView_Adapter_activities_all_in_one(arrayList_space_active_all_in_one_data_holder,ActivitiesListActivity.this,ActivitiesListActivity.this,activity_completed);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ActivitiesListActivity.this);
        list_activity.setLayoutManager(layoutManager);
        list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
    }
    public void set_paid_activities() {
        RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne=new RecyclerView_Adapter_activities_all_in_one(paid_arrayList_space_active_all_in_one_data_holder,ActivitiesListActivity.this,ActivitiesListActivity.this,activity_completed);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ActivitiesListActivity.this);
        list_activity.setLayoutManager(layoutManager);
        list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
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

    public void fetch(){
        RequestQueue requestQueue= Volley.newRequestQueue(ActivitiesListActivity.this);
        String url="http://43.205.45.96/api/spaceactive_activities.php";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                arrayList_space_active_all_in_one_data_holder.clear();
                free_arrayList_space_active_all_in_one_data_holder.clear();
                paid_arrayList_space_active_all_in_one_data_holder.clear();
                level.clear();
                domain.clear();
                key_domain_list=new ArrayList<>();
                level_list=new ArrayList<>();
                dev_domain_list=new ArrayList<>();
                key_domain.clear();
                JSONArray jsonArray= null;
                try {
                    jsonArray = response.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String activity_no=jsonObject.getString("activity_no");
                        String activity_name=jsonObject.getString("activity_name");
                        String activity_level=jsonObject.getString("activity_level");
                        String activity_dev_domain=jsonObject.getString("activity_dev_domain");
                        String activity_objectives=jsonObject.getString("activity_objectives");
                        String activity_key_dev=jsonObject.getString("activity_key_dev");
                        String activity_material=jsonObject.getString("activity_material");
                        String activity_assessment=jsonObject.getString("activity_assessment");
                        String activity_process=jsonObject.getString("activity_process");
                        String activity_instructions=jsonObject.getString("activity_instructions");
                        //String activity_complete_status=jsonObject.getString("work_done");
                        //String activity_image=jsonObject.getString("image_url");
                        //String activity_video=jsonObject.getString("v_id");
                        String activity_type_status=jsonObject.getString("status");
                        String activity_date=jsonObject.getString("activity_date");
                        String activity_playlist_id=jsonObject.getString("playlist_id");
                        String getActivity_playlist_description=jsonObject.getString("playlist_descr");
                        String getActivity_playlist_name=jsonObject.getString("playlist_name");
                        arrayList_space_active_all_in_one_data_holder.add(new space_active_data_holder_all_in_one(activity_assessment,activity_date,activity_dev_domain,activity_instructions,activity_key_dev,activity_level,activity_material,activity_name,activity_no,activity_objectives,activity_playlist_id,activity_process,activity_type_status,getActivity_playlist_description,getActivity_playlist_name));
                        if (jsonObject.getString("v_id")!=null){
                            arrayList_space_active_all_in_one_data_holder.get(i).setActivity_video(jsonObject.getString("v_id"));
                        }
                        if (jsonObject.getString("image_url")!=null){
                            arrayList_space_active_all_in_one_data_holder.get(i).setActivity_image(jsonObject.getString("image_url"));
                        }
                        if (jsonObject.getString("work_done")!=null){
                            arrayList_space_active_all_in_one_data_holder.get(i).setActivity_complete_status(jsonObject.getString("work_done"));
                        }
                        arrayList_space_active_all_in_one_data_holder.get(i).print_All();
                        if (arrayList_space_active_all_in_one_data_holder.get(i).activity_type_status.equals("free")){
                            free_arrayList_space_active_all_in_one_data_holder.add(arrayList_space_active_all_in_one_data_holder.get(i));
                            Log.e( "Free:>>>>>>>>>>>>>>>>>>>>",arrayList_space_active_all_in_one_data_holder.get(i).activity_no);
                        }else if (arrayList_space_active_all_in_one_data_holder.get(i).activity_type_status.equals("paid")){
                            paid_arrayList_space_active_all_in_one_data_holder.add(arrayList_space_active_all_in_one_data_holder.get(i));
                            Log.e( "paid:>>>>>>>>>>>>>>>>>>>>",arrayList_space_active_all_in_one_data_holder.get(i).activity_no);
                        }
                        if (level.containsKey(arrayList_space_active_all_in_one_data_holder.get(i).activity_level)){
                            Log.e( "Old okok :!!!!!!!!!!!!!!!!!!!!!!!!!",arrayList_space_active_all_in_one_data_holder.get(i).activity_level);
                            level.get(arrayList_space_active_all_in_one_data_holder.get(i).activity_level).add(arrayList_space_active_all_in_one_data_holder.get(i));
                        }else {
                            ArrayList<space_active_data_holder_all_in_one>arrayList=new ArrayList<>();
                            arrayList.add(arrayList_space_active_all_in_one_data_holder.get(i));
                            level.put(arrayList_space_active_all_in_one_data_holder.get(i).activity_level,arrayList);
                            Log.e( "New okokokok :!!!!!!!!!!!!!!",arrayList_space_active_all_in_one_data_holder.get(i).activity_level);
                        }

                        if (domain.containsKey(arrayList_space_active_all_in_one_data_holder.get(i).activity_dev_domain)){
                            Log.e( "Old okok :>>>>>>>>>>>>>>>>>",arrayList_space_active_all_in_one_data_holder.get(i).activity_dev_domain);
                            domain.get(arrayList_space_active_all_in_one_data_holder.get(i).activity_dev_domain).add(arrayList_space_active_all_in_one_data_holder.get(i));
                        }else {
                            ArrayList<space_active_data_holder_all_in_one>arrayList=new ArrayList<>();
                            arrayList.add(arrayList_space_active_all_in_one_data_holder.get(i));
                            Log.e( "New okok :>>>>>>>>>>>>>>>>>",arrayList_space_active_all_in_one_data_holder.get(i).activity_dev_domain);
                            domain.put(arrayList_space_active_all_in_one_data_holder.get(i).activity_dev_domain,arrayList);
                        }

                        if (key_domain.containsKey(arrayList_space_active_all_in_one_data_holder.get(i).activity_key_dev)){
                            Log.e( "Old okok :>>>>>>>>>>>>>>>>>",arrayList_space_active_all_in_one_data_holder.get(i).activity_key_dev);
                            key_domain.get(arrayList_space_active_all_in_one_data_holder.get(i).activity_key_dev).add(arrayList_space_active_all_in_one_data_holder.get(i));
                        } else {
                            ArrayList<space_active_data_holder_all_in_one>arrayList=new ArrayList<>();
                            arrayList.add(arrayList_space_active_all_in_one_data_holder.get(i));
                            Log.e( "New okok :>>>>>>>>>>>>>>>>>",arrayList_space_active_all_in_one_data_holder.get(i).activity_key_dev);
                            key_domain.put(arrayList_space_active_all_in_one_data_holder.get(i).activity_key_dev,arrayList);
                        }
                    }
                    RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne=new RecyclerView_Adapter_activities_all_in_one(arrayList_space_active_all_in_one_data_holder,ActivitiesListActivity.this,ActivitiesListActivity.this,activity_completed);
                    RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ActivitiesListActivity.this);
                    list_activity.setLayoutManager(layoutManager);
                    list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
                    key_domain_list=new ArrayList<>(key_domain.keySet());
                    key_domain_list.add(0,"All");
                    ArrayAdapter<String>arrayAdapter=new ArrayAdapter<>(ActivitiesListActivity.this,R.layout.support_simple_spinner_dropdown_item,key_domain_list);
                    key_spinner.setAdapter(arrayAdapter);


                    dev_domain_list=new ArrayList<>(domain.keySet());
                    dev_domain_list.add(0,"All");
                    ArrayAdapter<String>dev_domain_adapter=new ArrayAdapter<>(ActivitiesListActivity.this,R.layout.support_simple_spinner_dropdown_item,dev_domain_list);
                    dev_spinner.setAdapter(dev_domain_adapter);



                    level_list=new ArrayList<>(level.keySet());
                    level_list.add(0,"All");
                    ArrayAdapter<String>level_adapter=new ArrayAdapter<>(ActivitiesListActivity.this,R.layout.support_simple_spinner_dropdown_item,level_list);
                    level_spinner.setAdapter(level_adapter);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e( "onErrorResponse:------------",error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
        String url1="http://43.205.45.96/spacec_active/api_fetchWorkdone.php?user_id=";
        if (MainActivity.ACCOUNT !=null && MainActivity.ACCOUNT.getAccount_id()!=null){
            JsonObjectRequest jsonObjectRequest1=new JsonObjectRequest(url1 + MainActivity.ACCOUNT.getAccount_id(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    activity_completed.clear();
                    try {
                        JSONArray jsonArray=response.getJSONArray("activities");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            activity_completed.put(jsonObject.getString("activity_no"),jsonObject.getString("workdone"));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e( "onResponse:@@@@@@@@@@@@@@",error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest1);
        }

    }
    @Override
    public void onclick_space_active(int position) {
        Log.e( "onclick_space_active:-----------",position+"-----------");
        try {
            if (arrayList_space_active_all_in_one_data_holder.get(position).activity_video!=null && !arrayList_space_active_all_in_one_data_holder.get(position).activity_video.equals("null")){
                Intent intent=new Intent(ActivitiesListActivity.this, second_page_for_space_active_with_video.class);
                intent.putExtra("activity_no",arrayList_space_active_all_in_one_data_holder.get(position).activity_no);
                intent.putExtra("activity_name",arrayList_space_active_all_in_one_data_holder.get(position).activity_name);
                intent.putExtra("activity_level",arrayList_space_active_all_in_one_data_holder.get(position).activity_level);
                intent.putExtra("activity_dev_domain",arrayList_space_active_all_in_one_data_holder.get(position).activity_dev_domain);
                intent.putExtra("activity_objectives",arrayList_space_active_all_in_one_data_holder.get(position).activity_objectives);
                intent.putExtra("activity_key_dev",arrayList_space_active_all_in_one_data_holder.get(position).activity_key_dev);
                intent.putExtra("activity_material",arrayList_space_active_all_in_one_data_holder.get(position).activity_material);
                intent.putExtra("activity_assessment",arrayList_space_active_all_in_one_data_holder.get(position).activity_assessment);
                intent.putExtra("activity_process",arrayList_space_active_all_in_one_data_holder.get(position).activity_process);
                intent.putExtra("activity_instructions",arrayList_space_active_all_in_one_data_holder.get(position).activity_instructions);
                intent.putExtra("activity_complete_status",arrayList_space_active_all_in_one_data_holder.get(position).activity_complete_status);
                intent.putExtra("activity_image",arrayList_space_active_all_in_one_data_holder.get(position).activity_image);
                intent.putExtra("activity_video",arrayList_space_active_all_in_one_data_holder.get(position).activity_video);
                intent.putExtra("activity_type_status",arrayList_space_active_all_in_one_data_holder.get(position).activity_type_status);
                intent.putExtra("activity_date",arrayList_space_active_all_in_one_data_holder.get(position).activity_date);
                intent.putExtra("playlist_id",arrayList_space_active_all_in_one_data_holder.get(position).activity_playlist_id);
                intent.putExtra("playlist_descr",arrayList_space_active_all_in_one_data_holder.get(position).getActivity_playlist_description);
                intent.putExtra("playlist_name",arrayList_space_active_all_in_one_data_holder.get(position).getActivity_playlist_name);
                startActivity(intent);
            }else {
                Intent intent=new Intent(ActivitiesListActivity.this, second_page_for_space_active_with_image.class);
                intent.putExtra("activity_no",arrayList_space_active_all_in_one_data_holder.get(position).activity_no);
                intent.putExtra("activity_name",arrayList_space_active_all_in_one_data_holder.get(position).activity_name);
                intent.putExtra("activity_level",arrayList_space_active_all_in_one_data_holder.get(position).activity_level);
                intent.putExtra("activity_dev_domain",arrayList_space_active_all_in_one_data_holder.get(position).activity_dev_domain);
                intent.putExtra("activity_objectives",arrayList_space_active_all_in_one_data_holder.get(position).activity_objectives);
                intent.putExtra("activity_key_dev",arrayList_space_active_all_in_one_data_holder.get(position).activity_key_dev);
                intent.putExtra("activity_material",arrayList_space_active_all_in_one_data_holder.get(position).activity_material);
                intent.putExtra("activity_assessment",arrayList_space_active_all_in_one_data_holder.get(position).activity_assessment);
                intent.putExtra("activity_process",arrayList_space_active_all_in_one_data_holder.get(position).activity_process);
                intent.putExtra("activity_instructions",arrayList_space_active_all_in_one_data_holder.get(position).activity_instructions);
                intent.putExtra("activity_complete_status",arrayList_space_active_all_in_one_data_holder.get(position).activity_complete_status);
                intent.putExtra("activity_image",arrayList_space_active_all_in_one_data_holder.get(position).activity_image);
                intent.putExtra("activity_video",arrayList_space_active_all_in_one_data_holder.get(position).activity_video);
                intent.putExtra("activity_type_status",arrayList_space_active_all_in_one_data_holder.get(position).activity_type_status);
                intent.putExtra("activity_date",arrayList_space_active_all_in_one_data_holder.get(position).activity_date);
                intent.putExtra("playlist_id",arrayList_space_active_all_in_one_data_holder.get(position).activity_playlist_id);
                intent.putExtra("playlist_descr",arrayList_space_active_all_in_one_data_holder.get(position).getActivity_playlist_description);
                intent.putExtra("playlist_name",arrayList_space_active_all_in_one_data_holder.get(position).getActivity_playlist_name);
                startActivity(intent);
            }
        }catch (Exception e){
            Log.e( "onclick_space_active: ",e.toString());
        }
    }
}