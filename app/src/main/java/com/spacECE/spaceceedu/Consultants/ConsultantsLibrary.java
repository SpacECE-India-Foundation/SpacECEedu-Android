package com.spacECE.spaceceedu.Consultants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.spacECE.spaceceedu.ApiFunctions;
import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.VideoLibrary.Topic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConsultantsLibrary extends AppCompatActivity {
    private ArrayList<Consultant> consultants= new ArrayList<Consultant>();
    int rating=5;

    String categoryFilter;
    String ratingFilter;

    private Consultants_RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private Consultants_RecyclerViewAdapter.RecyclerViewClickListener listener;

    private RequestQueue mQueue;
    private String URL = "https://run.mocky.io/v3/837c1168-db9e-41b6-8b4f-5856dd2136a8";


    private Spinner s_categories;
    private Spinner s_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultants_library);
/*

        mQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("Consultants");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));

Consultant newTopic = new Consultant(response_element.getString("name"),
                                    response_element.getString("consultant_id"),
                                    response_element.getString("profilePic_src"),
                                    response_element.getString("speciality"),
                                    response_element.getInt("price"),
                                    response_element.getString("rating"));

                            Log.i("List----",String.valueOf(response_element.getString("name")+
                                    response_element.getString("consultant_id")+
                                    response_element.getString("profilePic_src")+
                                    response_element.getString("speciality")+
                                    response_element.getInt("price")+
                                    response_element.getString("rating")));
                            //consultants.add(newTopic);
                        }
                        setUpAdapter();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("API","NO RESPONSE!!!!!!!!!!!!");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
*/


        recyclerView= findViewById(R.id.RecycleView);
        s_categories=findViewById(R.id.Spinner_Category);
        s_rating=findViewById(R.id.Spinner_Rating);

        s_rating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryFilter=String.valueOf(s_rating.getItemAtPosition(i));
                if(String.valueOf(s_rating.getItemAtPosition(i))=="Categories"){
                    categoryFilter=null;
                }
                adapter.listUpdate(categoryFilter,ratingFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                categoryFilter=null;
                adapter.listUpdate(categoryFilter,ratingFilter);
            }
        });
        s_rating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ratingFilter=String.valueOf(s_rating.getItemAtPosition(i));
                if(String.valueOf((s_rating.getItemAtPosition(i)))=="Rating"){
                    ratingFilter=null;
                }
//               adapter.listUpdate(categoryFilter,ratingFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ratingFilter=null;
                adapter.listUpdate(categoryFilter,ratingFilter);
            }
        });
        //addToList();
            getList();
        //setUpAdapter();

    }

    private void setUpAdapter() {
        Log.i("Adapter","Started");
        setOnClickListener();
        Consultants_RecyclerViewAdapter adapter = new Consultants_RecyclerViewAdapter(consultants,listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        Log.i("Adapter","Ended");
    }
//    private void addToList(){
//        consultants.add(new Consultant("Dr Kapoor", "consultant_id", "profilePic_src", "Dentist, Dermatology", 1000, "5"));
//        consultants.add(new Consultant("Dr Pankaj Kumar", "consultant_id", "profilePic_src", "Psychology, Respiratory", 3000, "1"));
//        consultants.add(new Consultant("Dr Harsh Jha", "consultant_id", "profilePic_src","Internal Medicine", 1500, "5"));
//        consultants.add(new Consultant("Dr Gaurav Singh", "consultant_id", "profilePic_src", "Dentist,Cardiologist", 3500, "4"));
//        consultants.add(new Consultant("Dr Aman", "consultant_id", "profilePic_src", "Lungs, Neuro", 1000, "2"));
//        consultants.add(new Consultant("Dr Jatin Chabra", "consultant_id", "profilePic_src", "Psychology", 5000, "3"));
//        Log.i("List","Done");
//        setUpAdapter();
//    }

    private void setOnClickListener() {
        listener = new Consultants_RecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), ConsultantProfile.class);
                intent.putExtra("consultant_name", consultants.get(position).getName());
                intent.putExtra("pic_src", consultants.get(position).getProfilePic_src());
                intent.putExtra("about", consultants.get(position).getAbout());
                intent.putExtra("consultant_id",consultants.get(position).getConsultant_id());
                intent.putExtra("categories",consultants.get(position).getCategories());

                startActivity(intent);
            }
        };
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.search,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
*/


 public void getList(){
     final boolean[] COMPLETED = {false};
         final JSONObject[] apiCall = {null};

         Thread thread = new Thread(new Runnable() {

             @Override
             public void run() {

                 apiCall[0] = ApiFunctions.UsingGetAPI("https://run.mocky.io/v3/837c1168-db9e-41b6-8b4f-5856dd2136a8");
                 try {
                     Log.i("Object Obtained: ", apiCall[0].get("Consultants").toString());
                 } catch (JSONException e) {
                     Log.i("API Response:","Eror");
                     e.printStackTrace();
                 }

                 JSONArray jsonArray = null;
                 try {
                     jsonArray = apiCall[0].getJSONArray("Consultants");
                     Log.i("API : ",apiCall[0].toString());
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }


                 try {
                     for (int i = 0; i < jsonArray.length(); i++) {

                         JSONObject response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));

                         Consultant newConsultants = new Consultant(response_element.getString("name"),
                                 response_element.getString("consultant_id"),
                                 response_element.getString("profilePic_src"),
                                 response_element.getString("Speciality"),
                                 response_element.getInt("price"),
                                 response_element.getString("rating"));

                         consultants.add(newConsultants);
                     }
                     COMPLETED[0] =true;
                     Log.i("CONSULTANTS:::::",consultants.toString());

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }

             }
             });

         thread.start();
        setUpAdapter();
 }
}
