package com.spacECE.spaceceedu.Consultants;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Authentication.LoginActivity;
import com.spacECE.spaceceedu.UsefulFunctions;
import com.spacECE.spaceceedu.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Consultant_Main extends AppCompatActivity {

    Account account= null;

    public static ArrayList<ConsultantCategory> categoryList = new ArrayList<>();
    NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.consultant_main_navButton_all:
                            selectedFragment = new Consultant_Categories();
                            break;
                        case R.id.consultant_main_navButton_my:
                            selectedFragment = new Fragment_Consultants_MyAppointments();
                            break;
                        case R.id.consultant_main_navButton_appointments:
                            selectedFragment = new Fragment_Consultants_Consultants_appointments();
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.ConsultantMain_Frame,
                            selectedFragment).commit();

                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_main);

        BottomNavigationView bottomNav = findViewById(R.id.Consultant_Main_BottomNav);
        bottomNav.setOnItemSelectedListener(navListener);


        account= LoginActivity.ACCOUNT;
       // generateMyConsList();
        if(false){
            if (true) {
                bottomNav.inflateMenu(R.menu.consultant_main_consultants_bottomnav);
                getSupportFragmentManager().beginTransaction().replace(R.id.ConsultantMain_Frame,
                        new Fragment_Consultants_Consultants_appointments()).commit();
                generateConsultantsAppointmentList();
            } else {
                bottomNav.inflateMenu(R.menu.consultant_main_bottomnav);
                generateUserConsultantsList();
            }
        }else{
            bottomNav.inflateMenu(R.menu.consultant_main_bottomnav);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.ConsultantMain_Frame,
                        new Consultant_Categories()).commit();
            }
        }
    }

    private void generateUserConsultantsList() {
        final boolean[] COMPLETED = {false};
        final JSONObject[] apiCall = {null};

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try{
                    apiCall[0] = UsefulFunctions.UsingGetAPI("http://3.109.14.4/ConsultUs/api_user_appoint?user=raju%20rastogi");
                    try {
                        Log.i("Object Obtained: ", apiCall[0].get("data").toString());
                    } catch (JSONException e) {
                        Log.i("API Response:", "Error");
                        e.printStackTrace();
                    }

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = apiCall[0].getJSONArray("data");
                        Log.i("API : ", apiCall[0].toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));

                            UserAppointments newAppointment = new UserAppointments(response_element.getString("date_appointment"), response_element.getString("date_appointment")
                                    , response_element.getString("date_appointment"), response_element.getString("date_appointment"), response_element.getString("cname")
                                    , response_element.getString("username"), response_element.getString("cid"), response_element.getString("category")
                                    , response_element.getString("status"), response_element.getString("bid"), response_element.getString("com_mob")
                                    , response_element.getString("mobile"));
                            Fragment_Consultants_MyAppointments.myAppointments.add(newAppointment);
                        }
                        COMPLETED[0] = true;
                        Log.i("My CONSULTANTS:::::", Fragment_Consultants_MyAppointments.myAppointments.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }   catch (RuntimeException runtimeException){
                    Log.i("RUNTIME EXCEPTION:::", "Server did not respons");
                }
            }
        });

        thread.start();
    }

    private void generateConsultantsAppointmentList() {
        final boolean[] COMPLETED = {false};
        final JSONObject[] apiCall = {null};

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try{
                    apiCall[0] = UsefulFunctions.UsingGetAPI("http://3.109.14.4/ConsultUs/api_consultant_appoint?consultant=raju%20rastogi");
                    try {
                        Log.i("Object Obtained: ", apiCall[0].get("consultant_taken_appoint").toString());
                    } catch (JSONException e) {
                        Log.i("API Response:", "Error");
                        e.printStackTrace();
                    }

                    JSONArray jsonUArray = null;
                    JSONArray jsonCArray = null;
                    try {
                        jsonUArray = apiCall[0].getJSONArray("consultant_taken_appoint");
                        jsonCArray = apiCall[0].getJSONArray("consultant_given_appoint");
                        Log.i("API : ", apiCall[0].toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        for (int i = 0; i < jsonUArray.length(); i++) {

                            JSONObject response_element = new JSONObject(String.valueOf(jsonUArray.getJSONObject(i)));

                            UserAppointments newAppointment = new UserAppointments(response_element.getString("date_appointment"), response_element.getString("date_appointment")
                                    , response_element.getString("date_appointment"), response_element.getString("date_appointment"), response_element.getString("cname")
                                    , response_element.getString("username"), response_element.getString("cid"), response_element.getString("category")
                                    , response_element.getString("status"), response_element.getString("bid"), response_element.getString("com_mob")
                                    , response_element.getString("mobile"));
                            Fragment_Consultants_MyAppointments.myAppointments.add(newAppointment);
                        }
                        for (int i = 0; i < jsonCArray.length(); i++) {

                            JSONObject response_element = new JSONObject(String.valueOf(jsonUArray.getJSONObject(i)));

                            UserAppointments newAppointment = new UserAppointments(response_element.getString("date_appointment"), response_element.getString("date_appointment")
                                    , response_element.getString("date_appointment"), response_element.getString("date_appointment"), response_element.getString("cname")
                                    , response_element.getString("username"), response_element.getString("cid"), response_element.getString("category")
                                    , response_element.getString("status"), response_element.getString("bid"), response_element.getString("com_mob")
                                    , response_element.getString("mobile"));
                            Fragment_Consultants_Consultants_appointments.consultantsAppointments.add(newAppointment);
                        }
                        COMPLETED[0] = true;
                        Log.i("My CONSULTANTS:::::", Fragment_Consultants_MyAppointments.myAppointments.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }   catch (RuntimeException runtimeException){
                    Log.i("RUNTIME EXCEPTION:::", "Server did not respons");
                }
            }
        });

        thread.start();
    }

