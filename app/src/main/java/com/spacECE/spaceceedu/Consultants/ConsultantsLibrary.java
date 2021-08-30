package com.spacECE.spaceceedu.Consultants;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class ConsultantsLibrary extends AppCompatActivity {
    public static ArrayList<Consultant> consultantsList = new ArrayList<>();

    String categoryFilter;
    String ratingFilter;

    private Consultants_RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private Consultants_RecyclerViewAdapter.RecyclerViewClickListener listener;

    private String URL = "http://educationfoundation.space/ConsultUs/api_getconsultant?user=all";


    private Spinner s_categories;
    private Spinner s_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultants_library);


        recyclerView = findViewById(R.id.RecycleView);
        s_categories = findViewById(R.id.Spinner_Category);
        s_rating = findViewById(R.id.Spinner_Rating);

//        s_rating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                categoryFilter = String.valueOf(s_rating.getItemAtPosition(i));
//                if (String.valueOf(s_rating.getItemAtPosition(i)) == "Categories") {
//                    categoryFilter = null;
//                }
//                adapter.listUpdate(categoryFilter, ratingFilter);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                categoryFilter = null;
//                adapter.listUpdate(categoryFilter, ratingFilter);
//            }
//        });
//        s_rating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                ratingFilter = String.valueOf(s_rating.getItemAtPosition(i));
//                if (String.valueOf((s_rating.getItemAtPosition(i))) == "Rating") {
//                    ratingFilter = null;
//                }
//               adapter.listUpdate(categoryFilter,ratingFilter);
//              }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                ratingFilter = null;
//                adapter.listUpdate(categoryFilter, ratingFilter);
//            }
//        });
// addToList();
        setUpAdapter();
    }

    private void setUpAdapter() {
        Log.i("Adapter", "Started");
        setOnClickListener();
        Consultants_RecyclerViewAdapter adapter = new Consultants_RecyclerViewAdapter(consultantsList, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        Log.i("Adapter", "Ended");
    }


    private void setOnClickListener() {
        listener = new Consultants_RecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), ConsultantProfile.class);
                intent.putExtra("profile_pic", consultantsList.get(position).getProfilePic_src());
                intent.putExtra("consultant_id", consultantsList.get(position).getConsultant_id());
                intent.putExtra("consultant_name", consultantsList.get(position).getName());
                intent.putExtra("speciality", consultantsList.get(position).getCategories());
                intent.putExtra("chamber", consultantsList.get(position).getAddress());
                intent.putExtra("fee", consultantsList.get(position).getPrice());
                intent.putExtra("language", consultantsList.get(position).getLanguage());
                intent.putExtra("days_from", consultantsList.get(position).getDaysFrom());
                intent.putExtra("days_to", consultantsList.get(position).getDaysTo());
                intent.putExtra("timing_to", consultantsList.get(position).getTiming_end());
                intent.putExtra("timing_from", consultantsList.get(position).getTiming_start());
                intent.putExtra("qualification", consultantsList.get(position).getQualification());
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
//
// public void getList(String url, ArrayList<Consultant> aList){
//     final boolean[] COMPLETED = {false};
//         final JSONObject[] apiCall = {null};
//
//         Thread thread = new Thread(new Runnable() {
//
//             @Override
//             public void run() {
//
//                 apiCall[0] = ApiFunctions.UsingGetAPI(url);
//                 try {
//                     Log.i("Object Obtained: ", apiCall[0].get("data").toString());
//                 } catch (JSONException e) {
//                     Log.i("API Response:","Error");
//                     e.printStackTrace();
//                 }
//
//                 JSONArray jsonArray = null;
//                 try {
//                     jsonArray = apiCall[0].getJSONArray("data");
//                     Log.i("API : ",apiCall[0].toString());
//                 } catch (JSONException e) {
//                     e.printStackTrace();
//                 }
//
//
//                 try {
//                     for (int i = 0; i < jsonArray.length(); i++) {
//
//                         JSONObject response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));
//
//                         Consultant newConsultants = new Consultant(response_element.getString("name"),response_element.getString("c_id"),response_element.getString("img")
//                                 ,response_element.getString("category"),response_element.getString("office"),response_element.getString("mobile"),response_element.getString("lang")
//                                 ,response_element.getString("email"),response_element.getString("ctime"),response_element.getString("stime"),response_element.getInt("fee"),response_element.getString("lno"));
//
//                         aList.add(newConsultants);
//                     }
//                     COMPLETED[0] =true;
//                     Log.i("CONSULTANTS ::::: ===>",aList.toString());
//
//                 } catch (JSONException e) {
//                     e.printStackTrace();
//                 }
//
//             }
//             });
//
//         thread.start();
//        while(true) {
//            if(COMPLETED[0]){
//                setUpAdapter();
//                return;
//            }
//        }
// }

//    private void addToList(){
//        consultants.add(new Consultant("Dr Kapoor", "consultant_id", "profilePic_src", "Dentist, Dermatology", 1000, "5"));
//        consultants.add(new Consultant("Dr Pankaj Kumar", "consultant_id", "profilePic_src", "Psychology, Respiratory", 3000, "1"));
//        consultants.add(new Consultant("Dr Harsh Jha", "consultant_id", "profilePic_src","Internal Medicine", 1500, "5"));
//        consultants.add(new Consultant("Dr Gaurav Singh", "consultant_id", "profilePic_src", "Dentist,Cardiologist", 3500, "4"));
//        consultants.add(new Consultant("Dr Aman", "consultant_id", "profilePic_src", "Lungs, Neuro", 1000, "2"));
//        consultants.add(new Consultant("Dr Jatin Chabra", "consultant_id", "profilePic_src", "Psychology", 5000, "3"));
//        Log.i("Consultant Listing :::","Done => "+consultants.toString());
//        setUpAdapter();
//    }

}