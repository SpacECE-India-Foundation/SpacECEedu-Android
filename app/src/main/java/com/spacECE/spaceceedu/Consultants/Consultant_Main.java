package com.spacECE.spaceceedu.Consultants;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;

import com.spacECE.spaceceedu.UsefulFunctions;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class Consultant_Main extends AppCompatActivity {

    public static ArrayList<ConsultantCategory> categoryList = new ArrayList<>();
    NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.consultant_main_navButton_all:
                            selectedFragment = new Fragment_Consultant_Categories();
                            break;
                        case R.id.consultant_main_navButton_my:
                            selectedFragment = new Fragment_Appointments_For_User();
                            break;
                        case R.id.consultant_main_navButton_appointments:
                            selectedFragment = new Fragment_Appointments_For_Consultants();
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

        // generateMyConsList();
        if(MainActivity.ACCOUNT!=null){
            if (MainActivity.ACCOUNT.isCONSULTANT()) {
                bottomNav.inflateMenu(R.menu.consultant_main_consultants_bottomnav);
                generateAppointmentsListForUser();
                generateAppointmentsListForConsultant();
            } else {
                bottomNav.inflateMenu(R.menu.consultant_main_user_bottomnav);
                generateAppointmentsListForUser();
            }
        }else{
            bottomNav.inflateMenu(R.menu.consultant_main_nouser_bottomnav);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.ConsultantMain_Frame,
                        new Fragment_Consultant_Categories()).commit();
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.ConsultantMain_Frame,
                new Fragment_Consultant_Categories()).commit();
    }

    private void generateAppointmentsListForUser() {

        new Thread(new Runnable() {

            JSONObject jsonObject;
            JSONArray jsonArray;

            String url = "http://spacefoundation.in/test/SpacECE-PHP/ConsultUs/api_user_appoint.php";

            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody fromBody = new FormBody.Builder()
                        .add("u_id", MainActivity.ACCOUNT.getAccount_id())
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(fromBody)
                        .build();


                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("Registration Error ApI " + e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                        try {
                            jsonObject = new JSONObject(response.body().string());
                            Log.d("Login", "onResponse: " + jsonObject);

                            if(jsonObject.getString("status").equals("success")) {
                                jsonArray = jsonObject.getJSONArray("data");

                                Fragment_Appointments_For_User.appointmentsArrayList = new ArrayList<>();

                                try {
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));

                                        Appointment newAppointment = new Appointment(response_element.getString("c_id"), response_element.getString("c_name"), response_element.getString("u_name")
                                                , response_element.getString("c_image"), response_element.getString("u_image"), response_element.getString("b_time")
                                                , response_element.getString("end_time"));
                                        Fragment_Appointments_For_User.appointmentsArrayList.add(newAppointment);
                                    }
                                    Log.i("Appointments_For_User:::::", Fragment_Appointments_For_User.appointmentsArrayList.toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    private void generateAppointmentsListForConsultant() {

        new Thread(new Runnable() {

            JSONObject jsonObject;
            JSONArray jsonArray;

            String url = "http://spacefoundation.in/test/SpacECE-PHP/ConsultUs/api_user_appoint.php";

            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody fromBody = new FormBody.Builder()
                        .add("c_id", MainActivity.ACCOUNT.getAccount_id())
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(fromBody)
                        .build();


                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("Registration Error ApI " + e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                        try {
                            jsonObject = new JSONObject(response.body().string());
                            Log.d("Login", "onResponse: " + jsonObject);

                            if(jsonObject.getString("status").equals("success")) {

                                jsonArray = jsonObject.getJSONArray("data");

                                Fragment_Appointments_For_Consultants.appointmentsArrayList = new ArrayList<>();

                                try {
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));

                                        Appointment newAppointment = new Appointment(response_element.getString("c_id"), response_element.getString("c_name"), response_element.getString("u_name")
                                                , response_element.getString("c_image"), response_element.getString("u_image"), response_element.getString("b_time")
                                                , response_element.getString("end_time"));
                                        Fragment_Appointments_For_Consultants.appointmentsArrayList.add(newAppointment);
                                    }
                                    Log.i("Appointments_For_Consultant:::::", Fragment_Appointments_For_Consultants.appointmentsArrayList.toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    public static void SetDateTimeDay(int position, ArrayList<Appointment> myConsultants, TextView date, TextView time, TextView day) {
        Date dateObject = new Date();
        try {
            dateObject = UsefulFunctions.DateFunc.StringToDate(myConsultants.get(position).getBookedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        date.setText(UsefulFunctions.DateFunc.DateObjectToDate(dateObject));
        time.setText(UsefulFunctions.DateFunc.DateObjectToTime(dateObject));
        day.setText(UsefulFunctions.DateFunc.DateObjectToDay(dateObject));
    }


}
