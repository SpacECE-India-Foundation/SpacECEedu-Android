package com.spacECE.spaceceedu.space_active;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_list);

        getWindow().setStatusBarColor(ContextCompat.getColor(ActivitiesListActivity.this,R.color.black));
        list_activity=findViewById(R.id.list_activity);
        fetch();
    }
    public void set_level_activities(String string) {
        ArrayList<space_active_data_holder_all_in_one> local=level.get(string);
        Log.e( "onResponse:1!!!!!!!!!!!!!!!","!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne=new RecyclerView_Adapter_activities_all_in_one(local,ActivitiesListActivity.this,ActivitiesListActivity.this,activity_completed);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ActivitiesListActivity.this);
        list_activity.setLayoutManager(layoutManager);
        list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
        Log.e( "onResponse:2!!!!!!!!!!!!!!","!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
    public void set_domain_activities(String string) {
        ArrayList<space_active_data_holder_all_in_one> local=domain.get(string);
        Log.e( "onResponse:1!!!!!!!!!!!!!!!","!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne=new RecyclerView_Adapter_activities_all_in_one(local,ActivitiesListActivity.this,ActivitiesListActivity.this,activity_completed);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ActivitiesListActivity.this);
        list_activity.setLayoutManager(layoutManager);
        list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
        Log.e( "onResponse:2!!!!!!!!!!!!!!","!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
    public void set_key_domain_activities(String string) {
        ArrayList<space_active_data_holder_all_in_one> local=key_domain.get(string);
        Log.e( "onResponse:1!!!!!!!!!!!!!!!","!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne=new RecyclerView_Adapter_activities_all_in_one(local,ActivitiesListActivity.this,ActivitiesListActivity.this,activity_completed);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ActivitiesListActivity.this);
        list_activity.setLayoutManager(layoutManager);
        list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
        Log.e( "onResponse:2!!!!!!!!!!!!!!","!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
    public void set_free_activities() {
        Log.e( "onResponse:1!!!!!!!!!!!!!!!","!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne=new RecyclerView_Adapter_activities_all_in_one(free_arrayList_space_active_all_in_one_data_holder,ActivitiesListActivity.this,ActivitiesListActivity.this,activity_completed);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ActivitiesListActivity.this);
        list_activity.setLayoutManager(layoutManager);
        list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
        Log.e( "onResponse:2!!!!!!!!!!!!!!","!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
    public void set_paid_activities() {
        Log.e( "onResponse:1!!!!!!!!!!!!!!!","!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne=new RecyclerView_Adapter_activities_all_in_one(paid_arrayList_space_active_all_in_one_data_holder,ActivitiesListActivity.this,ActivitiesListActivity.this,activity_completed);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ActivitiesListActivity.this);
        list_activity.setLayoutManager(layoutManager);
        list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
        Log.e( "onResponse:2!!!!!!!!!!!!!!","!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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
        arrayList_space_active_all_in_one_data_holder.clear();
        free_arrayList_space_active_all_in_one_data_holder.clear();
        paid_arrayList_space_active_all_in_one_data_holder.clear();
        level.clear();
        domain.clear();
        key_domain.clear();
        activity_completed.clear();
        RequestQueue requestQueue= Volley.newRequestQueue(ActivitiesListActivity.this);
        String url="http://43.205.45.96/api/spaceactive_activities.php";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e( "onResponse:-----------------",response.toString());
                JSONArray jsonArray= null;
                try {
                    jsonArray = response.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        Log.e( "onResponse:----"+i+"------",jsonObject.toString());
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
                        Log.e( "onResponse:-------------------------------------------------","----------------------------------------------------------");
                        arrayList_space_active_all_in_one_data_holder.get(i).print_All();
                        if (arrayList_space_active_all_in_one_data_holder.get(i).activity_type_status.equals("free")){
                            free_arrayList_space_active_all_in_one_data_holder.add(arrayList_space_active_all_in_one_data_holder.get(i));
                            Log.e( "Free:>>>>>>>>>>>>>>>>>>>>",arrayList_space_active_all_in_one_data_holder.get(i).activity_no);
                        }else if (arrayList_space_active_all_in_one_data_holder.get(i).activity_type_status.equals("paid")){
                            paid_arrayList_space_active_all_in_one_data_holder.add(arrayList_space_active_all_in_one_data_holder.get(i));
                            Log.e( "paid:>>>>>>>>>>>>>>>>>>>>",arrayList_space_active_all_in_one_data_holder.get(i).activity_no);
                        }
                        if (level.containsKey(arrayList_space_active_all_in_one_data_holder.get(i).activity_level)){
                            Log.e( "Old okok :>>>>>>>>>>>>>>>>>",arrayList_space_active_all_in_one_data_holder.get(i).activity_level);
                            level.get(arrayList_space_active_all_in_one_data_holder.get(i).activity_level).add(arrayList_space_active_all_in_one_data_holder.get(i));
                        }else {
                            ArrayList<space_active_data_holder_all_in_one>arrayList=new ArrayList<>();
                            arrayList.add(arrayList_space_active_all_in_one_data_holder.get(i));
                            level.put(arrayList_space_active_all_in_one_data_holder.get(i).activity_level,arrayList);
                            Log.e( "New okokokok :>>>>>>>>>>>>>>>>>",arrayList_space_active_all_in_one_data_holder.get(i).activity_level);
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
                    Log.e( "onResponse:1","---------------------------------------");
                    RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne=new RecyclerView_Adapter_activities_all_in_one(arrayList_space_active_all_in_one_data_holder,ActivitiesListActivity.this,ActivitiesListActivity.this,activity_completed);
                    RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ActivitiesListActivity.this);
                    list_activity.setLayoutManager(layoutManager);
                    list_activity.setAdapter(recyclerViewAdapterActivitiesAllInOne);
                    Log.e( "onResponse:2","---------------------------------------");
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
        JsonObjectRequest jsonObjectRequest1=new JsonObjectRequest(url1 + MainActivity.ACCOUNT.getAccount_id(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e( "onResponse:@@@@@@@@@@@@@@",response.toString());
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
    @Override
    public void onclick_space_active(int position) {
        Log.e( "onclick_space_active:-----------",position+"-----------");


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

    }
}