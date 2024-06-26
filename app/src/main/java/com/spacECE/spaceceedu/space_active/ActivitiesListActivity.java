package com.spacECE.spaceceedu.space_active;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.spacECE.spaceceedu.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivitiesListActivity extends AppCompatActivity implements ClickListener{
    RecyclerView list_activity;
    ArrayList<space_active_data_holder_all_in_one>arrayList_space_active_all_in_one_data_holder=new ArrayList<space_active_data_holder_all_in_one>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_list);

        list_activity=findViewById(R.id.list_activity);

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
                    }
                    Log.e( "onResponse:1","---------------------------------------");
                    RecyclerView_Adapter_activities_all_in_one recyclerViewAdapterActivitiesAllInOne=new RecyclerView_Adapter_activities_all_in_one(arrayList_space_active_all_in_one_data_holder,ActivitiesListActivity.this,ActivitiesListActivity.this);
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

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onclick_space_active(int position) {
        Log.e( "onclick_space_active:-----------",position+"-----------");
        Intent intent=new Intent(ActivitiesListActivity.this, second_page_for_space_active_with_image.class);
        startActivity(intent);
    }
}