//    private void generateCatList() {
//        Log.i("Generate List : "," Generating....");
//        categoryList.add(new ConsultantCategory("Psychologist","Nice"));
//        categoryList.add(new ConsultantCategory("Internal Medicine","Nice"));
//        categoryList.add(new ConsultantCategory("Anesthesia","Nice"));
//        categoryList.add(new ConsultantCategory("Dermatologist","Nice"));
//        categoryList.add(new ConsultantCategory("Dentist","Nice"));
//        categoryList.add(new ConsultantCategory("Good","Nice"));
//        categoryList.add(new ConsultantCategory("Great","Nice"));
//        categoryList.add(new ConsultantCategory("Cool","Nice"));
//        categoryList.add(new ConsultantCategory("Kind","Nice"));
//        categoryList.add(new ConsultantCategory("Unkind","Nice"));
//        categoryList.add(new ConsultantCategory("Right","Nice"));
//        categoryList.add(new ConsultantCategory("Down","Nice"));
//        categoryList.add(new ConsultantCategory("Up","Nice"));
//        Log.i("Generate List : "," Generated");
//    }

    public static class ConsultantAppointments {
        private String client_id, name, date, month, year, time, contact_number, status, b_id;

        public ConsultantAppointments(String client_id, String name, String date, String month, String year, String time
                , String contact_number, String status, String b_id) {
            this.client_id = client_id;
            this.name = name;
            this.date = date;
            this.month = month;
            this.year = year;
            this.time = time;
            this.contact_number = contact_number;
            this.status = status;
            this.b_id = b_id;
        }

        public String getClient_id() {
            return client_id;
        }

        public String getName() {
            return name;
        }

        public String getDate() {
            return date;
        }

        public String getMonth() {
            return month;
        }

        public String getYear() {
            return year;
        }

        public String getTime() {
            return time;
        }

        public String getContact_number() {
            return contact_number;
        }

        public String getStatus() {
            return status;
        }

        public String getB_id() {
            return b_id;
        }
    }
}
